package com.wanjala.gcpdemo.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="BOOK", schema = "book_store")
public class Book {

  @Id
  @GeneratedValue
  private Long id;

  @Column(name="PRICE")
  private Long price;

  @Column(name="TITLE")
  private String title;

  public Book() {
    super();
  }

  public Book(Long id, Long price, String title){
    super();
    this.id = id;
    this.price = price;
    this.title = title;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public Long getPrice() {
    return price;
  }

  public void setPrice(Long price) {
    this.price = price;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }
}
