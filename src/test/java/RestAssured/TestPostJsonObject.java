package RestAssured;

import io.restassured.RestAssured;
import org.json.JSONObject;
import org.testng.annotations.Test;

import java.util.Random;

import static io.restassured.RestAssured.given;

public class TestPostJsonObject {

    @Test
    public void postMethod(){
        JSONObject json = new JSONObject();
        int randomId = new Random().nextInt(10_000)+1;
        json.put("id", randomId);
        json.put("title", "myTitle"+randomId);
        json.put("author", "author"+randomId);

        RestAssured.baseURI="http://localhost:3000";

        given()
                .contentType("application/json")
                .and()
                .body(json.toString())
            .when()
                .post("/posts")
            .then()
                .statusCode(201)
                .and()
                .log().all();

    }
}
