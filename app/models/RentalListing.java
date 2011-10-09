package models;

import java.util.*;

import javax.persistence.*;

import play.db.jpa.Model;

@Entity(name = "rental_listings")
public class RentalListing extends Model {
    @Column 
    public String url;
    
    @Column
    @Lob
    public String html;
	
    @OneToMany(mappedBy="rentalListing", cascade=CascadeType.ALL)
	public List<UserListing> userListings;
    
    public RentalListing(String url, String html) {
        this.url = url;
        this.html = html;
        this.userListings = new ArrayList<UserListing>();
    }
    
    @Override
    public String toString() {
    	return url + " " + html;
    }
}
