package controllers;

import models.User;
import play.mvc.Before;
import play.mvc.With;
import play.Logger;

@With(Secure.class)
public class Home extends Application {

	/*
	@Before
    static void setConnectedUser() {
		renderArgs.put("user", currentUser());
    }
	*/
	
	public static void index(User user) {
		if (user == null)
			render(user);
		else
			render(currentUser());
	}
}
