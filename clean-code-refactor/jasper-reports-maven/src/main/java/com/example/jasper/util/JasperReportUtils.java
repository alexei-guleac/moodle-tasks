package com.example.jasper.util;

import com.example.jasper.model.Holiday;
import java.io.File;
import java.io.FileNotFoundException;
import java.sql.ResultSet;
import java.util.List;
import java.util.Map;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRResultSetDataSource;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

@Service
public class JasperReportUtils {

  public JasperReport getJasperReport(File file) throws JRException {
    return JasperCompileManager.compileReport(file.getAbsolutePath());
  }

  public JasperPrint getResultSetJasperPrint(JasperReport jasperReport,
      JRResultSetDataSource resultSetDataSource, Map<String, Object> parameters)
      throws JRException {
    return JasperFillManager
        .fillReport(jasperReport, parameters, resultSetDataSource);
  }

  public JRResultSetDataSource getResultSetDataSource(ResultSet holydays) {
    return new JRResultSetDataSource(holydays);
  }

  public JasperPrint getCollectionJasperPrint(JasperReport jasperReport,
      JRBeanCollectionDataSource dataSource, Map<String, Object> parameters) throws JRException {
    return JasperFillManager.fillReport(jasperReport, parameters, dataSource);
  }

  public JRBeanCollectionDataSource getCollectionDataSource(List<Holiday> holydays) {
    return new JRBeanCollectionDataSource(holydays);
  }

  public void exportReportToHtml(String path, JasperPrint jasperPrint, String resultFileName)
      throws JRException {
    JasperExportManager.exportReportToHtmlFile(jasperPrint, path + resultFileName);
  }

  public void exportReportToPdf(String path, JasperPrint jasperPrint, String s) throws JRException {
    JasperExportManager.exportReportToPdfFile(jasperPrint, path + s);
  }
}
