package controllers;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import models.User;
import util.TokenGenerator;
import util.EmailValidator;
import notifiers.MailManager;

import play.data.validation.Required;
import play.Logger;

public class Users extends Application {

	public static void signup() {
		render();
	}
	
	public static void signupHandler(@Required String email) {
		boolean validEmail = EmailValidator.valid(email);
    	User user = User.find("byEmail", email).first();
    	
    	if (validEmail && user == null) {
    		TokenGenerator tokenGen = new TokenGenerator();
    		String emailToken = tokenGen.nextToken();
    		Logger.debug("New user being created with email {}, token {}", email, emailToken);
    		user = new User(email, emailToken);
    		user.save();
    		// send email to user containing welcome copy and tokened activation link.
    		MailManager.signup(user);
    		render("Users/accountCreated.html", user);
    	} else {
    		// TODO: nicer treatment here
    		Logger.info("Account cannot be created for email: {}", email);
    		render("Users/signup.html");
    	}
	}
	
	public static void activate() {
		render();
	}
	
	public static void activateHandler(String email, String emailToken) {
		User user = User.find("byEmailAndEmailToken", email, emailToken).first();
		if (user == null) {
			// deal with some shits
		} else {
			// render signup completion form
			render("Users/accountComplete.html", user);
		}
	}
}
