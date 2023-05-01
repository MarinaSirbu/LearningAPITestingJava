package HttpClient;

import io.restassured.RestAssured;
import io.restassured.path.xml.XmlPath;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class TestSoapAPI {

    @Test(groups = {"HttpClient"})
    public void getMethod() throws IOException {
        //https://www.dataaccess.com/webservicesserver/NumberConversion.wso

        String filePath = "src/soapRequest/request1.xml";

        RestAssured.baseURI="https://www.dataaccess.com";

        CloseableHttpClient client = HttpClients.createDefault();
        HttpPost request = new HttpPost("https://www.dataaccess.com/webservicesserver/NumberConversion.wso");
        request.addHeader("Content-Type", "text/xml");
        request.setEntity(new StringEntity(Files.readString(Paths.get(filePath))));

        CloseableHttpResponse response = client.execute(request);

        int statusCode = response.getStatusLine().getStatusCode();
        Assert.assertEquals(statusCode,200);
        System.out.println("Status code: " + statusCode);

        String responseString = EntityUtils.toString(response.getEntity());
        System.out.println(responseString);

        XmlPath xmlPath = new XmlPath(responseString);
        String wordResult = xmlPath.getString("NumberToWordsResult");
        System.out.println("NumberToWordsResult: " + wordResult);


    }

}
