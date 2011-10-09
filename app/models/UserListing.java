package models;

import java.util.*;
import javax.persistence.*;

import play.db.jpa.Model;

@Entity(name = "users_listings")
public class UserListing extends Model {
	
	@ManyToOne
	@JoinColumn(name="user_id")
    public User user;
	
	@ManyToOne
	@JoinColumn(name="rental_listing_id")
    public RentalListing rentalListing;
    
    public UserListing(User user, RentalListing rentalListing) {
        this.user = user;
        this.rentalListing = rentalListing;
    }
}
