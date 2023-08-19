package com.example.jasper.service;


import static net.sf.dynamicreports.report.builder.DynamicReports.cmp;
import static net.sf.dynamicreports.report.builder.DynamicReports.ctab;
import static net.sf.dynamicreports.report.builder.DynamicReports.report;

import com.example.jasper.model.Holiday;
import com.example.jasper.util.Templates;
import java.util.ArrayList;
import java.util.HashMap;
import net.sf.dynamicreports.report.builder.crosstab.CrosstabBuilder;
import net.sf.dynamicreports.report.builder.crosstab.CrosstabColumnGroupBuilder;
import net.sf.dynamicreports.report.builder.crosstab.CrosstabRowGroupBuilder;
import net.sf.dynamicreports.report.constant.Calculation;
import net.sf.dynamicreports.report.constant.PageOrientation;
import net.sf.dynamicreports.report.constant.PageType;
import net.sf.dynamicreports.report.datasource.DRDataSource;
import net.sf.dynamicreports.report.exception.DRException;
import net.sf.jasperreports.engine.JRDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DynamicReportCrosstab {

  @Autowired
  private DataBeanListProvider dataBeanListProvider;

  public void build() {
    CrosstabRowGroupBuilder<String> rowGroup = ctab.rowGroup("country", String.class)
        .setTotalHeader("Total per month");
    CrosstabColumnGroupBuilder<Integer> columnGroup = ctab.columnGroup("month", Integer.class);

    CrosstabBuilder crosstab = ctab.crosstab()
        .headerCell(cmp.text(" ").setStyle(Templates.boldCenteredStyle))
        .rowGroups(rowGroup)
        .columnGroups(columnGroup)
        .measures(
            ctab.measure("count", Integer.class, Calculation.SUM));
    try {
      report()
          .setPageFormat(PageType.A4, PageOrientation.LANDSCAPE)
          .setTemplate(Templates.reportTemplate)
          .title(Templates.createTitleComponent("Report"))
          .summary(crosstab)
          .pageFooter(Templates.footerComponent)
          .setDataSource(createDataSource())
          .show();

    } catch (DRException e) {
      e.printStackTrace();
    }
  }


  private JRDataSource createDataSource() {

    DRDataSource dataSource = new DRDataSource("country", "month", "count");

    ArrayList<Holiday> dataBeanList = this.dataBeanListProvider.getDataBeanList();

    HashMap<String, HashMap<Integer, Integer>> countryHashMap = new HashMap<>();

    for (Holiday holiday : dataBeanList) {
      countryHashMap.put(holiday.getCountry(), new HashMap<>());
    }
    System.out.println(countryHashMap);

    for (Holiday holiday : dataBeanList) {
      HashMap<Integer, Integer> hashMap = countryHashMap.get(holiday.getCountry());
      for (int i = 1; i <= 12; i++) {
        hashMap.put(i, 0);
      }
    }
    System.out.println(countryHashMap);

    for (Holiday holiday : dataBeanList) {
      HashMap<Integer, Integer> monthsValuesMap = countryHashMap.get(holiday.getCountry());
//            System.out.println("load " + countryHashMap);

      int count = monthsValuesMap.getOrDefault(holiday.getDate().getMonthValue(), 0);
      monthsValuesMap.put(holiday.getDate().getMonthValue(), count + 1);
    }

    countryHashMap.forEach((country, monthsValuesMap) -> {
      monthsValuesMap.forEach((month, count) -> {
        dataSource.add(country, month, count);
      });
    });

    return dataSource;
  }

}
