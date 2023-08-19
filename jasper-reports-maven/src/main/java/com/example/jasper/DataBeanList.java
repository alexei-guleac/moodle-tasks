package com.example.jasper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class DataBeanList {

    @Autowired
    private HolidayRepository holidayRepository;

    public ArrayList<Holiday> getDataBeanList() {
        List<Holiday> all = holidayRepository.findAll();
        System.out.print(all.size());
        return new ArrayList<>(all);
    }

    /**
     * This method returns a DataBean object,
     * with name and country set in it.
     */
    private Holiday produce(String name, String country) {
        Holiday dataBean = new Holiday();
        dataBean.setName(name);
        dataBean.setCountry(country);

        return dataBean;
    }
}
