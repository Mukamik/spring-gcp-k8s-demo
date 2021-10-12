package com.wanjala.gcpdemo.controllers;

import com.wanjala.gcpdemo.models.Book;
import com.wanjala.gcpdemo.services.BookService;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/books")
public class BookController {

  @Autowired
  private BookService bookService;

  @GetMapping
  public List<Book> findAll() {
    List<Book> books = bookService
        .findAllBooksInOrderOfPriceDescending();
    log.info("Found " +books.size() + " books");
    return books;
  }

}
