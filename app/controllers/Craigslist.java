package controllers;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import play.mvc.Http;
import play.mvc.results.*;
import play.modules.redis.Redis;

import models.CraigslistListing;

import org.jsoup.*;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.google.gson.Gson;

public class Craigslist extends Application {

	public static void queueListing(String url){
		String remoteIP = Http.Request.current().remoteAddress;
		String sessionKey = String.format("user:%s:urls", remoteIP);
		
		// Add listing URL to a user's queue
		Redis.sadd(sessionKey, url);
		CraigslistListing listing = processListing(url);
		
		// Process the listing and store the JSON for later
		Gson gson = new Gson();
		String listingJson = gson.toJson(listing);
		String listingKey = String.format("listing:craigslist:%s", url);
		Redis.set(listingKey, listingJson);
	}
	
	public static void dequeueListing(String url){
		String remoteIP = Http.Request.current().remoteAddress;
		String sessionKey = String.format("user:%s:urls", remoteIP);
		Redis.srem(sessionKey, url);
	}

	public static void createDigest() {
		String remoteIP = Http.Request.current().remoteAddress;
		String sessionKey = String.format("user:%s:urls", remoteIP);
		Set<String> urls = Redis.smembers(sessionKey);
		List<Object> urlList = new ArrayList<Object>(urls);
		List<CraigslistListing> listings = new ArrayList<CraigslistListing>();
		Gson gson = new Gson();
		for (int i = 0; i < urlList.size(); i++) {
			String url = urlList.get(i).toString();
			String listingJson = Redis.get(String.format("listing:craigslist:%s", url));
			CraigslistListing listing = gson.fromJson(listingJson, CraigslistListing.class);
			listings.add(listing);
		}
		render("Craigslist/digest.html", listings);
	}
	
	public static CraigslistListing processListing(String url) {
		CraigslistListing listing = new CraigslistListing();
		Document doc = null;
		try {
			doc = Jsoup.connect(url).get();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			badRequest();
		} catch (IOException e) {
			badRequest();
		}
		
		// Example: http://newyork.craigslist.org/brk/roo/2625483628.html
		listing.url = url;
		url = url.replaceAll(".html", "");
		String[] urlFragments = url.split("/");
		long postId = Long.parseLong(
				urlFragments[urlFragments.length - 1]);
		listing.id = postId;
		// Extract post title
		String postTitle = doc.select("h2").text();
		listing.title = postTitle;
		
		// Extract post date
		Elements dirtyDateTags = doc.select("h2 + hr");
		for (Element tag : dirtyDateTags) {
			System.out.println(tag.firstElementSibling());
		}

		/*
		 Element headerRow = postDateElement.first();
		 System.out.println(headerRow.firstElementSibling());
		 Element headerRow = postDateElement.first();
		 String postDate = headerRow.firstElementSibling().text();
		 System.out.println(postDate);
		*/
		
		// Extract reply-to email
		Elements replyToLink = doc.select("br + a[href~=mailto]");
		String replyToEmail = replyToLink.text();
		listing.email = replyToEmail;
		String mailToHref = replyToLink.attr("href");
		
		// Extract Google maps link
		Elements googleMapLink = null;
		googleMapLink = doc.select("a:contains(google map)");
		if (googleMapLink != null) {
			String googleMapHref = googleMapLink.attr("href");
		}
		
		return listing;
	}
}

