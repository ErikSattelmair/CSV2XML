package de.erik.tools.csv2xml;

import javax.ws.rs.core.Response;

public class ResponseUtils {

    private ResponseUtils() {}

    public static Response createResponse(final byte[] payload, final Response.Status status) {
        return Response.status(status).entity(payload).build();
    }

}
