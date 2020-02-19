package de.erik.tools.csv2xml.controller;

import de.erik.tools.csv2xml.ResponseUtils;
import de.erik.tools.csv2xml.service.CSV2XMLConverterService;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.parsers.ParserConfigurationException;

@Path("/csv")
public class RestAPI {

    @Inject
    private CSV2XMLConverterService csv2XMLConverterService;

    @POST
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_XML)
    @Path("/converter/xml")
    public Response convertCSVtoXML(final String csvContent,
                                      @QueryParam("delimiter") final String delimiter,
                                      @QueryParam("header") final boolean header,
                                      @QueryParam("rootElement") final String rootElement,
                                      @QueryParam("rowElement") final String rowElement) throws ParserConfigurationException {
        return ResponseUtils.createResponse(
                this.csv2XMLConverterService.convertCSV2XML(csvContent.getBytes(), delimiter, false, rootElement, rowElement),
                Response.Status.OK);
    }

}
