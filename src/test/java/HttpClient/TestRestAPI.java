package HttpClient;

import io.restassured.path.json.JsonPath;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class TestRestAPI {

    @Test(groups = {"HttpClient"})
    public void getMethod() throws IOException {
        //Creating a client
        CloseableHttpClient client = HttpClients.createDefault();

        //Creating request
        HttpGet request = new HttpGet("http://localhost:3000/posts/");
        //Adding header
        request.addHeader("Content-Type", "application/json");
        //Execute command
        CloseableHttpResponse response = client.execute(request);

        //Asserting response
        int statusCode = response.getStatusLine().getStatusCode();
        System.out.println(statusCode);
        Assert.assertEquals(statusCode, 200);

        String responseString = EntityUtils.toString(response.getEntity(),"UTF-8");
        System.out.println(responseString);

        System.out.println("*************************");
        JsonPath jsonPath = new JsonPath(responseString);
        List<Integer> id = jsonPath.getList("id");
        Collections.sort(id);
        System.out.println(id);
        System.out.println("Count of ids: "+id.size());
    }

    @Test(groups = {"HttpClient"})
    public void postMethod() throws IOException {
        int random = new Random().nextInt(10_000)+1;
        String bodyPayload = "{\"id\": " + random + ", \"title\": \"json-server\", \"author\": \"typicode\"}";

        CloseableHttpClient client = HttpClients.createDefault();
        HttpPost request = new HttpPost("http://localhost:3000/posts");
        request.addHeader("Content-Type", "application/json");
        request.setEntity(new StringEntity(bodyPayload));

        CloseableHttpResponse response = client.execute(request);
        //Asserting response
        int statusCode = response.getStatusLine().getStatusCode();
        System.out.println(statusCode);
        Assert.assertEquals(statusCode, 201);

        String responseString = EntityUtils.toString(response.getEntity(),"UTF-8");
        System.out.println(responseString);
        JsonPath jsonPath = new JsonPath(responseString);
        int id = jsonPath.get("id");
        System.out.println(id);

    }
}
