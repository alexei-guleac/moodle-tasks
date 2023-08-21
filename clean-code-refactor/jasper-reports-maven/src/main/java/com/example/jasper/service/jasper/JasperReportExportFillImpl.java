package com.example.jasper.service.jasper;

import com.example.jasper.model.Holiday;
import com.example.jasper.service.interfaces.DataBeanListProvider;
import com.example.jasper.service.interfaces.JasperReportFill;
import com.example.jasper.service.interfaces.ResultSetConnectionManager;
import com.example.jasper.util.FileUtils;
import com.example.jasper.util.JasperReportUtils;
import java.io.File;
import java.io.FileNotFoundException;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRResultSetDataSource;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class JasperReportExportFillImpl implements JasperReportFill {

  @Autowired
  private DataBeanListProvider dataBeanListProvider;

  @Autowired
  private ResultSetConnectionManager resultSetConnectionManager;

  @Autowired
  private FileUtils fileUtils;

  @Autowired
  private JasperReportUtils jasperReportUtils;

  @Override
  public void fillReportWithCollectionBeanDataSource() throws JRException, FileNotFoundException {
    String path = ".\\";
    List<Holiday> holydays = dataBeanListProvider.getDataBeanList();

    //load file and compile it
    File file = fileUtils.getFile("classpath:holydaysreportdesign.jrxml");

    JasperReport jasperReport = jasperReportUtils.getJasperReport(file);
    JRBeanCollectionDataSource dataSource = jasperReportUtils.getCollectionDataSource(holydays);
    Map<String, Object> parameters = new HashMap<>();

    JasperPrint jasperPrint = jasperReportUtils
        .getCollectionJasperPrint(jasperReport, dataSource, parameters);
    jasperReportUtils.exportReportToHtml(path, jasperPrint, "holydays.html");
    jasperReportUtils.exportReportToPdf(path, jasperPrint, "holydays.pdf");

    System.out.println("report generated in path : " + path);
  }


  @Override
  public void fillReportWithResultSetDataSource() throws JRException, FileNotFoundException {
    String path = ".\\";
    ResultSet holydays = resultSetConnectionManager.getResultSet();

    //load file and compile it
    File file = fileUtils.getFile("classpath:holydaysreportdesign-set.jrxml");

    JasperReport jasperReport = jasperReportUtils.getJasperReport(file);
    JRResultSetDataSource resultSetDataSource = jasperReportUtils.getResultSetDataSource(holydays);
    Map<String, Object> parameters = new HashMap<>();

    JasperPrint jasperPrint = jasperReportUtils
        .getResultSetJasperPrint(jasperReport, resultSetDataSource,
            parameters);
    jasperReportUtils.exportReportToHtml(path, jasperPrint, "holydays_set.html");
    jasperReportUtils.exportReportToPdf(path, jasperPrint, "holydays_set.pdf");

    System.out.println("report generated in path : " + path);
  }


}
