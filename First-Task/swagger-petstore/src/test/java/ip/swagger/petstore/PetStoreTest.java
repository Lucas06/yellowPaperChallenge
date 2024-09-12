package ip.swagger.petstore;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.hamcrest.Matchers.equalTo;

public class PetStoreTest {
  @BeforeAll
  public static void setup() {
    RestAssured.baseURI = "http://localhost:8080/api/v3";
  }

  @Test
  public void testCreatePet() {
    String requestBody = "{ \"id\": 1, \"name\": \"Sky\", \"status\": \"available\" }";
    RestAssured.given()
      .header("Content-Type", "application/json")
      .body(requestBody)
      .when()
      .post("/pet")
      .then()
      .statusCode(200)
      .body("name", equalTo("Sky"));
  }

  @Test
  public void testGetPet() {
    RestAssured.given()
      .when()
      .get("/pet/1")
      .then()
      .statusCode(200)
      .body("name", equalTo("Sky"));
  }

  @Test
  public void testUpdatePet() {
    String requestBody = "{ \"id\": 1, \"name\": \"SkyUpdated\", \"status\": \"available\" }";
    RestAssured.given()
      .header("Content-Type", "application/json")
      .body(requestBody)
      .when()
      .put("/pet")
      .then()
      .statusCode(200)
      .body("name", equalTo("SkyUpdated"));
  }

  @Test
  public void testDeletePet() {
    RestAssured.given()
      .when()
      .delete("/pet/1")
      .then()
      .statusCode(200);

    // Verify pet is deleted
    RestAssured.given()
      .when()
      .get("/pet/1")
      .then()
      .statusCode(404);
  }

  @Test
  public void testGetNonExistentPet() {
    Response response = RestAssured.given()
      .when()
      .get("/pet/9999");

    response.then().statusCode(404);

    String responseBody = response.getBody().asString();
    if (responseBody.contains("Pet not found")) {
    } else {
      throw new AssertionError("Unexpected response body: " + responseBody);
    }
  }

  @Test
  public void testUpdateNonExistentPet() {
    String requestBody = "{ \"id\": 9999, \"name\": \"SkyUpdated\", \"status\": \"available\" }";

    Response response = RestAssured.given()
      .header("Content-Type", "application/json")
      .body(requestBody)
      .when()
      .put("/pet");

    response.then().statusCode(404);

    String responseBody = response.getBody().asString();
    if (responseBody.contains("Pet not found")) {
    } else {
      throw new AssertionError("Unexpected response body: " + responseBody);
    }
  }

  @Test
  public void testDeleteNonExistentPet() {
    Response response = RestAssured.given()
      .when()
      .delete("/pet/9999999999999999999");

    response.then().statusCode(400);

    String responseBody = response.getBody().asString();
    if (responseBody.contains("Input error: couldn't convert")) {
    } else {
      throw new AssertionError("Unexpected response body: " + responseBody);
    }
  }
}
