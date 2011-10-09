package models;

import org.mindrot.jbcrypt.BCrypt;
import java.util.*;
import javax.persistence.*;
import play.db.jpa.Model;

@Entity(name = "users")
public class User extends Model {
    @Column 
    public String email;
	
    @Column(name = "first_name")
    public String firstName;
	
    @Column(name = "last_name")
    public String lastName;
	
    @Column
    public String password;
    
    @Column
    public boolean activated;
    
    @Column(name = "email_token")
    public String emailToken;
    
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
	public List<UserListing> listings;
    
    public User(String email, String firstName, String lastName, String password, boolean activated) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = BCrypt.hashpw(password, BCrypt.gensalt());
        this.activated = activated;
        this.listings = new ArrayList<UserListing>();
    }
    
    public User(String email, String emailToken) {
        this.email = email;
        this.activated = false;
        this.emailToken = emailToken;
        this.listings = new ArrayList<UserListing>();
    }
    
    public User addListing(RentalListing rentalListing) {
    	UserListing userListing = new UserListing(this, rentalListing).save();
        this.listings.add(userListing);
        return this;
    }
    
    public static User connect(String email, String candidatePw) {
    	if (email == null || candidatePw == null)
    		return null;
    	User user = find("byEmail", email).first();
    	if (user == null)
    		return null;
    	if (BCrypt.checkpw(candidatePw, user.password))
    		return user;
    	else
    		return null;
    }
    
    @Override
    public String toString() {
    	return email + " " + password;
    }
}
