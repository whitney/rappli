package controllers;

import models.User;

import play.mvc.Before;
import play.mvc.With;

@With(Secure.class)
public class Home extends Application {

	@Before
    static void setConnectedUser() {
        if(Security.isConnected()) {
            User user = User.find("byEmail", Security.connected()).first();
            renderArgs.put("user", user);
        }
    }
	
	public static void index(User user) {
		if (user == null)
			user = currentUser();
		render(user);
	}
}
