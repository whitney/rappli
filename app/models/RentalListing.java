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
	
    @Column 
    public int price;
    
    @ManyToOne
    @JoinColumn(name="listing_source_id")
	public ListingSource listingSource;
    
    @OneToMany(mappedBy="rentalListing", cascade=CascadeType.ALL)
	public List<UserListing> userListings;
    
    public RentalListing(String url, String html, int price) {
        this.url = url;
        this.html = html;
        this.price = price;
        this.userListings = new ArrayList<UserListing>();
    }
    
    @Override
    public String toString() {
    	return url + " " + html;
    }
}
