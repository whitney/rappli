package controllers;

import play.*;
import play.mvc.*;
import play.mvc.With;

import java.util.*;

import models.User;

public class Application extends Controller {

    public static void index() {
        render();
    }
    
    @Before
    public static void setUser(){
        renderArgs.put("user", currentUser());
    }

    public static User currentUser() {
        if (session.contains("userId")) {
            Long userId = new Long(session.get("userId"));
            return (User) User.findById(userId);
        }
        return null;
    }
}	