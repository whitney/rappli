package models;

import java.util.*;
import javax.persistence.*;
import play.db.jpa.Model;

@Entity
@Table(name = "`users`")
public class User extends Model {
     
	@Id
	@Column(name="id")
	public long id;
    public String email;
    public String first_name;
    public String last_name;
	/*
	@ManyToMany
	@JoinTable(name="users_listings",
		joinColumns={@JoinColumn(name="user_id", referencedColumnName="id")},
		inverseJoinColumns={@JoinColumn(name="rental_listing_id", referencedColumnName="id")})
	private List<RentalListing> rentalListings;
	*/
    
    public User(String email, String first_name, String last_name) {
        this.email = email;
        this.first_name = first_name;
        this.last_name = last_name;
    }
}
