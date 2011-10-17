package controllers;

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
		
		//UnauthedUserListing unauthedListing = new UnauthedUserListing(ipv4, rentalListing);
		//unauthedListing.save();
	}
}
