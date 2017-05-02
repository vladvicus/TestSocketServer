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
        //  product = new Product(50000, "sword", 5.5);
        RestAssured.baseURI = BASEURI;
        productPath = "/book";//+ product.getId();


    }

    @DataProvider(name = "invalid id")
    public Object[][] invalidDataForFourHundredFour() throws Exception {
        return new Object[][]{
                {"/bum"},
                {"/5&#423"},
                {"/®"}    //!!!! using restGate i was receive 500 error

        };
    }

    @DataProvider(name = "valid id")
    public Object[][] validDataForCreateProducts() throws Exception {
        return new Object[][]{
                {999},
                {-1},
                {500000000}
        };
    }









    @Test(dependsOnMethods={"testBookCreationWithDataFromXML"})
    public void goToProductPage() {
//passed
        String response = given().header(new Header("Accept", "application/xml")).
                pathParam("id", 7).
                when().delete(productPath + "/{id}").
                then().
                               body(containsString("Delete")).
                            //   body(containsString("book")).
                           //    body(containsString("author")).
                                      statusCode(200).
                        extract().body().asString();
        System.out.println(response);}


    @Test
    public void testBookCreationWithDataFromXML() {
        //passed
        book = new Book(7, "erhgrh", "drhdrh", "srgrdgdr", "drgrerg");

        Response r = given().header(new Header("Accept", "application/xml")).

                body(XmlBuilder.xmlBuilderForPutCreate(book)).
                when().
                post(productPath);
        Assert.assertEquals(r.getStatusCode(),201);
    }
    @Test
    public void testUpdateBookWithDataFromXML(){
     //   book = new Book(2, "Java", "", "", "9999");
        Response r = given().header(new Header("Accept", "application/xml")).

                body(XmlBuilder.xmlBuilderLangForChange("Java",3)).
                when().
                put(productPath);

        Assert.assertEquals(r.getStatusCode(),201);
    }


 @Test
    public void testBookCreationWithJson(){
    book = new Book(5, "Java", "01-2007", "Rowling", "2007");
    Response resp=given().header(new Header("Accept", "application/json")).
          //  contentType("application/json").
            body(book).
            when().post(productPath);//.as(Book.class);
    Assert.assertEquals(resp.getStatusCode(),201);
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
    System.out.println(response);}

@Test
    public void testBookCreationWithPost() {
        Map<String, String> testBook = new HashMap<>();
        testBook.put("id", "55");
        testBook.put("language", "Eng");
        testBook.put("author", "Tolkien");
        Response resp = given().header(new Header("Accept", "application/json"))
                .body(testBook).
                      //  contentType("application/json").
                when().post("/book").andReturn();

        Assert.assertEquals(resp.getStatusCode(),201);
    }


/*

    @Test(dataProvider = "invalid id")
    public void negativeTestUsingGetForInvalidUriWithFourHundredFour(String path) {
        getProductStep.getProduct(BASEURI, path).then().assertThat().statusCode(404);
    }
*/



/*
@Test(dataProvider = "valid price for change")
    public void changePriceInProductPositiveTest(Product productWithPrice) {
        createStep.createNewProductWithPut(product, baseUri, productPath);
        changeStep.changePrice(Double.toString(productWithPrice.getPrice()), baseUri, productPath);
        getProductStep.getProduct(baseUri, productPath).then().
                body("PRODUCT.PRICE", equalTo(Double.toString(productWithPrice.getPrice())));


    }

    @Test
    public void changePriceInProductNegativeTestForFiveHundred() {
        createStep.createNewProductWithPut(product, baseUri, productPath);
        changeStep.changePrice("lol", baseUri, productPath).then().statusCode(500);
        deleteStep.deleteProduct(product, baseUri, productPath);

    }

    @Test
    public void changePriceInProductNegativeTestForFourHundredThree() {
        createStep.createNewProductWithPut(product, baseUri, productPath);
        changeStep.changePrice("®", baseUri, productPath).then().statusCode(403);
        deleteStep.deleteProduct(product, baseUri, productPath);
    }

    @Test
    public void negativeTestForDoubleCreatingForFiveHundred() {
        createStep.createNewProductWithPut(product, baseUri, productPath);
        createStep.createNewProductWithPut(product, baseUri, productPath).then().statusCode(500);
        deleteStep.deleteProduct(product, baseUri, productPath);
    }

    @Test
    public void negativeTestForDoubleDeletingForFourHundredFour() {
        createStep.createNewProductWithPut(product, baseUri, productPath);
        deleteStep.deleteProduct(product, baseUri, productPath);
        deleteStep.deleteProduct(product, baseUri, productPath).then().statusCode(404);
    }


    @Test(dataProvider = "valid name for change")
    public void changeNameInProductTest(Product productWithName) {
        createStep.createNewProductWithPut(product, baseUri, productPath);
        changeStep.changeName(productWithName.getName(), baseUri, productPath);
        getProductStep.getProduct(baseUri, productPath).
                then().
                body("PRODUCT.NAME", equalTo(productWithName.getName()));

    }

    @Test(dataProvider = "valid id")
    public void deleteProductTest(int id) {
        product.setId(id);
        productPath = "/" + product.getId();
        createStep.createNewProductWithPut(product, baseUri, productPath);
        deleteStep.deleteProduct(product, baseUri, productPath).then().body("resource.deleted.text()", equalTo(Integer.toString(product.getId())));
    }


    @Test(dataProvider = "valid id")
    public void createNewProductWithPutTest(int id) {
        product.setId(id);
        productPath = "/" + product.getId();
        deleteStep.deleteProduct(product, baseUri, productPath);
        createStep.createNewProductWithPut(product, baseUri, productPath).then().
                statusCode(201);
        deleteStep.deleteProduct(product, baseUri, productPath);
    }


    //this test does not work and will be nice if somebody tell me why because using restGate it works
    @Ignore
    public void createProductWithPostTest() {

        //  with().params("ID",200,"NAME","sword","PRICE",5.5).when().post(baseUri).then().statusCode(201);
        Response r = given().contentType("application/xml").
                body(XmlBuilder.xmlBuilderForPostCreate(product)).
                when().
                post(baseUri);

        String body = r.getBody().asString();
        System.out.println(body);
    }

    // The element type "HR" must be terminated by the matching end-tag "</HR>".
    //Bad page-proofs maybe
    @Ignore
    public void negativeTestUsingDeleteForInvalidUri() {
        given().
                config(RestAssured.config().xmlConfig(xmlConfig().with().namespaceAware(true))).
                when().
                get(baseUri + "lol").
                then().
                body(hasXPath("//u[contains(text() ,'delete')]"));
    }

    @AfterMethod
    public void tearDown() {
        deleteStep.deleteProduct(product, baseUri, productPath);
    }
*/

}
