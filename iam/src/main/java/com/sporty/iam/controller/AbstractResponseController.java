package com.sporty.iam.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * Created by Tigran Melkonyan
 * Date: 3/20/25
 * Time: 11:42 AM
 */
public abstract class AbstractResponseController {

    protected <T> ResponseEntity<T> respondOK(final T object) {
        return respond(object);
    }

    protected <T> ResponseEntity<T> respondEmpty() {
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    private <T> ResponseEntity<T> respond(final T object) {
        return object == null ? respondEmpty() : new ResponseEntity<>(object, HttpStatus.OK);
    }
}
