package de.erik.tools.csv2xml.controller;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/health")
public class Health {

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public Response health() {
        return Response.ok().build();
    }

}
