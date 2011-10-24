package models;

import org.joda.time.contrib.hibernate.PersistentDateTime;
import org.joda.time.DateTime;
import org.mindrot.jbcrypt.BCrypt;
import org.hibernate.annotations.Type;

import java.util.*;
import javax.persistence.*;

import play.Logger;
import play.db.jpa.Model;

@Entity(name = "users")
public class User extends Model {
	
	private static final int SALT_LEN = 12;
    
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
    
	@Column(name = "created_at")
	@Type(type="org.joda.time.contrib.hibernate.PersistentDateTime")
	public DateTime createdAt;
    
    public User(String email, String firstName, String lastName, String password, boolean activated) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.password = BCrypt.hashpw(password, BCrypt.gensalt());
        this.activated = activated;
        this.listings = new ArrayList<UserListing>();
        this.createdAt = new DateTime();
    }
    
    public User(String email, String emailToken) {
        this.email = email;
        this.activated = false;
        this.emailToken = emailToken;
        this.listings = new ArrayList<UserListing>();
        this.createdAt = new DateTime();
    }
    
    public User addListing(RentalListing rentalListing) {
    	UserListing userListing = new UserListing(this, rentalListing).save();
        this.listings.add(userListing);
        return this;
    }
    
    public static User connect(String email, String candidatePw) {
    	Logger.info(">>> email: " + email);
    	Logger.info(">>> candidatePw: " + candidatePw);
    	if (email == null || candidatePw == null)
    		return null;
    	User user = find("byEmail", email).first();
    	
    	Logger.info(">>> user: " + user);
    	
    	if (user == null)
    		return null;
    	
    	Logger.info(">>> checkpw: " + BCrypt.checkpw(candidatePw, user.password));
    	if (BCrypt.checkpw(candidatePw, user.password))
    		return user;
    	else
    		return null;
    }
    
    public void setPassword(String password) {
    	this.password = BCrypt.hashpw(password, BCrypt.gensalt());
    }
    
    @Override
    public String toString() {
    	return email + " " + password + " " + activated;
    }
}
