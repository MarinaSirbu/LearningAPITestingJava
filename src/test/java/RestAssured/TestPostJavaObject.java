package RestAssured;

import io.restassured.RestAssured;
import org.testng.annotations.Test;

import java.util.Random;

import static io.restassured.RestAssured.given;

public class TestPostJavaObject {

    @Test
    public void postMethod(){
        int randomId = new Random().nextInt(10_000)+1;
        JsonInputData jsonInputData = new JsonInputData(randomId, "Title"+randomId, "author"+randomId);

        RestAssured.baseURI="http://localhost:3000";

        given()
                .header("Content-Type","application/json")
                .and()
                .body(jsonInputData)
            .when()
                .post("/posts")
            .then()
                .statusCode(201)
                .and()
                .log().all();
    }
}
