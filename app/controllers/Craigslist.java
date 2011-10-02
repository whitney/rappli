package controllers;
import java.io.IOException;
import play.mvc.results.*;

import org.jsoup.*;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


public class Craigslist extends Application {
	public static void processListing(String url) {
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
		url = url.replaceAll(".html", "");
		String[] urlFragments = url.split("/");
		long postId = Long.parseLong(
				urlFragments[urlFragments.length - 1]);
		System.out.println(postId);
		
		// Extract post title
		String postTitle = doc.select("h2").text();
		System.out.println(postTitle);
		
		// Extract post date
		// Elements postDateElement = doc.select("h2 + hr");
		// System.out.println(postDateElement);
		// Element headerRow = postDateElement.first();
		// System.out.println(headerRow.firstElementSibling());
		// Element headerRow = postDateElement.first();
		// String postDate = headerRow.firstElementSibling().text();
		// System.out.println(postDate);
		
		
		// Extract reply-to email
		Elements replyToLink = doc.select("br + a[href~=mailto]");
		String replyToEmail = replyToLink.text();
		System.out.println(replyToEmail);
		String mailToHref = replyToLink.attr("href");
		System.out.println(mailToHref);
		// Extract Google maps link
		Elements googleMapLink = null;
		googleMapLink = doc.select("a:contains(google map)");
		if (googleMapLink != null) {
			String googleMapHref = googleMapLink.attr("href");
		}
	}

	
	public static void index() {
		render();
	}
}

