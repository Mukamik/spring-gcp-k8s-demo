package com.wanjala.gcpdemo.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "BOOK")
public class Book {

  @Id
  @GeneratedValue
  @Column(name = "ID")
  Long id;

  @Column(name = "PRICE")
  Long price;

  @Column(name = "TITLE")
  String title;
}
