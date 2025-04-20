package com.sporty.bookstore.domain.model.common.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sporty.bookstore.controller.model.response.common.ErrorResponse;
import feign.Response;
import feign.codec.ErrorDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Optional;

/**
 * Created by Tigran Melkonyan
 * Date: 4/20/25
 * Time: 3:58 PM
 */
@Component
public class FeignErrorDecoder implements ErrorDecoder {

    private static final Logger LOGGER = LoggerFactory.getLogger(FeignErrorDecoder.class);

    private final ObjectMapper objectMapper;
    private final ErrorDecoder errorDecoder = new Default();

    public FeignErrorDecoder(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public Exception decode(String s, Response response) {

        ErrorResponse errorResponse = null;
        try {
            errorResponse = objectMapper.readValue(response.body().asInputStream(), ErrorResponse.class);
        } catch (Exception ex) {
            LOGGER.debug("Exception happened when reading error response", ex);
        }

        Optional<ErrorResponse> optionalResponse = Optional.ofNullable(errorResponse);
        if (optionalResponse.isPresent() && optionalResponse.get().errorCode() != null) {
            return switch (errorResponse.errorCode()) {
                case ILLEGAL_ARGUMENT_EXCEPTION -> new IllegalArgumentException(errorResponse.message());
                default -> new RecordConflictException(errorResponse.message(), errorResponse.errorCode());
            };
        }
        return errorDecoder.decode(s, response);
    }
}
