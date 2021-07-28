package com.messyo.bookstoremanager.controller;

import com.messyo.bookstoremanager.dto.MessageResponseDTO;
import com.messyo.bookstoremanager.entity.Book;
import com.messyo.bookstoremanager.repository.BookRepository;
import com.messyo.bookstoremanager.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/books")
public class BookController {

    private BookService bookService;

    @Autowired
    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @PostMapping
    public MessageResponseDTO create(@RequestBody Book book){
        return bookService.create(book);
    }
}
