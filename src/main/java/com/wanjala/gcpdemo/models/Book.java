package com.wanjala.gcpdemo.models;

import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Accessors(chain = true)
@Table(name="BOOK", schema = "book_store")
public class Book {

  @Id
  @GeneratedValue
  private Long id;

  @Column(name="PRICE")
  private BigDecimal price;

  @Column(name="TITLE")
  private String title;

}
