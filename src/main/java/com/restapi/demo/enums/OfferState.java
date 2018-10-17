package com.restapi.demo.enums;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.util.Arrays;

/**
 * @author G.Nikolov on 10/10/18
 * @project rest-service-basic
 *
 * Offer to accept only one of these states
 */
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum OfferState implements Serializable {
    ACTIVE("ACTIVE"),
    EXPIRED("EXPIRED"),
    CANCELLED("CANCELLED"),
    RENEWED("RENEWED");

    private final String state;

    OfferState(String state)
    {
        this.state = state.toUpperCase();
    }

}
