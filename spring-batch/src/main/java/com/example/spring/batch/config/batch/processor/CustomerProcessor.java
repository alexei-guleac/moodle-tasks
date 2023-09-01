package com.example.spring.batch.config.batch.processor;

import com.example.spring.batch.entity.Customer;
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
