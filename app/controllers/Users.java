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
	
	public static void activate(String email, String emailToken) {
		User user = User.find("byEmailAndEmailToken", email, emailToken).first();
		if (user == null) {
    		// TODO: put this information in the "flash" to inform the user what to do 
    		Logger.info("Account cannot be created for email: {}, token: {}", email, emailToken);
    		render("Users/signup.html");
		} else {
			render(user);
		}
	}
	
	public static void activateHandler(String email, String emailToken, String password, 
			String passwordConfirmation, String firstName, String lastName) {
		User user = User.find("byEmailAndEmailToken", email, emailToken).first();
		boolean authenticated = false;
		if (user == null) {
    		// TODO: put this information in the "flash" to inform the user what to do 
    		Logger.info("Account cannot be created for email: {}, token: {}", email, emailToken);
    		render("Users/signup.html");
		} else if (!password.equals(passwordConfirmation)) {
    		// TODO: put this information in the "flash" to inform the user what to do 
			render("Users/activate.html", user);
		} else if (!EmailValidator.valid(email)) {
    		// TODO: put this information in the "flash" to inform the user what to do 
			render("Users/activate.html", user);
		} else if (firstName.isEmpty() || lastName.isEmpty()) {
    		// TODO: put this information in the "flash" to inform the user what to do 
			render("Users/activate.html", user);
		} else {
			user.setPassword(password);
			user.firstName = firstName;
			user.lastName = lastName;
			user.activated = true;
			user.save();
			authenticated = Security.authenticate(email, password);
		}
		
		if (authenticated) {
			Logger.info("Account successfully activated!", user);
			//render("/", user);
			Home.index(user);
		} else {
    		// TODO: put this information in the "flash" to inform the user what to do 
			render("Users/activate.html", user);
		}
	}
}
