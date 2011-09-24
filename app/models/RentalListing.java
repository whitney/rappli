package models;

import java.util.*;
import javax.persistence.*;
import play.db.jpa.Model;

@Entity
@Table(name = "`rental_listings`")
public class RentalListing extends Model {
     
    public String url;
    public String html;
    
    public RentalListing(String url, String html) {
        this.url = url;
        this.html = html;
    }
}
