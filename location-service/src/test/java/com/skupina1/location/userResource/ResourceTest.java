package  com.skupina1.location.userResource;

import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ResourceTest {

    private static Client client;
    private static String BASE_URL = "http://localhost:8080/location"; // your deployed app base URL

    @BeforeAll
    public static void init() {
        client = ClientBuilder.newClient();
    }

    @AfterAll
    public static void cleanup() {
        client.close();
    }

    @Test
    public void testGetUsers() {
        WebTarget target = client.target(BASE_URL + "/locations");
        Response response = target.request(MediaType.APPLICATION_JSON).get();

        assertEquals(200, response.getStatus());

        // Parse the response (JSON) as string or use a JSON library
        String json = response.readEntity(String.class);
        assertTrue(json.contains("Alice"));
    }
}
