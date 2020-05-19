package com.lc.def.studentapp.testbase;

import io.restassured.RestAssured;
import org.junit.BeforeClass;

/* Created
 * by Lamee */
public class TestBase {
    @BeforeClass
    public static void inIt() {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = 8085;
        RestAssured.basePath = "/student";
    }
}
