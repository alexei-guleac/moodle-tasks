package com.example.jasper;

import net.sf.jasperreports.engine.JRException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.xml.sax.SAXException;

import javax.annotation.PostConstruct;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

@Service
public class DataTransfer {

    @Autowired
    private DataReader dataReader;

    @Autowired
    private JasperReportFill jasperReportFill;


    @Autowired
    private DynamicReportCrosstab dynamicReportCrosstab;


    @PostConstruct
    public void loadData() throws ParserConfigurationException, IOException, SAXException, JRException {
        dataReader.loadData();
        jasperReportFill.fillReportWithCollectionBeanDataSource();
        jasperReportFill.fillReportWithResultSetDataSource();
        dynamicReportCrosstab.build();
    }
}
