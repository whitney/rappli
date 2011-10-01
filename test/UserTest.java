import org.junit.*;
import java.util.*;
import play.test.*;
import models.User;

public class UserTest extends UnitTest {

	@Test
	public void createAndRetrieveUser() {
	    // Create a new user and save it
	    new User("bob@gmail.com", "Bob", "Zoller").save();
	    
	    // Retrieve the user with email address bob@gmail.com
	    User bob = User.find("byEmail", "bob@gmail.com").first();
	    
	    // Test 
	    assertNotNull(bob);
	    assertEquals("Bob", bob.firstName);
	}

}