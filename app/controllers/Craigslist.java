package controllers;
import java.io.IOException;

import org.jsoup.*;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Craigslist extends Application {

	public static void submitUrl(String url) {
		Document listing = null;
		try {
			listing = Jsoup.connect(url).get();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
				
		// Find the link to the owner's email
		Elements replyToLink = listing.select("br + a[href~=mailto]");
		String mailto = replyToLink.text();
		String mailtoHref = replyToLink.attr("href");
		
		Elements googleMapLink = null;		
		googleMapLink = listing.select("a:contains(google map)");
		String googleMapHref = googleMapLink.attr("href");
	}
}
