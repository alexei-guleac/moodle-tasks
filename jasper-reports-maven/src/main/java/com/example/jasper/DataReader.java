package com.example.jasper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Service
public class DataReader {

    @Autowired
    private HolidayRepository holidayRepository;

    public void loadData() throws ParserConfigurationException, IOException, SAXException {
        File file = new File("C:\\Users\\CRME061\\Downloads\\MyDataBase.xml");

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d/MM/yyyy");

        //an instance of factory that gives a document builder
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        //an instance of builder to parse the specified xml file
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document doc = db.parse(file);
        doc.getDocumentElement().normalize();
        System.out.println("Root element: " + doc.getDocumentElement().getNodeName());

        NodeList nodeList = doc.getElementsByTagName("holydays");
        for (int itr = 0; itr < nodeList.getLength(); itr++) {
            Node node = nodeList.item(itr);
//            System.out.println("\nNode Name : " + node.getNodeName());
            if (node.getNodeType() == Node.ELEMENT_NODE) {
//                System.out.println("TRUEEEE");
                Element eElement = (Element) node;
//                System.out.println((eElement.getElementsByTagName("COUNTRY").item(0).getTextContent()));
//                System.out.println((eElement.getElementsByTagName("DATE").item(0).getTextContent()));
//                System.out.println((eElement.getElementsByTagName("NAME").item(0).getTextContent()));

                Holiday holiday = new Holiday();
                holiday.setName(eElement.getElementsByTagName("NAME").item(0).getTextContent());
                holiday.setCountry(eElement.getElementsByTagName("COUNTRY").item(0).getTextContent());
                holiday.setDate(LocalDate.parse(eElement.getElementsByTagName("DATE").item(0).getTextContent(), formatter));

                holidayRepository.save(holiday);
            }
        }
    }
}
