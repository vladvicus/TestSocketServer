package com.epam.test;

import com.epam.bean.Book;
import com.epam.util.XmlBuilder;
import io.restassured.RestAssured;
import io.restassured.http.Header;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.hamcrest.core.StringContains.containsString;


public class ServerSocketTest {
    private final String BASEURI = "http://localhost:8085";
    Book book;
    String productPath;


    @BeforeMethod
    public void setUp() {

        RestAssured.baseURI = BASEURI;
        productPath = "/book";


    }

    @DataProvider(name = "invalid id")
    public Object[][] invalidDataForFourHundred() throws Exception {
        return new Object[][]{
                {"/bum"},
                {"/5&#423"},
                {"/®"}

        };
    }

    @DataProvider(name = "valid id")
    public Object[][] validDataForCreateProducts() throws Exception {
        return new Object[][]{
                {"999"},
                {"-1"},
                {"500000000"}
        };
    }

    @Test(dependsOnMethods = {"testBookCreationWithDataFromXML"})
    public void deleteValidBook() {

        String response = given().header(new Header("Accept", "application/xml")).
                pathParam("id", 7).
                when().delete(productPath + "/{id}").
                then().
                body(containsString("Delete")).

                statusCode(200).
                extract().body().asString();
        System.out.println(response);
    }


    @Test
    public void testBookCreationWithDataFromXML() {

        book = new Book(7, "erhgrh", "drhdrh", "srgrdgdr", "drgrerg");

        Response r = given().header(new Header("Accept", "application/xml")).

                body(XmlBuilder.xmlBuilderForPutCreate(book)).
                when().
                post(productPath);
        Assert.assertEquals(r.getStatusCode(), 201);
    }

    @Test
    public void testUpdateBookWithDataFromXML() {

        Response r = given().header(new Header("Accept", "application/xml")).

                body(XmlBuilder.xmlBuilderLangForChange("Java", 3)).
                when().
                put(productPath);

        Assert.assertEquals(r.getStatusCode(), 201);
    }


    @Test
    public void testBookCreationWithJson() {
        book = new Book(5, "Java", "01-2007", "Rowling", "2007");
        Response resp = given().header(new Header("Accept", "application/json")).

                body(book).
                when().post(productPath);//.as(Book.class);
        Assert.assertEquals(resp.getStatusCode(), 201);
    }

    @Test(dependsOnMethods = "testBookCreationWithJson")
    public void deleteFromBooks() {
//passed
        String response = given().header(new Header("Accept", "application/json")).
                pathParam("id", 5).
                when().delete(productPath + "/{id}").
                then().
                body(containsString("Delete")).

                statusCode(200).
                extract().body().asString();
        System.out.println(response);
    }

    @Test
    public void testBookCreationWithPost() {
        Map<String, String> testBook = new HashMap<>();
        testBook.put("id", "55");
        testBook.put("language", "Eng");
        testBook.put("author", "Tolkien");
        Response resp = given().header(new Header("Accept", "application/json"))
                .body(testBook).

                        when().post("/book").andReturn();

        Assert.assertEquals(resp.getStatusCode(), 201);
    }


    @Test
    public void positiveTestUsingGetForValidUriWithTwoHundred() {
        String resp = given().header(new Header("Accept", "application/json")).
                pathParam("id", 1).
                when().get(productPath + "/{id}").
                // andReturn().

                        then().statusCode(200).
                        extract().body().toString();

        System.out.println(resp);
    }

    @Test
    public void deleteBookByNonValidIdExpectingFourHundred() {
//passed
        String response = given().header(new Header("Accept", "application/xml")).
                pathParam("id", 7).
                when().delete(productPath + "/{id}").
                then().
                body(containsString("No such")).

                statusCode(400).
                extract().body().asString();
        System.out.println(response);
    }


    @Test(dataProvider = "invalid id")
    public void negativeTestUsingGetForInvalidUriWithFourHundred(String path) {
        String resp = given().header(new Header("Accept", "application/json")).//pathParam("id", 7).
                when().get(productPath + path).//"/{id}").
                // andReturn().

                        then().statusCode(400).
                        extract().body().toString();
        // Assert.assertEquals(resp.getStatusCode(),400);
        System.out.println(resp);
    }

    @Test(dataProvider = "valid id")
    public void testBookCreationWithPost(String id) {
        Map<String, String> testBook = new HashMap<>();
        testBook.put("id", id);
        testBook.put("language", "Eng");
        testBook.put("author", "Tolkien");
        Response resp = given().
                header(new Header("Accept", "application/json")).
                body(testBook).
                when().post("/book").andReturn();

        Assert.assertEquals(resp.getStatusCode(), 201);
    }






}
