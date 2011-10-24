import models.RentalListing;
import models.UnauthedUserListing;

import play.test.*;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.*;

import org.junit.Before;
import org.junit.Test;

import play.test.Fixtures;

public class UnauthedUserListingTest extends UnitTest {
	
    @Before
    public void setup() {
        Fixtures.deleteAllModels();
    }
    
	@Test
	public void createAndRetrieveUnauthedUserListings() throws UnknownHostException {
		String url0 = "http://newyork.craigslist.org/brk/abo/2627480745.html";
		String html0 = "<html>blah blah blah</html>";
		String price0 = "2500.00";
	    // Create a new listing and save it
	    RentalListing apt0 = new RentalListing(url0, html0, price0).save();
	    
		String url1 = "http://brooklyn.craigslist.org/brk/abo/2627480745.html";
		String html1 = "<html>meh, meh, meh</html>";
		String price1 = "7500.00";
	    RentalListing apt1 = new RentalListing(url1, html1, price1).save();
	    
	    String ip = "127.0.0.1";
	    InetAddress addr = InetAddress.getByName(ip);
		
	    // create the unauthedUserListings
	    new UnauthedUserListing(addr, apt0).save();
	    new UnauthedUserListing(addr, apt1).save();
	    
	    // Retrieve the listings
	    List<UnauthedUserListing> listings = UnauthedUserListing.findByIp(addr);
	    
	    // Tests 
	    assertNotNull(listings);
	    assertEquals(2, listings.size());
	    
	    UnauthedUserListing l0 = listings.get(0);
	    UnauthedUserListing l1 = listings.get(1);
	    
	    assertTrue(l0.rentalListing.equals(apt0) || l0.rentalListing.equals(apt1));
	    assertTrue(l1.rentalListing.equals(apt0) || l1.rentalListing.equals(apt1));
	}
}
