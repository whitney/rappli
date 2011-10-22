package models;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;

import play.db.jpa.Model;

@Entity(name = "listing_sources")
public class ListingSource extends Model {	
	@Column 
    public String name;
	
    @Column
    public String code;
    
    @OneToMany(mappedBy = "listingSource", cascade = CascadeType.ALL)
	public List<RentalListing> listings;
    
    public ListingSource(String name, String code) {
    	this.name = name;
    	this.code = code;
    }
}
