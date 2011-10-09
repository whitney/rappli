package models;

import java.util.*;

import javax.persistence.*;

import play.db.jpa.Model;

@Entity
@Table(name="`rental_listings`")
public class RentalListing extends Model {
     
    public String url;
    @Lob
    public String html;
	@OneToMany(mappedBy="rentalListing", cascade=CascadeType.ALL)
	public List<UserListing> userListings;
    
    public RentalListing(String url, String html) {
        this.url = url;
        this.html = html;
        this.userListings = new ArrayList<UserListing>();
    }
}
