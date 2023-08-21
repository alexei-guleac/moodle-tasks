package com.example.jasper.util;

import java.io.File;
import java.io.IOException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

@Service
public class XmlUtils {

  public DocumentBuilder getDocumentBuilder() throws ParserConfigurationException {
    //an instance of factory that gives a document builder
    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
    //an instance of builder to parse the specified xml file
    return dbf.newDocumentBuilder();
  }

  public NodeList getElementsByTag(Document doc, String elementName) {
    return doc.getElementsByTagName(elementName);
  }

  public Document getParsedFile(File file, DocumentBuilder db) throws SAXException, IOException {
    return db.parse(file);
  }
}
