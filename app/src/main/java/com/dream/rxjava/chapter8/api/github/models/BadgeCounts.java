package com.dream.rxjava.chapter8.api.github.models;

import com.google.gson.annotations.Expose;

import lombok.Data;

@Data
public class BadgeCounts {

  @Expose
  private Integer bronze;

  @Expose
  private Integer silver;

  @Expose
  private Integer gold;
}