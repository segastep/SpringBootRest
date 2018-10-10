package com.restapi.demo.controllers;


import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
/**
 * @author G.Nikolov on 10/10/18
 * @project rest-service-basic
 *
 * Content to be rendered on index page
 *
 * A servlet can be used to render a real HTML page but...
 * focusing on other functionality plus front end wasn't really part of the spec
 */

@RestController
@RequestMapping("/")
public class IndexController {

    /**
     *
     *  Index for localhost
     */

    @RequestMapping(method= RequestMethod.GET)
    String index()
    {
        String ret = "\"http://localhost:9000/swagger-ui.html#/\" - API's swagger UI page\n"
                + "\n\"http://localhost:9000/h2-console/\" - H2 console - username: test, no password\n"
                + "\n\"http://localhost:9000/api/v1/\" - Api V1";
        return ret;
    }
}
