package models;

import java.util.*;
import javax.persistence.*;

import org.hibernate.annotations.Type;
import org.joda.time.contrib.hibernate.PersistentDateTime;
import org.joda.time.DateTime;

import play.db.jpa.Model;

@Entity(name = "users_listings")
public class UserListing extends Model {
	
	@ManyToOne
	@JoinColumn(name="user_id")
    public User user;
	
	@ManyToOne
	@JoinColumn(name="rental_listing_id")
    public RentalListing rentalListing;
	
	@Column(name = "created_at")
	@Type(type="org.joda.time.contrib.hibernate.PersistentDateTime")
	public DateTime createdAt;
    
    public UserListing(User user, RentalListing rentalListing) {
        this.user = user;
        this.rentalListing = rentalListing;
        this.createdAt = new DateTime();
    }
}
