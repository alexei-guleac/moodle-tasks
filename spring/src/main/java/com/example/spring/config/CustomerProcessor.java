package com.example.spring.config;

import com.example.spring.entity.Customer;
import org.springframework.batch.item.ItemProcessor;

public class CustomerProcessor implements ItemProcessor<Customer, Customer> {

  @Override
  public Customer process(Customer customer) {
    if (customer.getCountry().equals("United States")) {
      return customer;
    } else {
      return null;
    }
  }
}
