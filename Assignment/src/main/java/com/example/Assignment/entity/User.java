package com.example.Assignment.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "users")
public class User extends Author {


  private String username;

  private Boolean isPremium;

}
