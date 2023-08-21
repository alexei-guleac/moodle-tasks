package com.example.jasper.service.interfaces;

import java.io.IOException;
import javax.annotation.PostConstruct;
import javax.xml.parsers.ParserConfigurationException;
import net.sf.jasperreports.engine.JRException;
import org.xml.sax.SAXException;

public interface DataTransfer {

  @PostConstruct
  void loadData()
      throws ParserConfigurationException, IOException, SAXException, JRException;
}
