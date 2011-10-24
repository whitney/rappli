package models;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.*;

import org.hibernate.annotations.Type;
import org.joda.time.contrib.hibernate.PersistentDateTime;
import org.joda.time.DateTime;

import play.db.jpa.Model;

@Entity(name = "rental_listings")
public class RentalListing extends Model {
    @Column 
    public String url;
    
    @Column
    @Lob
    public String html;
	
    // this represents a whole dollar amount in USD
    @Column 
    public int price;
    
    @ManyToOne
    @JoinColumn(name="listing_source_id")
	public ListingSource listingSource;
    
    @OneToMany(mappedBy="rentalListing", cascade=CascadeType.ALL)
	public List<UserListing> userListings;
    
	@Column(name = "created_at")
	@Type(type="org.joda.time.contrib.hibernate.PersistentDateTime")
	public DateTime createdAt;
    
    public RentalListing(String url, String html, int price) {
        this.url = url;
        this.html = html;
        this.price = price;
        this.userListings = new ArrayList<UserListing>();
        this.createdAt = new DateTime();
    }
    
    @Override
    public String toString() {
    	return url + " " + html;
    }
}
