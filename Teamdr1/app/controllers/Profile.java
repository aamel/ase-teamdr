package controllers;

import com.fasterxml.jackson.databind.JsonNode;
import models.*;
import play.data.Form;
import play.mvc.Controller;
import play.mvc.Result;
import views.html.profile;
import views.html.update_profile;

import java.util.List;
import java.util.Map;
import java.util.ArrayList;

import static play.libs.Json.toJson;

/**
 * Created by bluemelodia on 11/11/15.
 */
public class Profile extends Controller {
	
	private static final Form<UserProfile> ProfileForm = Form.form(UserProfile.class);

    public Result viewProfile() {
        String user = session("connected");
        return ok(profile.render(UserProfile.getUser(user), UserAccount.getUser(user), Notification.getNotifs(user)));
    }

    public Result showUpdateProfilePage() {
        String user = session("connected");
        UserProfile profile = UserProfile.getUser(user);
        return ok(update_profile.render(profile));
    }

    public Result updateProfile() {
        String user = session("connected");
		System.out.println("IN UPDATE PROFILE");
        if (user == null) { // unauthorized user login, kick them back to login screen
            return redirect(routes.Account.signIn());
        }
		
		UserProfile p = UserProfile.getUser(user);
        final Map<String, String[]> values = request().body().asFormUrlEncoded();
        String email = null;
        String pictureURL = null;
        String description = null;
        System.out.println(p.email + " " + p.pic_url + " " + p.description);
        System.out.println("Values: " + values);
        if (values == null) {
            System.out.println("values is null");
            email = p.email;
            pictureURL = p.pic_url;
            description = p.description;
        }
        System.out.println("OLD: " + values.get("email")[0] + (values.get("email")).toString().replace(" ", "").length());
        if (values.get("email") == null || (values.get("email")).toString().trim().length() < 1) {
            System.out.println("old email");
            email = p.email;
        }
        else{
            System.out.println("new email");
            email = values.get("email")[0];
        }

        if (values.get("pictureURL") == null || (values.get("pictureURL")).toString().trim().length() < 1) {
            pictureURL = p.pic_url;
        }
        else{
            pictureURL = values.get("pictureURL")[0];
        }

        if (values.get("description") == null || (values.get("description")).toString().trim().length() < 1) {
            description = p.email;
        }
        else{
            description = values.get("description")[0];
        }
        System.out.println("Email: " + p.email + " url: " + p.pic_url + " description: " + p.description);
		p.email = email;
		p.pic_url = pictureURL;
		p.description = description;
		System.out.println("Also here");
		p.save();

        UserAccount getUser = UserAccount.getUser(user);

        ArrayList<ClassRecord> classsRecord = new ArrayList<ClassRecord>();
        String userClasses = UserAccount.allClasses(user);
        if (userClasses.length() > 1) {
            System.out.println("here");
            String[] userClassArray = userClasses.split("");
            System.out.println("here");
            for (int i = 0; i < userClassArray.length; i++) {
                ClassRecord thisClass = ClassRecord.getClass(userClassArray[i].trim());
                classsRecord.add(thisClass);
            }
        } else {
            System.out.println("else");
        }
        System.out.println(classsRecord.size());
        for (int i = 0; i < classsRecord.size(); i++) {
            System.out.println("heretoo");
            ClassRecord currentClass = classsRecord.get(i);
            System.out.println(currentClass);
            System.out.println("gotten");
            System.out.println("Class array: " + currentClass.className + " " + currentClass.classID);
        }

        return ok(profile.render(UserProfile.getUser(user), UserAccount.getUser(user), Notification.getNotifs(user)));
    }

    public Result viewNotifications() {
        String user = session("connected");
        if (user == null) { // unauthorized user login, kick them back to login screen
            return redirect(routes.Account.signIn());
        }
        UserAccount thisUser = UserAccount.getUser(user);

        List<Notification> allNotifs = Notification.getNotifs(thisUser.username);
        String notifsJson = toJson(allNotifs).toString();
        return ok(views.html.notifications.render(notifsJson));
    }

    public Result showNotifications() {
        String user = session("connected");
        if (user == null) { // unauthorized user login, kick them back to login screen
            return redirect(routes.Account.signIn());
        }
        return TODO;
    }

