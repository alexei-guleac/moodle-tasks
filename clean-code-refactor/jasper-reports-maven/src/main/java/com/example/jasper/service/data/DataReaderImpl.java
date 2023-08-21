package com.example.jasper.service.data;

import com.example.jasper.model.Holiday;
import com.example.jasper.repository.HolidayRepository;
import com.example.jasper.service.interfaces.DataReader;
import com.example.jasper.util.DateUtils;
import com.example.jasper.util.XmlUtils;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

@Service
public class DataReaderImpl implements DataReader {

  @Autowired
  private HolidayRepository holidayRepository;

  @Autowired
  private DateUtils dateUtils;

  @Autowired
  private XmlUtils xmlUtils;

  @Override
  public void loadData() throws ParserConfigurationException, IOException, SAXException {
    File file = new File("C:\\Users\\CRME061\\Downloads\\MyDataBase.xml");

    DateTimeFormatter formatter = dateUtils.getDateTimeFormatter();
    DocumentBuilder db = xmlUtils.getDocumentBuilder();
    Document doc = xmlUtils.getParsedFile(file, db);
    doc.getDocumentElement().normalize();
    System.out.println("Root element: " + doc.getDocumentElement().getNodeName());

    NodeList nodeList = xmlUtils.getElementsByTag(doc, "holydays");
    for (int itr = 0; itr < nodeList.getLength(); itr++) {
      fillAndSaveHolyday(formatter, nodeList, itr);
    }
  }

  private void fillAndSaveHolyday(DateTimeFormatter formatter, NodeList nodeList, int itr) {
    Node node = nodeList.item(itr);
    if (node.getNodeType() == Node.ELEMENT_NODE) {
      Element eElement = (Element) node;

      Holiday holiday = getHolidayFromData(formatter, eElement);

      save(holiday);
    }
  }


  private Holiday getHolidayFromData(DateTimeFormatter formatter, Element eElement) {
    Holiday holiday = new Holiday();
    holiday.setName(eElement.getElementsByTagName("NAME").item(0).getTextContent());
    holiday.setCountry(eElement.getElementsByTagName("COUNTRY").item(0).getTextContent());
    holiday.setDate(LocalDate
        .parse(eElement.getElementsByTagName("DATE").item(0).getTextContent(), formatter));

    return holiday;
  }

  private void save(Holiday holiday) {
    holidayRepository.save(holiday);
  }


}
