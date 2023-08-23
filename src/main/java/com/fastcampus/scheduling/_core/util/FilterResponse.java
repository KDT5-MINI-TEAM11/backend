package com.fastcampus.scheduling._core.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fastcampus.scheduling._core.errors.exception.Exception401;
import com.fastcampus.scheduling._core.errors.exception.Exception403;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;

public class FilterResponse {

    private FilterResponse() {
        throw new IllegalArgumentException("Suppress default constructor");
    }

    private static final ObjectMapper OBJECT_MAPPER;

    static {
        OBJECT_MAPPER = new ObjectMapper();
    }

    public static void unAuthorized(HttpServletResponse response, Exception401 exception) throws IOException {
        response.setStatus(exception.status().value());
        response.setContentType("application/json; charset=utf-8");

        String responseBody = OBJECT_MAPPER.writeValueAsString(exception.body());
        response.getWriter().println(responseBody);
    }

    public static void forbidden(HttpServletResponse response, Exception403 exception) throws IOException {
        response.setStatus(exception.status().value());
        response.setContentType("application/json; charset=utf-8");

        String responseBody = OBJECT_MAPPER.writeValueAsString(exception.body());
        response.getWriter().println(responseBody);
    }
}
