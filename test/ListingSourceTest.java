import org.junit.*;

import java.util.*;
import play.test.*;
import models.ListingSource;

public class ListingSourceTest extends UnitTest {

    @Before
    public void setup() {
        Fixtures.deleteAllModels();
    }
    
	@Test
	public void createAndRetrieveRentalListing() {
		String name = "Source Name";
		String code = "CRAIG";
		
	    // Create a new listing source and save it
	    new ListingSource(name, code).save();
	    
	    // Retrieve the listing source with 
	    ListingSource source = ListingSource.find("byCode", code).first();
	    
	    // Test 
	    assertNotNull(source);
	    assertEquals(code, source.code);
	}

}