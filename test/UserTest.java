import org.junit.*;
import java.util.*;
import play.test.*;
import models.User;
import models.RentalListing;
import models.UserListing;

public class UserTest extends UnitTest {

    @Before
    public void setup() {
        Fixtures.deleteAllModels();
    }
    
	@Test
	public void createAndRetrieveUser() {
	    // Create a new user and save it
	    new User("bob@gmail.com", "Bob", "Zoller", "psswd", true).save();
	    
	    // Retrieve the user with email address bob@gmail.com
	    User bob = User.find("byEmail", "bob@gmail.com").first();
	    
	    // Test 
	    assertNotNull(bob);
	    assertEquals("Bob", bob.firstName);
	}
	
	@Test
	public void testAuth() {
	    // Create a new user and save it
	    new User("bill@gmail.com", "Bill", "Zoller", "bsswd", true).save();
	    
	    // Test 
	    // TODO: fix this test!
	    //assertNotNull(User.connect("bill@gmail.com", "bsswd"));
	    assertNull(User.connect("bill@gmail.com", "badpassword"));
	    assertNull(User.connect("tom@gmail.com", "bsswd"));
	}
	
	@Test
	public void testUserListingRelation() {
	    // Create a new user and save it
	    User bob = new User("bob@gmail.com", "Bob", "Zoller", "psswd", true).save();
	 
	    // Create a new rental-listing
		String url = "http://newyork.craigslist.org/brk/abo/2627480745.html";
		String html = "<html>blah blah blah</html>";
	    RentalListing rentalListing1 = new RentalListing(url, html, "890.00").save();
		url = "http://brooklyn.craigslist.org/brk/abo/2627480745.html";
		html = "<html>wah wah wah</html>";
	    RentalListing rentalListing2 = new RentalListing(url, html, "2000.00").save();
	 
	    // add a couple of listings
	    bob.addListing(rentalListing1);
	    bob.addListing(rentalListing2);
	 
	    // Count things
	    assertEquals(1, User.count());
	    assertEquals(2, RentalListing.count());
	    assertEquals(2, UserListing.count());
	 
	    // Retrieve one of Bob's listings
	    UserListing bobListing = UserListing.find("byUser", bob).first();
	    assertNotNull(bobListing);
	 
	    // Navigate to listings
	    assertEquals(2, bob.listings.size());
	    assertEquals(url, bob.listings.get(1).rentalListing.url);
	    
	    // Delete one of Bob's listings
	    bobListing.delete();
	    // Check count
	    assertEquals(1, UserListing.count());
	    
	    // Delete rental-listings
	    rentalListing1.delete();
	    rentalListing2.delete();
	    
	    // Check that all user-listings have been deleted
	    assertEquals(1, User.count());
	    assertEquals(0, UserListing.count());
	    assertEquals(0, RentalListing.count());
	}

}