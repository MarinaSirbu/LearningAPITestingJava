package RestAssured;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.json.JSONObject;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Random;

import static io.restassured.RestAssured.given;

public class TestDemoRestAPI {

    int lastId;

    public int setLastId(int lastId){
        return lastId;
    }

    @BeforeMethod(groups = {"RestAssured"})
    public void beforeMethodAnnotation(){
        System.out.println("*****************\nRunning @BeforeMethod\n*****************");
    }

    @AfterMethod(groups = {"RestAssured"})
    public void afterMethodAnnotation(){
        System.out.println("*****************\nRunning @AfterMethod\n*****************");
    }

    /**
     * Given -> headers(), parameters(), cookies(), body(), ContentType(), relexHTTPSValidation()
     * When -> get(), post(), put(), delete()
     * Then -> assertThat(), statusCode(), log().all()
     */

    @Test(priority = 6, groups = {"RestAssured"})
    public void getAllMethod(){
        RestAssured.baseURI="http://localhost:3000";

        Response response = given().contentType("application/json")
                .when().get("/posts/")
                .then()
                .log().all()
                .extract().response();

        String stringResponse = response.asString();
        JsonPath jsonPath = new JsonPath(stringResponse);

        List<Integer> listId = jsonPath.getList("id");
        lastId = listId.get(listId.size()-1);

        System.out.println(lastId);
    }

    @Test(dependsOnMethods = "getAllMethod", groups = {"RestAssured"})
    public void getLastIdMethod(){
        RestAssured.baseURI="http://localhost:3000";

        Response response = given().contentType("application/json")
                .when().get("/posts/"+setLastId(lastId))
                .then()
                .log().all()
                .extract().response();

        String stringResponse = response.asString();
        JsonPath jsonPath = new JsonPath(stringResponse);
        int idNumber = jsonPath.getInt("id");

        System.out.println(idNumber);
    }

    @Test(priority = 1, groups = {"RestAssured"})
    public void postMethodRandomID(){
        RestAssured.baseURI="http://localhost:3000";

        Random random = new Random();
        int randomNumber = random.nextInt(10_000)+1;

        Response response = given()
                .header("Content-Type","application/json")
                .and()
                .body("{\n" +
                        "  \"id\": " + randomNumber + ",\n" +
                        "  \"title\": \"json-server" + randomNumber + "\",\n" +
                        "  \"author\": \"typicode" + randomNumber + "\"\n" +
                        "}")
            .when()
                .post("/posts")
            .then()
                .statusCode(201)
                .and()
                .log().all()
                .extract().response();
        JsonPath jsonXpath = new JsonPath(response.asString());
        System.out.println("*******************************");
        System.out.println("ID is " + jsonXpath.getString("id"));
    }

    @Test(priority = 2, groups = {"RestAssured"})
    public void postMethodFromFile() throws IOException {
        String filePath = "src/jsonFileInput/jsonRequest1.json";
        String jsonContent = Files.readString(Paths.get(filePath));

        int randomNumber = new Random().nextInt(10_000)+1;

        String updatedJsonContent = jsonContent.replaceAll("\"id\"\\s*:\\s*\\d+", "\"id\": " + randomNumber);

        RestAssured.baseURI="http://localhost:3000";

        Response response = given()
                .contentType("application/json")
                .and()
                .body(updatedJsonContent)
            .when()
                .post("/posts")
            .then()
                .statusCode(201)
                .and()
                .log().all()
                .extract().response();
    }

    @Test(priority = 3, groups = {"RestAssured"})
    public void postMethodFromFolder() throws IOException {
        String folderPath = "src/jsonFileInput";
        File folder = new File(folderPath);
        File[] files = folder.listFiles((dir, name) -> name.toLowerCase().endsWith(".json"));

        for(File file: files){
            try {
                updateJsonFile(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        RestAssured.baseURI = "http://localhost:3000";

        Files.list(Paths.get(folderPath))
                .filter(Files::isRegularFile)
                .forEach(file -> {
                    try {
                        Response response = given()
                                .contentType("application/json")
                                .and()
                                .body(Files.readString(file))
                                .when()
                                .post("/posts")
                                .then()
                                .statusCode(201)
                                .and()
                                .log().all()
                                .extract().response();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });
    }

    public static void updateJsonFile(File file) throws IOException {
        String content = Files.readString(file.toPath());
        JSONObject jsonObject = new JSONObject(content);

        Random random = new Random();
        int newId = random.nextInt(10_000) + 1;

        jsonObject.put("id", newId);

        FileWriter fileWriter = new FileWriter(file);
        fileWriter.write(jsonObject.toString());
        fileWriter.close();
    }

    @Test(priority = 4, groups = {"RestAssured"})
    public void putMethod(){
        RestAssured.baseURI="http://localhost:3000";

        Response response = given()
                .header("Content-Type","application/json")
                .and()
                .body("{\n" +
                        "  \"id\": 345,\n" +
                        "  \"title\": \"json-server345-AgainUpdated\",\n" +
                        "  \"author\": \"typicode345-AgainUpdated\"\n" +
                        "}")
                .when()
                .put("/posts/345")
                .then()
                .statusCode(200)
                .and()
                .log().all()
                .extract().response();
        JsonPath jsonXpath = new JsonPath(response.asString());
        System.out.println("*******************************");
        System.out.println("ID is " + jsonXpath.getString("id"));
        System.out.println("Title is " + jsonXpath.getString("title"));
        System.out.println("Author is " + jsonXpath.getString("author"));
    }

    @Test(dependsOnMethods = "getLastIdMethod", groups = {"RestAssured"})
    public void deleteMethod(){
        RestAssured.baseURI="http://localhost:3000";

        given()
                .header("Content-Type","application/json")
            .when()
                .delete("/posts/"+setLastId(lastId))
            .then()
                .statusCode(200)
                .and()
                .log().all();
    }

    @Test(priority = 5, groups = {"RestAssured"})
    public void postMethodRandomIDAndDelete(){
        RestAssured.baseURI="http://localhost:3000";

        Random random = new Random();
        int randomNumber = random.nextInt(10_000)+1;

        Response response = given()
                .header("Content-Type","application/json")
                .and()
                .body("{\n" +
                        "  \"id\": " + randomNumber + ",\n" +
                        "  \"title\": \"json-server" + randomNumber + "\",\n" +
                        "  \"author\": \"typicode" + randomNumber + "\"\n" +
                        "}")
                .when()
                .post("/posts")
                .then()
                .statusCode(201)
                .and()
                .log().all()
                .extract().response();

        System.out.println("*******************************");


        given()
                .header("Content-Type","application/json")
                .when()
                .delete("/posts/"+randomNumber)
                .then()
                .statusCode(200)
                .and()
                .log().all();
    }

}
