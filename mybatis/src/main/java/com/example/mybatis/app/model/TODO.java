package com.example.mybatis.app.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TODO {
  private Long id;
  private String title;
  private String body;
}
