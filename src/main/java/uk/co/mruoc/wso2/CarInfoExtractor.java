package uk.co.mruoc.wso2;

import org.apache.commons.io.IOUtils;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.nio.charset.Charset;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class CarInfoExtractor {

    public CarInfo extract(File file) {
        return extractCarInfo(file);
    }

    private CarInfo extractCarInfo(File file) {
        String xml = extractArtifactsXmlAsString(file);
        Document document = toDocument(xml);
        return toCarInfo(document);
    }

    private String extractArtifactsXmlAsString(File file) {
        String filePath = file.getAbsolutePath();
        try {
            ZipFile zipFile = new ZipFile(file.getAbsolutePath());
            Enumeration zipEntries = zipFile.entries();
            while (zipEntries.hasMoreElements()) {
                ZipEntry entry = (ZipEntry) zipEntries.nextElement();
                if (entry.getName().contains("artifacts.xml")) {
                    try (InputStream is = zipFile.getInputStream(entry)) {
                        return IOUtils.toString(is, Charset.defaultCharset());
                    }
                }
            }
            throw new InvalidCarException(filePath + " does not contain an artifacts.xml file");
        } catch (IOException e) {
            throw new InvalidCarException("could not extract artifacts.xml from car " + filePath, e);
        }
    }

    private Document toDocument(String xml) {
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            InputSource source = new InputSource(new StringReader(xml));
            return db.parse(source);
        } catch (IOException | ParserConfigurationException | SAXException e) {
            throw new InvalidCarException("cannot parse xml: " + xml, e);
        }
    }

    private CarInfo toCarInfo(Document document) {
        Node artifact = getArtifactNode(document);
        String name = extractAttribute(artifact, "name");
        String version = extractAttribute(artifact, "version");
        return new CarInfo(name, version);
    }

    private Node getArtifactNode(Document document) {
        NodeList nodes = document.getElementsByTagName("artifact");
        return nodes.item(0);
    }

    private String extractAttribute(Node node, String name) {
        NamedNodeMap attributes = node.getAttributes();
        Node attributeNode = attributes.getNamedItem(name);
        return attributeNode.getTextContent();
    }

}
