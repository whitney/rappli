package models;

import java.util.*;
import javax.persistence.*;
import play.db.jpa.Model;

@Entity
@Table(name="`users`")
public class User extends Model {
     
    public String email;
	@Column(name="first_name")
    public String firstName;
	@Column(name="last_name")
    public String lastName;
	@OneToMany(mappedBy="user", cascade=CascadeType.ALL)
	public List<UserListing> listings;
    
    public User(String email, String firstName, String lastName) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.listings = new ArrayList<UserListing>();
    }
    
    public User addListing(RentalListing rentalListing) {
    	UserListing userListing = new UserListing(this, rentalListing).save();
        this.listings.add(userListing);
        return this;
    }
}
