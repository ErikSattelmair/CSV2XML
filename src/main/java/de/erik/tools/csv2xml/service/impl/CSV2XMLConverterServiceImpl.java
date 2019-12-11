package de.erik.tools.csv2xml.service.impl;

import de.erik.tools.csv2xml.properties.Property;
import de.erik.tools.csv2xml.service.CSV2XMLConverterService;
import de.erik.tools.csv2xml.service.CSVValidationService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.util.stream.IntStream;

@Named("csv2xmlConverter")
@Slf4j
@Data
@ApplicationScoped
public class CSV2XMLConverterServiceImpl implements CSV2XMLConverterService {

    @Property("default.root.element.name")
    private String defaultRootNodeName;

    @Property("default.row.element.name")
    private String defaultRowNodeName;

    @Property("default.seperator")
    private String defaultDelimiter;

    @Named("csvValidator")
    private CSVValidationService csvValidationService;

    @Override
    public byte[] convertCSV2XML(final byte[] csv, final String delimiter, final boolean header, final String rootElementName, final String rowNodeName) throws ParserConfigurationException {
        final String csvDelimiter = StringUtils.defaultString(delimiter, this.defaultDelimiter);

        if(!this.csvValidationService.isCSVvalid(csv, csvDelimiter)) {
            log.error("CSV data is invalid");
            return new byte[0];
        }

        final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        final DocumentBuilder builder = factory.newDocumentBuilder();
        final Document result = builder.newDocument();

        final String xmlRootElementName = StringUtils.isNotBlank(rootElementName) ? rootElementName : this.defaultRootNodeName;
        final Element root = result.createElement(xmlRootElementName);

        final String[] rows = new String(csv).split("\n");
        final String xmlRowElementName = StringUtils.isNotBlank(rowNodeName) ? rowNodeName : this.defaultRowNodeName;
        final String[] rowElementNames = extractRowElementNames(rows[0].split(csvDelimiter), header);

        for(final String row : rows) {
            final Element rowNode = result.createElement(xmlRowElementName);
            final String[] rowElements = row.split("\n");

            for(int i=0; i<rowElements.length; i++) {
                final Element rowElement = result.createElement(rowElementNames[i]);
                rowElement.setTextContent(rowElements[i]);
            }

            root.appendChild(rowNode);
        }

        return new byte[0];
    }

    private String[] extractRowElementNames(final String[] headerRow, final boolean header) {
        if(header) {
            return headerRow;
        }

        return (String[]) IntStream.range(0, headerRow.length).mapToObj(num -> "element" + num).toArray();
    }

}
