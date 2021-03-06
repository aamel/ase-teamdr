import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.JsonNode;
import org.junit.*;
// import javaGuide.tests.controllers.Application;
import play.mvc.*;
import play.test.*;
import play.data.DynamicForm;
import play.data.validation.ValidationError;
import play.data.validation.Constraints.RequiredValidator;
import play.i18n.Lang;
import play.libs.F;
import play.libs.F.*;
import play.twirl.api.Content;

import controllers.Team;
import models.TeamRecord;

import static play.test.Helpers.*;
import static org.junit.Assert.*;

import play.mvc.Http.RequestBuilder;

import play.db.Database;
import play.db.Databases;
import play.db.evolutions.*;
import java.sql.Connection;

import play.libs.ws.*;
import play.libs.F.*;

import controllers.Classes;
import models.ClassRecord;


import com.avaje.ebean.Ebean;


// import com.google.common.collect.ImmutableMap;
/**
*
* Simple (JUnit) tests that can call all parts of a play app.
* If you are interested in mocking a whole application, see the wiki for more details.
*
*/
public class ClassTest extends WithApplication{

   //  @Test
   //  public void testcreateNewClass() {
   //    ClassRecord.createNewClass("classID", "className");

   //    assertTrue(ClassRecord.exists("classID"));

   //    ClassRecord.deleteClass("classID");

   //  }

   //  @Test
   //  public void testgetNumClasses() {

   //    ClassRecord.createNewClass("classID", "className");

   //    int classSize = ClassRecord.getNumClasses();
   //    assertTrue(classSize >0);


   //    ClassRecord.deleteClass("classID");

   //  }

   //  @Test
   //  public void testClassFindList() {

   //    ClassRecord.createNewClass("classID", "className");
   //    ClassRecord.createNewClass("classID2", "className2");


   //    List<ClassRecord> class_list = ClassRecord.findAll();

   //    // System.out.println(acc_list.getClass().getName());

   //    assertEquals("com.avaje.ebean.common.BeanList", class_list.getClass().getName());

   //    ClassRecord.deleteClass("classID");
   //    ClassRecord.deleteClass("classID2");

   //  }


  	// //test Account Routes

  	// @Test
   //  public void testAddClassRoute() {
   //      RequestBuilder request = new RequestBuilder()
   //          .method(POST)
   //          .uri("/addClass");

   //      Result result = route(request);
   //      assertEquals(303, result.status());
   //  }

   //  @Test
   //  public void testClassRoute() {
   //      RequestBuilder request = new RequestBuilder()
   //          .method(GET)
   //          .uri("/class");

   //      Result result = route(request);
   //      assertEquals(200, result.status());
   //  }
    
   //  @Test
   //  public void testSetClassRoute() {
   //      RequestBuilder request = new RequestBuilder()
   //          .method(GET)
   //          .uri("/setCurrentClass");

   //      Result result = route(request);
   //      assertEquals(400, result.status());
   //  }

   //  @Test
   //  public void testLeaveClassRoute() {
   //      RequestBuilder request = new RequestBuilder()
   //          .method(GET)
   //          .uri("/leaveClass");

   //      Result result = route(request);
   //      assertEquals(400, result.status());
   //  }


}
