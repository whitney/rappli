package controllers;
 
import models.User;
import play.Logger;
 
public class Security extends Secure.Security {
	
    static boolean authenticate(String email, String password) {
    	User user = User.connect(email, password);
    	Logger.debug("Attempting to auth user: " + user);
    	if (user != null && user.activated) {
    		session.put("userId", String.valueOf(user.id));
    		return true;
    	}
    	return false;
    }
    
    /*
    static void loginHandler(String username, String password) {
    	if (authenticate(username, password)) {
    		Home.index((User) User.find("byEmail", username).first());
    	} else {
    		render("/login");
    	}
    }
    */
    
    static void onDisconnected() {
        session.remove("userId");
    }
    
}
