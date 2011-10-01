import org.junit.*;
import java.util.*;
import play.test.*;
import models.RentalListing;

public class RentalListingTest extends UnitTest {

	@Test
	public void createAndRetrieveRentalListing() {
		String url = "http://newyork.craigslist.org/brk/abo/2627480745.html";
		String html = "<html>blah blah blah</html>";
		
	    // Create a new listing and save it
	    new RentalListing(url, html).save();
	    
	    // Retrieve the listing with 
	    RentalListing apt = RentalListing.find("byUrl", url).first();
	    
	    // Test 
	    assertNotNull(apt);
	    assertEquals(url, apt.url);
	}

}