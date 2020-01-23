package de.erik.tools.csv2xml.controller;

import de.erik.tools.csv2xml.service.CSV2XMLConverterService;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.xml.parsers.ParserConfigurationException;

@Path("/csv")
public class RestAPI {

    @Inject
    private CSV2XMLConverterService csv2XMLConverterService;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("/converter/xml")
    public byte[] convertCSVtoXML(final byte[] csvContent, final String delimiter, final boolean header,
                                  final String rootElementName, final String rowElementName) throws ParserConfigurationException {
        return this.csv2XMLConverterService.convertCSV2XML(csvContent, delimiter, header, rootElementName, rowElementName);
    }

}
