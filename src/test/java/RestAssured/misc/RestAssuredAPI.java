package RestAssured.misc;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.Assert;

import java.util.concurrent.TimeUnit;

public class RestAssuredAPI {
    public static void main(String[] args) {
        Response response = RestAssured
                .get("http://history.openweathermap.org/data/2.5/history/city?id=2885679&type=hour&appid=64ea1b840abff3d19b907aa1f354d047");
        int statusCode = response.getStatusCode();
        Assert.assertEquals(200, statusCode);

        System.out.println(response.asString());
        System.out.println(response.getTimeIn(TimeUnit.MILLISECONDS));
    }
}
