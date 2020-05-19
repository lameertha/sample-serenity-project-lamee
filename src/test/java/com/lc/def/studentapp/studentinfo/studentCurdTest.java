package com.lc.def.studentapp.studentinfo;

import com.lc.def.studentapp.model.StudentPojo;
import com.lc.def.studentapp.testbase.TestBase;
import com.lc.def.studentapp.utils.TestUtils;
import io.restassured.http.ContentType;
import net.serenitybdd.junit.runners.SerenityRunner;
import net.serenitybdd.rest.SerenityRest;
import net.thucydides.core.annotations.Title;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static org.hamcrest.Matchers.hasValue;
import static org.junit.Assert.assertThat;

/* Created
 * by Lamee */
@RunWith(SerenityRunner.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class studentCurdTest extends TestBase {
    static String firstName = "Lamee" + TestUtils.getRandomValue();
    static String lastName = "Thev" + TestUtils.getRandomValue();
    static String programme = "Electrical & Electronic Engineering";
    static String email ="lamee" +TestUtils.getRandomValue() + "@gmail.com";
    static int studentId;


    @Title("This test will create a new student")
    @Test
    public void test001() {
        List<String> courses = new ArrayList<>();
        courses.add("Tele communication");
        courses.add("Digital & Signal");
        courses.add("Mobile communication");

        StudentPojo studentPojo = new StudentPojo();
        studentPojo.setFirstName(firstName);
        studentPojo.setLastName(lastName);
        studentPojo.setEmail(email);
        studentPojo.setProgramme(programme);
        studentPojo.setCourses(courses);

        SerenityRest.rest()
                .given()
                .contentType(ContentType.JSON)
                .log().all()
                .when()
                .body(studentPojo)
                .post()
                .then().log().all().statusCode(201);
    }

    @Title("Verify if the student was added to the application")
    @Test
    public void test002() {
        String p1 = "findAll{it.firstName=='";
        String p2 = "'}.get(0)";

        HashMap<String, Object> value = SerenityRest.rest().given()
                .when()
                .get("/list")
                .then()
                .statusCode(200)
                .extract()
                .path(p1 + firstName + p2);
        assertThat(value, hasValue(firstName));
        studentId = (int) value.get("id");

    }

    @Title("Update the user information and verify the updated information")
    @Test
    public void test03() {
        String p1 = "findAll{it.firstName=='";
        String p2 = "'}.get(0)";

        firstName = firstName +"_Updated";

        List<String> courses = new ArrayList<>();
        courses.add("Tele communication");
        courses.add("Digital & Signal");
        courses.add("Mobile communication");

        StudentPojo studentPojo = new StudentPojo();
        studentPojo.setFirstName(firstName);
        studentPojo.setLastName(lastName);
        studentPojo.setEmail(email);
        studentPojo.setProgramme(programme);
        studentPojo.setCourses(courses);

        SerenityRest.rest()
                .given()
                .contentType(ContentType.JSON)
                .log().all()
                .when()
                .body(studentPojo)
                .put("/"+studentId)
                .then().log().all().statusCode(200);

        HashMap<String, Object> value = SerenityRest.rest().given()
                .when()
                .get("/list")
                .then()
                .statusCode(200)
                .extract()
                .path(p1 + firstName + p2);
        System.out.println(value);
        assertThat(value, hasValue(firstName));


    }

    @Title("Delete the student and verify if the student is deleted!")
    @Test
    public void test04() {
        SerenityRest.rest()
                .given()
                .when()
                .delete("/"+studentId)
                .then()
                .statusCode(204);

        SerenityRest.rest()
                .given()
                .when()
                .get("/"+studentId)
                .then().statusCode(404);
    }
}
