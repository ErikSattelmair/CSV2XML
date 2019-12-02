package de.erik.tools.csv2xml.service;

public interface CSVValidationService {

    boolean isCSVvalid(final byte[] csv, final String delimiter);

}
