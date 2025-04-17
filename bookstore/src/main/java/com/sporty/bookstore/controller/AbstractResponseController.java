package com.sporty.bookstore.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

/**
 * Created by Tigran Melkonyan
 * Date: 4/17/25
 * Time: 3:20 PM
 */
public class AbstractResponseController {

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
