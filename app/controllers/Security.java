package controllers;
 
import models.User;
import play.Logger;

import play.data.validation.Required;
 
public class Security extends Secure.Security {
	
    static boolean authenticate(String email, String password) {
    	User user = User.connect(email, password);
    	Logger.debug("Attempting to auth user: " + user);
    	if (user != null && user.activated) {
    		session.put("userId", String.valueOf(user.id));
    		session.put("username", email);
    		session.put("email", email);
    		//session.put("user", user);
    		return true;
    	}
    	return false;
    }
    
    public static void login(@Required String email, @Required String password) {
    	if (authenticate(email, password)) {
    		User user = (User) User.find("byEmail", email).first();
    		Home.index(user);
    	} else {
    		render("Secure/login.html");
    	}
    }
    
    static void onDisconnected() {
        session.remove("userId");
    }
    
}