    // This one only called in type 1 call
    public Result acceptNotification() {
        JsonNode json = request().body().asJson();
        int notificationID = json.asInt();
        System.out.println(notificationID);

        if (!Notification.notifExists(notificationID)) {
            return ok(toJson("Accepted"));
        }

        // Find that notification, getting the classID and teamID of the requester
        Notification thisNotif = Notification.getThisNotif(notificationID);
        String classID = thisNotif.classID;
        String teamID = thisNotif.teamID;
        if (!TeamRecord.exists(teamID)) { // someone already swiped right, or team was otherwise purged
            Notification.deleteNotif(notificationID);
            return ok(toJson("Accepted"));
        }

        // Get the requester's team members
        TeamRecord requesterTeam = TeamRecord.getTeam(teamID);
        String[] theirMembers = (requesterTeam.teamMembers).split(" ");
        ArrayList<String> theirTeam = new ArrayList<String>();
        for (int j = 0; j < theirMembers.length; j++) {
            theirTeam.add(theirMembers[j].trim());
        }

        // Get yourself
        String user = session("connected");

        // Do the merge only if you aren't already on the same team as the requester
        TeamRecord myTeam = TeamRecord.getTeamForClass(user, classID);
        Boolean sameTeam = false;
        String[] teamMembers = (myTeam.teamMembers).split(" ");
        for (int i = 0; i < teamMembers.length; i++) {
            String currentMember = teamMembers[i].trim();
            if (theirTeam.contains(currentMember)) {
                sameTeam = true; // The team was already merged, do not try to merge again!
            }
        }

        if (!sameTeam) { // can do the merge
            System.out.println("gonna merge");
            requesterTeam = requesterTeam.updateTeam(requesterTeam.tid, myTeam.tid, classID);
            requesterTeam.save();
            //td = td.updateTeam(thisTeam, user);
            //System.out.println("new team " + td.teamMembers);
            //td.save();
        }

        // Delete the notification
        Notification.deleteNotif(notificationID);

        return ok(toJson("Accepted"));
    }

    // Just delete the record
    public Result rejectNotification() {
        JsonNode json = request().body().asJson();
        System.out.println("REJECT");
        int notificationID = json.asInt();
        if (!Notification.notifExists(notificationID)) {
            return ok(toJson("Rejected"));
        }
        System.out.println("Deleting this notif: " + notificationID);
        // Delete this notification
        Notification.deleteNotif(notificationID);

        return ok(toJson("Rejected"));
    }
	
	public Result addClass() {
		String user = session("connected");
        if (user == null) { // unauthorized user login, kick them back to login screen
            return redirect(routes.Account.signIn());
        }
        UserAccount getUser = UserAccount.getUser(user);
		UserProfile p = UserProfile.getUser(user);

        boolean foundClass = false;
		final Map<String, String[]> values = request().body().asFormUrlEncoded();
        String classID = values.get("classID")[0];
        System.out.println("ClassID to find: " + classID);
        String className = null;

        List<ClassRecord> cs = ClassRecord.findAll();
        for (int i = 0; i < cs.size(); i++) {
            System.out.println("DATABASE CLASS: " + cs.get(i).classID + " : " + cs.get(i).className);
        }

        ClassRecord c = null;
        int i;
        for(i=0; i<cs.size(); i++){
            c =  cs.get(i);
            if(c.classID.equals(classID)) {
                className = c.className;
                foundClass = true;
                break;
            }
        }

        ArrayList<ClassRecord> classes = new ArrayList<ClassRecord>();
		//String className = values.get("className")[0];
		if (foundClass) {
            UserAccount.addClass(user, classID);
            System.out.println("ADDED CLASS: " + classID + " CLASS NAME: " + className);
            String userClasses = UserAccount.allClasses(user);
            String[] userClassArray = userClasses.split("\\|");
            for (int j = 0; j < userClassArray.length; j++) {
                ClassRecord thisClass = ClassRecord.getClass(userClassArray[j].trim());
                classes.add(thisClass);
            }
        }
        return ok(profile.render(UserProfile.getUser(user), UserAccount.getUser(user), Notification.getNotifs(user)));
    }
}