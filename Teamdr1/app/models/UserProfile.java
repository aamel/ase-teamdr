package models;
import com.avaje.ebean.Model;
import controllers.Classes;
import play.data.validation.Constraints;
import java.util.*;
import javax.persistence.*;
import play.db.ebean.*;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.xml.transform.Result;
import com.avaje.ebean.Ebean;
import java.util.List;
import java.util.ArrayList;

/**
 * Created by Bailey on 11/13/15.
 */
@Entity
public class UserProfile extends Model {

	@Id
	@Constraints.Required
	public String username;

	public String email;
	public String pic_url;
	public String description;

	// Finds all the UserProfile records on file, sorts them by usernames
	// Return as list of UserProfile records; elsewhere can iterate through the list
	// of records and process them by calling this method
	public static List<UserProfile> findAll() {
		return UserProfile.find.orderBy("email").findList();
	}

	// Check if this user already exists
	public static boolean exists(String email) {
		return (find.where().eq("email", email).findRowCount() == 1) ? true : false;
	}

	// Return the record with this matching username
	public static UserProfile getUser(String username) {
		return find.ref(username);
	}

	public UserProfile updateProfile(String uname, String e, String d) {
		this.username = uname;
		this.email = e;
		this.description = d;
		return this;
	}

	// Pass in type of primary key, type of model; pass in class so code can figure out its fields
	private static Model.Finder<String, UserProfile> find = new Model.Finder<>(UserProfile.class);
}