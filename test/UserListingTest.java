import org.junit.*;

import java.util.*;
import play.test.*;
import models.User;
import models.RentalListing;
import models.UserListing;

public class UserListingTest extends UnitTest {

    @Before
    public void setup() {
        Fixtures.deleteAllModels();
    }
    
	@Test
	public void testCreateFetchDestroy() {
		
	    User bob = new User("bob@gmail.com", "Bob", "Zoller", "psswd", true).save();
	 
	    // Create a new rental-listing
		String url = "http://newyork.craigslist.org/brk/abo/2627480745.html";
		String html = "<html>blah blah blah</html>";
	    RentalListing rentalListing1 = new RentalListing(url, html, 890).save();
		url = "http://brooklyn.craigslist.org/brk/abo/2627480745.html";
		html = "<html>wah wah wah</html>";
	    RentalListing rentalListing2 = new RentalListing(url, html, 2000).save();
	 
	    // add a couple of listings
	    bob.addListing(rentalListing1);
	    bob.addListing(rentalListing2);
	 
	    // Count things
	    assertEquals(2, UserListing.count());
	 
	    // Retrieve one of Bob's listings
	    UserListing bobListing = UserListing.find("byUser", bob).first();
	    assertNotNull(bobListing);
	    assertNotNull(bobListing.user);
	    assertNotNull(bobListing.rentalListing);
	    assertNotNull(bobListing.createdAt);
	 
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
	}

}
