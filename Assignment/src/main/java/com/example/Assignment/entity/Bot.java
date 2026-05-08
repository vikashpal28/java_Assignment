package com.example.Assignment.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Bot extends Author {

    @Column(nullable = false)
  private  String name;
  private  String personalDescription;
}
