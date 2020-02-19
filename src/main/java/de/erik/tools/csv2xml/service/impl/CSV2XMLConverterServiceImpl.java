package de.erik.tools.csv2xml.service.impl;

import de.erik.tools.csv2xml.properties.Property;
import de.erik.tools.csv2xml.service.CSV2XMLConverterService;
import de.erik.tools.csv2xml.service.CSVValidationService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.inject.Inject;
import javax.inject.Named;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.StringWriter;
import java.util.Base64;

@Slf4j
@Data
@Named
public class CSV2XMLConverterServiceImpl implements CSV2XMLConverterService {

    @Inject
    @Property("default.root.element.name")
    private String defaultRootNodeName;

    @Inject
    @Property("default.row.element.name")
    private String defaultRowNodeName;

    @Inject
    @Property("default.seperator")
    private String defaultDelimiter;

    @Inject
    private CSVValidationService csvValidationService;

    @Override
    public byte[] convertCSV2XML(final byte[] content, final String delimiter, final boolean header, final String rootElementName, final String rowNodeName) throws ParserConfigurationException {
        final String csvDelimiter = defaultString(delimiter, this.defaultDelimiter);
        final String csv = new String(Base64.getDecoder().decode(content));

        if(!this.csvValidationService.isCSVvalid(csv, csvDelimiter)) {
            log.error("CSV data is invalid");
            return new byte[0];
        }

        final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        final DocumentBuilder builder = factory.newDocumentBuilder();
        final Document result = builder.newDocument();

        final String xmlRootElementName = defaultString(rootElementName, this.defaultRootNodeName);
        final Element root = result.createElement(xmlRootElementName);

        final String[] rows = csv.split("\n");
        final String xmlRowElementName = defaultString(rowNodeName, this.defaultRowNodeName);
        final String[] rowElementNames = extractRowElementNames(rows[0].split(csvDelimiter), header);

        for(final String row : rows) {
            final Element rowNode = result.createElement(xmlRowElementName);

            final String[] rowElements = row.split(csvDelimiter);
            for(int i=0; i<rowElements.length; i++) {
                final Element rowElement = result.createElement(rowElementNames[i]);
                rowElement.setTextContent(rowElements[i]);
                rowNode.appendChild(rowElement);
            }

            root.appendChild(rowNode);
        }

        result.appendChild(root);

        return getResultXMLContent(result);
    }

    private String[] extractRowElementNames(final String[] headerRow, final boolean header) {
        if(header) {
            return headerRow;
        }

        final String[] rowElementNames = new String[headerRow.length];
        for(int i=0; i<headerRow.length; i++) {
            rowElementNames[i] = "element" + i;
        }

        return rowElementNames;
    }

    private String defaultString(final String value, final String defaultValue) {
        return StringUtils.isNotBlank(value) ? value : defaultValue;
    }

    private byte[] getResultXMLContent(final Document document) {
        final TransformerFactory transfac = TransformerFactory.newInstance();

        final Transformer transformer;
        try {
            transformer = transfac.newTransformer();
            transformer.setOutputProperty(OutputKeys.METHOD, "xml");
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", Integer.toString(2));

            final StringWriter sw = new StringWriter();
            final StreamResult result = new StreamResult(sw);
            final DOMSource source = new DOMSource(document.getDocumentElement());

            transformer.transform(source, result);
            return sw.toString().getBytes();
        } catch (TransformerException e) {
            log.error("Error while creating result XML.", e);
        }

        return new byte[0];
    }

}
