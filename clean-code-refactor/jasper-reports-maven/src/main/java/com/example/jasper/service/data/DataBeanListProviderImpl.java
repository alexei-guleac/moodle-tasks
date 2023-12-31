package com.example.jasper.service.data;

import com.example.jasper.model.Holiday;
import com.example.jasper.repository.HolidayRepository;
import com.example.jasper.service.interfaces.DataBeanListProvider;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DataBeanListProviderImpl implements DataBeanListProvider {

  @Autowired
  private HolidayRepository holidayRepository;

  @Override
  public ArrayList<Holiday> getDataBeanList() {
    List<Holiday> all = holidayRepository.findAll();
    System.out.print(all.size());
    return new ArrayList<>(all);
  }

  /**
   * This method returns a DataBean object, with name and country set in it.
   */
  private Holiday produce(String name, String country) {
    Holiday dataBean = new Holiday();
    dataBean.setName(name);
    dataBean.setCountry(country);

    return dataBean;
  }
}
