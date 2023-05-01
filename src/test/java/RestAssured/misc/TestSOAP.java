package RestAssured.misc;

import io.restassured.RestAssured;
import io.restassured.path.xml.XmlPath;
import io.restassured.response.Response;
import org.testng.annotations.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static io.restassured.RestAssured.given;

public class TestSOAP {

    @Test
    public void getMethod() throws IOException {
        String filePath = "src/soapRequest/request1.xml";

        RestAssured.baseURI="https://www.dataaccess.com";

        Response response = given()
                .header("Content-Type", "text/xml")
                .and()
                .body(Files.readString(Paths.get(filePath)))
            .when()
                .post("/webservicesserver/NumberConversion.wso")
            .then()
                .statusCode(200)
                .and()
                .log().all().extract().response();

        XmlPath xmlPath = new XmlPath(response.asString());
        String word = xmlPath.getString("NumberToWordsResult");
        System.out.println("************************");
        System.out.println("NumberToWordsResult is " + word);
    }
}
