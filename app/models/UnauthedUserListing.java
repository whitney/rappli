package models;

import play.db.jpa.Model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity(name = "unauthed_users_listings")
public class UnauthedUserListing extends Model {
	
	@Column(name = "IPv4_address")
    public int ipAddress;
	
	@ManyToOne
	@JoinColumn(name="rental_listing_id")
    public RentalListing rentalListing;
    
    public UnauthedUserListing(int ipAddress, RentalListing rentalListing) {
        this.ipAddress = ipAddress;
        this.rentalListing = rentalListing;
    }
}
