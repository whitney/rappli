package controllers;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import play.mvc.Http;
import play.mvc.Http.Cookie;
import play.mvc.Http.Request;
import play.mvc.Http.StatusCode;
import play.mvc.results.*;
import play.modules.redis.Redis;
import play.Logger;

import models.CraigslistListing;

import org.apache.commons.lang.StringEscapeUtils;
import org.jsoup.*;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;

public class Craigslist extends Application {
	
	private static Gson gson = new Gson();
	private static JsonParser jsonParser = new JsonParser();
	
	/**
	 * Create a queue key for storing a user's listing queue.:w
	 * 
	 * 
	 * If the user is authenticated use the sessionToken to form the key.
	 * If not use the user's IP address.
	 * 
	 * @param req
	 * @return
	 */
	public static String createQueueKey (Request req) {
		Cookie sessionToken = req.cookies.get("sessionToken");
		if (sessionToken != null) {
			return String.format("user:%s:urls", sessionToken.toString());
		} else {
			return String.format("noauth:%s:urls", request.remoteAddress);
		}
	}
	
	/**
	 * Convert a JSON string to a JsonObject.
	 * 
	 * @param json
	 * @return
	 */
	public static JsonObject parseJson(String json) {
		JsonObject jsonObj = (JsonObject) jsonParser.parse(json);
		return jsonObj;
	}
	
	/** 
	 * Handles a POST request for adding a listing to the user's queue.
	 * 
	 * @param body: Listing data as JSON.
	 */
	public static void queueListing(String body) {
		String queueKey = createQueueKey(request);
		JsonObject listingObj = parseJson(body);
		
		JsonObject listingDetails = processListing(listingObj);
		if (listingDetails == null) { 
			response.status = StatusCode.NOT_FOUND;
		}
		
		CraigslistListing listing = gson.fromJson(listingObj, CraigslistListing.class);

		// Add listing URL to a user's queue
		Redis.sadd(queueKey, listing.postUrl);
					
		// Process the listing and store the JSON for later
		String listingKey = String.format("listing:craigslist:%s", listing.postUrl);
		Redis.set(listingKey, listingObj.toString());
	}
	
	public static void dequeueListing(String url){
		String remoteIP = Http.Request.current().remoteAddress;
		String sessionKey = String.format("user:%s:urls", remoteIP);
		Redis.srem(sessionKey, url);
	}
	
	public static JsonObject processListing(JsonObject listing) {		
		Document doc = null;
		String url = listing.get("postUrl").getAsString();
		try {
			doc = Jsoup.connect(url).get();
		} catch (IllegalArgumentException e) {
			badRequest();
		} catch (IOException e) {
			badRequest();
		}
	
		// Extract the listing content
		Element content = doc.getElementById("userbody");
		if (content == null) {
			return null;
		}		
	
		// Extract image urls
		ArrayList<String> imgUrls = new ArrayList<String>();
		Elements images = content.getElementsByTag("img");
		if (!images.isEmpty()) {
			for (Element img : images) { 
				imgUrls.add(img.attr("src"));
				img.remove();			
			}
			String imagesJson = gson.toJson(imgUrls);
			JsonArray imgArr = jsonParser.parse(imagesJson).getAsJsonArray();
			listing.add("imgUrls", imgArr);
		}
		String contentHtml = content.html().split("<!-- START CLTAGS -->")[0];
		listing.addProperty("html", contentHtml);
		
		// Extract reply-to email
		Elements replyToLink = doc.select("br + a[href~=mailto]");
		listing.addProperty("contactEmail", replyToLink.text());
				
		// Extract Google maps link
		Elements googleMapLink = null;
		googleMapLink = doc.select("a:contains(google map)");
		if (googleMapLink != null) {
			listing.addProperty("mapLink", googleMapLink.attr("href"));
		}
		return listing;
	}



	public static void createDigest() {
		String queueKey = createQueueKey(request);
		Set<String> urls = Redis.smembers(queueKey);
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
}

