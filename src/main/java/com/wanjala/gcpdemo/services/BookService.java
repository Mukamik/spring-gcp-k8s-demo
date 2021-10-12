package com.wanjala.gcpdemo.services;

import com.wanjala.gcpdemo.models.Book;
import com.wanjala.gcpdemo.repositories.BookRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BookService {

  @Autowired
  private BookRepository bookRepository;


  public List<Book> findAllBooksInOrderOfPriceDescending() {
    return bookRepository.findAllByOrderByPriceDesc();
  }
}
