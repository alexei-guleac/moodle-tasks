package com.example.jasper.service;

import com.example.jasper.service.interfaces.DataReader;
import com.example.jasper.service.interfaces.DataTransfer;
import com.example.jasper.service.interfaces.DynamicReportCrosstab;
import com.example.jasper.service.interfaces.JasperReportFill;
import java.io.IOException;
import javax.annotation.PostConstruct;
import javax.xml.parsers.ParserConfigurationException;
import net.sf.jasperreports.engine.JRException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.xml.sax.SAXException;

@Service
public class DataTransferImpl implements DataTransfer {

  @Autowired
  private DataReader dataReader;

  @Autowired
  private JasperReportFill jasperReportFill;


  @Autowired
  private DynamicReportCrosstab dynamicReportCrosstab;


  @Override
  @PostConstruct
  public void loadData()
      throws ParserConfigurationException, IOException, SAXException, JRException {
    dataReader.loadData();
    jasperReportFill.fillReportWithCollectionBeanDataSource();
    jasperReportFill.fillReportWithResultSetDataSource();
    dynamicReportCrosstab.build();
  }
}
