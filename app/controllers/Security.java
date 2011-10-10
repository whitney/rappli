package controllers;
 
import models.User;
 
public class Security extends Secure.Security {
	
    static boolean authenticate(String email, String password) {
    	User user = User.connect(email, password);
    	if (user != null && user.activated) {
    		session.put("userId", String.valueOf(user.id));
    		return true;
    	}
    	return false;
    }
    
    static void onDisconnected() {
        session.remove("userId");
    }
}
