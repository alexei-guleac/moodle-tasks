package com.example.jasper;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class JasperReportFill {

    @Autowired
    private DataBeanList dataBeanList;

    @Autowired
    private ResultSetConnectionManager resultSetConnectionManager;

    public void fillReportWithCollectionBeanDataSource() throws JRException, FileNotFoundException {
        String path = ".\\";
        List<Holiday> holydays = dataBeanList.getDataBeanList();

        //load file and compile it
        File file = ResourceUtils.getFile("classpath:holydaysreportdesign.jrxml");

        JasperReport jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());
        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(holydays);
        Map<String, Object> parameters = new HashMap<>();

        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);
        JasperExportManager.exportReportToHtmlFile(jasperPrint, path + "holydays.html");
        JasperExportManager.exportReportToPdfFile(jasperPrint, path + "holydays.pdf");

        System.out.println("report generated in path : " + path);
    }

    public void fillReportWithResultSetDataSource() throws JRException, FileNotFoundException {
        String path = ".\\";
        ResultSet holydays = resultSetConnectionManager.getResultSet();

        //load file and compile it
        File file = ResourceUtils.getFile("classpath:holydaysreportdesign-set.jrxml");

        JasperReport jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());
        JRResultSetDataSource resultSetDataSource = new
                JRResultSetDataSource(holydays);
        Map<String, Object> parameters = new HashMap<>();

        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, resultSetDataSource);
        JasperExportManager.exportReportToHtmlFile(jasperPrint, path + "holydays_set.html");
        JasperExportManager.exportReportToPdfFile(jasperPrint, path + "holydays_set.pdf");

        System.out.println("report generated in path : " + path);
    }

}
