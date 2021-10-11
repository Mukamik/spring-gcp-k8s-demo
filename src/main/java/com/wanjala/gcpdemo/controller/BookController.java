package com.wanjala.gcpdemo.controller;

import com.wanjala.gcpdemo.entity.Book;
import com.wanjala.gcpdemo.repository.BookRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class BookController {

  @Autowired
  private BookRepository bookRepository;

  @GetMapping
  public List<Book> findAll() {
    return bookRepository.findAllOrderByPriceDesc();
  }

}
