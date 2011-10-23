package controllers;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.List;

import models.UnauthedUserListing;
import models.RentalListing;

import play.Logger;
import play.data.validation.Required;

public class UnauthedUserListings extends Application {

	public static void create(@Required String url, @Required String html) {
		// check if rental listing record already exists...
		// if it doesn't exist then create it, 
		// then add the rentalListing record to the unauthed_users_listings table.
		RentalListing rentalListing = new RentalListing(url, html, "950");
		rentalListing.save();
		
		String ipv4 = request.remoteAddress;
		try {
			InetAddress addr = InetAddress.getByName(ipv4);
			UnauthedUserListing unauthedListing = new UnauthedUserListing(addr, rentalListing);
			unauthedListing.save();
		} catch(UnknownHostException e) {
			Logger.error(">>> Invalid ipv4 address: " + ipv4);
		}
	}
	
	public static void get() {
		String ipv4 = request.remoteAddress;
		List<UnauthedUserListing> listings = null;
		try {
			InetAddress addr = InetAddress.getByName(ipv4);
			listings = UnauthedUserListing.findByIp(addr);
			renderJSON(listings);
		} catch(UnknownHostException e) {
			Logger.error(">>> Invalid ipv4 address: " + ipv4);
		}	
	}
}
