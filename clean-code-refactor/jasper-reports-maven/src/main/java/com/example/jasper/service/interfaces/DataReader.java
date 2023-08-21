package com.example.jasper.service.interfaces;

import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;

public interface DataReader {

  void loadData() throws ParserConfigurationException, IOException, SAXException;
}
