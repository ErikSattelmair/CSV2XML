package de.erik.tools.csv2xml.controller;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/csv")
public class RestAPI {

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public byte[] convertCSVtoXML(final byte[] csvContent) {
        //TODO
        return null;
    }

}
