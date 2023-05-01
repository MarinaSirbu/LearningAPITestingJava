package RestAssured;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Random;

import static io.restassured.RestAssured.given;

public class TestPostAndReceiveJavaObject {
    @Test
    public void postAndReceiveMethod(){
        int randomId = new Random().nextInt(10_000)+1;
        JsonInputData jsonInputData = new JsonInputData(randomId, "Title"+randomId, "author"+randomId);
        JsonReceiveData jsonReceiveData = new JsonReceiveData();

        Gson gson = new GsonBuilder().create();


        RestAssured.baseURI="http://localhost:3000";

        Response response = given()
                .header("Content-Type", "application/json")
                .and()
                .body(jsonInputData)
            .when()
                .post("/posts")
            .then()
                .statusCode(201)
                .and()
                .extract().response();

        jsonReceiveData = gson.fromJson(response.prettyPrint(), JsonReceiveData.class);

        Assert.assertEquals(jsonReceiveData.getId(), jsonInputData.getId());
        Assert.assertEquals(jsonReceiveData.getTitle(), jsonInputData.getTitle());
        Assert.assertEquals(jsonReceiveData.getAuthor(), jsonInputData.getAuthor());
    }
}
