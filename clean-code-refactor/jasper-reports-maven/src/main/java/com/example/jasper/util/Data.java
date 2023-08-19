package com.example.jasper.util;

import com.example.jasper.model.Holiday;
import com.example.jasper.repository.HolidayRepository;
import java.time.LocalDate;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class Data {

  @Autowired
  private HolidayRepository holidayRepository;

  @PostConstruct
  public void loadData() {
    Holiday holiday = new Holiday();
    holiday.setName("TEST");
    holiday.setCountry("MD");
    holiday.setDate(LocalDate.now());

    Holiday holiday1 = new Holiday();
    holiday1.setName("TEST1");
    holiday1.setCountry("MD1");
    holiday1.setDate(LocalDate.now());

    Holiday holiday2 = new Holiday();
    holiday2.setName("TEST2");
    holiday2.setCountry("MD2");
    holiday2.setDate(LocalDate.now());

//        holidayRepository.save(holiday);
//        holidayRepository.save(holiday1);
//        holidayRepository.save(holiday2);

  }

}
