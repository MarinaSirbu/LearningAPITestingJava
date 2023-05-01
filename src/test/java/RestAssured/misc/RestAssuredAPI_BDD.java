package RestAssured.misc;

import static io.restassured.RestAssured.given;

public class RestAssuredAPI_BDD {
    public static void main(String[] args) {

        given()
                .get("http://history.openweathermap.org/data/2.5/history/city?id=2885679&type=hour&appid=64ea1b840abff3d19b907aa1f354d047")
                .then().statusCode(200)
                .log()
                .all();
    }
}
