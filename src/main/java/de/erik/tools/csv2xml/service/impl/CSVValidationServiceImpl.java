package de.erik.tools.csv2xml.service.impl;

import de.erik.tools.csv2xml.service.CSVValidationService;
import org.apache.commons.lang3.StringUtils;

import javax.inject.Named;
import java.util.Arrays;

@Named
public class CSVValidationServiceImpl implements CSVValidationService {

    @Override
    public boolean isCSVvalid(final String csv, final String delimiter) {
        if(StringUtils.isBlank(csv)) {
            return false;
        }

        final String[] rows = csv.split("\n");

        if(rows.length == 0) {
            return false;
        }

        final int numberOfDelimiters = rows[0].split(delimiter).length;
        return Arrays.stream(rows).parallel().noneMatch(row -> row.split(delimiter).length != numberOfDelimiters);
    }

}
