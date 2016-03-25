package com.dream.rxjava.chapter8.api.github.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

import lombok.Data;

@Data
public class UsersResponse {

  @SerializedName("items") @Expose
  private List<User> users;
}
