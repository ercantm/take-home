package fun;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.networknt.schema.JsonSchema;
import com.networknt.schema.JsonSchemaFactory;
import com.networknt.schema.SpecVersion;
import com.networknt.schema.ValidationMessage;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


import javax.validation.ValidationException;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Set;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class GameTests {

    private static final String BASE_URL = "http://localhost:8080/game";

    @DisplayName("Test with parameter")
    @Test
    public void testWithQueryParam() {
        String param = "Chess";
        given()
            .param("name", param)
        .when()
            .get(BASE_URL)
        .then()
            .statusCode(200)
            .body("id", greaterThan(0))
            .body("text", containsString(param));

    }

    @DisplayName("Test without parameter")
    @Test
    public void testWithoutQueryParam() {
        String defaultParam = "Sudoku";
        given()
        .when()
            .get(BASE_URL)
        .then()
            .statusCode(200)
            .body("id", greaterThan(0))
            .body("text", containsString(defaultParam));

    }


    @DisplayName("Validate JSON Schema")
    @Test
    public void validateJsonSchema() throws ValidationException, IOException, URISyntaxException {

        Response response = RestAssured.given().get(BASE_URL);

        assertEquals(200,response.getStatusCode());

        ObjectMapper objectMapper = new ObjectMapper();
        JsonSchemaFactory factory = JsonSchemaFactory.getInstance(SpecVersion.VersionFlag.V4);
        JsonSchema jsonSchema = factory.getSchema(getClass().getClassLoader().getResource("schema.json").toURI());
        JsonNode jsonNode = objectMapper.readTree(response.getBody().print());

        Set<ValidationMessage> errors = jsonSchema.validate(jsonNode);
        assertTrue(errors.isEmpty());

    }

    @DisplayName("Convert JSON to POJO")
    @Test
    public void testPojoConversion() {
        String param = "Chess";
        Response response = RestAssured.given().param("name", param).get(BASE_URL);

        assertEquals(200,response.getStatusCode());

        ServiceResponse serviceResponse = response.as(ServiceResponse.class);

        assertTrue(serviceResponse.getId()>0);
        assertTrue(serviceResponse.getText().contains(param));
        System.out.println(serviceResponse);

    }



}
