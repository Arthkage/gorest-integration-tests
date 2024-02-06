package org.globallogic.gorest.core;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static io.restassured.RestAssured.given;

public class ApiMain {

     public Logger logger = LoggerFactory.getLogger(ApiMain.class);

    protected RequestSpecification request;
    protected Response response;
    protected static final String HOST = "https://gorest.co.in/public/v2";
    protected String token;



    public ApiMain (String token) {
        this.token = token;
    }

    public Response getResponse() {
        return response;
    }

    public void configuration(String endpoint) {
        request = given()
                        .baseUri(HOST)
                        .basePath(endpoint)
                        .contentType(ContentType.JSON)
                        .header("Authorization", "Bearer " + token);

    }
}
