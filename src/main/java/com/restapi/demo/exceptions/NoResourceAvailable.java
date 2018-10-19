package com.restapi.demo.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author G.Nikolov on 19/10/18
 * @project rest-service-basic
 */
@ResponseStatus(HttpStatus.OK)
public class NoResourceAvailable extends RuntimeException {
    public NoResourceAvailable(String message) {
        super(message);
    }
}
