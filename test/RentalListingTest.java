import org.junit.*;

import java.util.*;
import play.test.*;
import models.RentalListing;

public class RentalListingTest extends UnitTest {

    @Before
    public void setup() {
        Fixtures.deleteAllModels();
    }
    
	@Test
	public void createAndRetrieveRentalListing() {
		String url = "http://newyork.craigslist.org/brk/abo/2627480745.html";
		String html = "<html>blah blah blah</html>";
		String price = "2500.00";
		
	    // Create a new listing and save it
	    new RentalListing(url, html, price).save();
	    
	    // Retrieve the listing with 
	    RentalListing apt = RentalListing.find("byUrl", url).first();
	    
	    // Test 
	    assertNotNull(apt);
	    assertEquals(url, apt.url);
	    assertEquals(html, apt.html);
	    assertEquals(price, apt.price);
	    assertNotNull(apt.createdAt);
	}

}