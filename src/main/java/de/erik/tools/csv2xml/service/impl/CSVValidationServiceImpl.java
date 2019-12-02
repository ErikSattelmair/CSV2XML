package de.erik.tools.csv2xml.service.impl;

import de.erik.tools.csv2xml.service.CSVValidationService;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;
import java.util.Arrays;

@Named("csvValidator")
@ApplicationScoped
public class CSVValidationServiceImpl implements CSVValidationService {

    @Override
    public boolean isCSVvalid(final byte[] csv, final String delimiter) {
        if(csv == null || csv.length == 0) {
            return false;
        }

        final String csvContent = new String(csv);
        final String[] rows = csvContent.split("\n");

        if(rows.length == 0) {
            return false;
        }

        final int numberOfDelimiters = rows[0].split(delimiter).length;
        return Arrays.stream(rows).parallel().noneMatch(row -> row.split(delimiter).length != numberOfDelimiters);
    }

}
