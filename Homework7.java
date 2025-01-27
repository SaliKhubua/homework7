import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;


import java.util.UUID;

public class Homework7 {
    @DataProvider(name = "isbnData")
    public Object[][] isbnDataCheck() {
        return new Object[][]{

                {"9781449331818"},
                {"9781449337711"},
                {"9781449365035"},
                {"9781491904244"}

        };
    }

    @Test(dataProvider = "isbnData") //1.
    public void testGetBookDetails(String isbn) {
        RestAssured.baseURI = "https://bookstore.toolsqa.com";

        Response response = RestAssured
                .given()
                .queryParam("ISBN", isbn)
                .when()
                .get("/BookStore/v1/Book")
                .then()
                .extract().response();

        System.out.println("Response Status Code: " + response.statusCode());
        System.out.println("Response Body: " + response.getBody().asString());


        Assert.assertEquals(response.getStatusCode(), 200, "Status Code must be 200");

        String actualIsbn = response.jsonPath().getString("isbn");
        Assert.assertEquals(actualIsbn, isbn, "ISBN in response does not match the provided ISBN");

    }


    @Test //2.
    public void testCreateUser() {
        RestAssured.baseURI = "https://bookstore.toolsqa.com";

        String uniqueUsername = "sali" + UUID.randomUUID().toString().substring(0, 6);

        UserRequest1 userRequest = UserRequest1.builder()
                .userName(uniqueUsername)
                .password("Salome123!")
                .build();

        Response response = RestAssured.given()
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .body(userRequest)
                .when()
                .post("/Account/v1/User")
                .then()
                .extract().response();

        System.out.println("Response Code: " + response.getStatusCode());
        System.out.println("Response Body: " + response.asString());

        Assert.assertEquals(response.getStatusCode(), 201, "Expected status code 201");

        String responseBody = response.asString();
        Assert.assertTrue(responseBody.contains("userID"), "Response does not contain userID");

        String userID = response.jsonPath().getString("userID");
        System.out.println("UserID: " + userID);

    }



    @Test   //3.
    public void unaccaptablePassword () {
        RestAssured.baseURI = "https://bookstore.toolsqa.com";
        UserRequest userRequest = new UserRequest("username", "password");
        Response response = RestAssured.given()
                .header("Content-Type", "application/json")
                .body(userRequest)
                .when()
                .post("/Account/v1/User")
                .then()
                .extract().response();

        System.out.println("Response Status Code: " + response.getStatusCode());
        System.out.println("Response Body: " + response.getBody().asString());


        Assert.assertEquals(response.getStatusCode(), 400, "Expected status code: 400");

        String expectedMessage = "Passwords must have at least one non alphanumeric character, one digit ('0'-'9'), one uppercase ('A'-'Z'), one lowercase ('a'-'z'), one special character and Password must be eight characters or longer.";
        String actualMessage = response.jsonPath().getString("message");


        Assert.assertEquals(actualMessage, expectedMessage, "Expected password error message did not match.");

    }

}
