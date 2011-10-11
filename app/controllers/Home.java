package controllers;

import models.User;

import play.mvc.Before;
import play.mvc.With;
import play.Logger;

@With(Secure.class)
public class Home extends Application {

	@Before
    static void setConnectedUser() {
        if(Security.isConnected()) {
            renderArgs.put("user", currentUser());
        }
    }
	
	public static void index(User user) {
		if (user == null)
			user = currentUser();
		Logger.debug("logged in user: " + user);
		render(user);
	}
}
