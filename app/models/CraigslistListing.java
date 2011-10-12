package models;

import play.*;
import play.db.jpa.*;
import play.data.validation.Required;

import javax.persistence.*;
import java.util.*;

@Entity
public class CraigslistListing extends Model {

		@Required
		public String title;
		@Required
		public String url;
		@Required
		public Long postId;
		@Required 
		public String email;
		public int price;
}
