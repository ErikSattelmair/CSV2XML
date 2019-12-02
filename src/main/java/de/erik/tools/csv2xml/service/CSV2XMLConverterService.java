package de.erik.tools.csv2xml.service;

import javax.xml.parsers.ParserConfigurationException;

public interface CSV2XMLConverterService {

    byte[] convertCSV2XML(final byte[] csv, final String delimiter, final boolean header, final String rootElementName, final String rowElementName) throws ParserConfigurationException;

}
