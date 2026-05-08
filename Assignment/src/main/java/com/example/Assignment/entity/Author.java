package com.example.Assignment.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Author {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Long id;
}
