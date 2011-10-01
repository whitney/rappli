package models;

import java.util.*;
import javax.persistence.*;
import play.db.jpa.Model;

@Entity
@Table(name="`users`")
public class User extends Model {
     
	//@Id
	//@Column(name="id")
	//public long id;
    public String email;
	@Column(name="first_name")
    public String firstName;
	@Column(name="last_name")
    public String lastName;
	/*
	@ManyToMany
	@JoinTable(name="users_listings",
		joinColumns={@JoinColumn(name="user_id", referencedColumnName="id")},
		inverseJoinColumns={@JoinColumn(name="rental_listing_id", referencedColumnName="id")})
	private List<RentalListing> rentalListings;
	*/
    
    public User(String email, String firstName, String lastName) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
    }
}
