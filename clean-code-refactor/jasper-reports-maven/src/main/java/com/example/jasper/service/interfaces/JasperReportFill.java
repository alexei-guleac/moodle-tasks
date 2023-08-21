package com.example.jasper.service.interfaces;

import java.io.FileNotFoundException;
import net.sf.jasperreports.engine.JRException;

public interface JasperReportFill {

  void fillReportWithCollectionBeanDataSource() throws JRException, FileNotFoundException;

  void fillReportWithResultSetDataSource() throws JRException, FileNotFoundException;
}
