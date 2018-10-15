package com.restapi.demo;

import io.restassured.RestAssured;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = RestServiceBasicApplication.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class RestServiceBasicApplicationTests {

    @Value("${server.port}")
    int port;

    @Before
    public void setBaseUri()
    {
        RestAssured.port = port;
        RestAssured.baseURI = "http://localhost";

    }

    @Test
    public void getDataTest()
    {
        RestAssured.get();
    }

    @Test
	public void contextLoads()
    {
	}

}
