package models;

import java.util.*;
import javax.persistence.*;
import play.db.jpa.Model;

@Entity
@Table(name = "`users`")
public class User extends Model {
     
    public String email;
    public String first_name;
    public String last_name;
    
    public User(String email, String first_name, String last_name) {
        this.email = email;
        this.first_name = first_name;
        this.last_name = last_name;
    }

    // inverse side of the relation
    //@ManyToMany(mappedBy = "users")
    //public List<Neighborhood> neighborhoods;
}
