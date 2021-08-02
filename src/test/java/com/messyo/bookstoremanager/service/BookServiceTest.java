package com.messyo.bookstoremanager.service;

import com.messyo.bookstoremanager.dto.BookDTO;
import com.messyo.bookstoremanager.entity.Book;
import com.messyo.bookstoremanager.exception.BookNotFoundException;
import com.messyo.bookstoremanager.repository.BookRepository;
import com.messyo.bookstoremanager.utils.BookUtils;
import lombok.var;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static com.messyo.bookstoremanager.utils.BookUtils.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BookServiceTest {
    private static final String BOOK_API_URL_PATH = "/api/v1/books";

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BookService bookService;

    @Test
    void whenGivenUnExistingIdThenNotFoundThrowAnException() {
        var invalidId = 10L;
        when(bookRepository.findById(invalidId)).thenReturn(Optional.ofNullable(any(Book.class)));

        assertThrows(BookNotFoundException.class, () -> bookService.findById(invalidId));
    }

    @Test
    void whenGivenExistingIdThenReturnThisBook() throws BookNotFoundException {
        Book expectedFoundBook = createFakeBook();

        when(bookRepository.findById(expectedFoundBook.getId())).thenReturn(Optional.of(expectedFoundBook));

        BookDTO bookDTO = bookService.findById(expectedFoundBook.getId());

        assertEquals(expectedFoundBook.getName(), bookDTO.getName());
        assertEquals(expectedFoundBook.getIsbn(), bookDTO.getIsbn());
        assertEquals(expectedFoundBook.getPublisherName(), bookDTO.getPublisherName());
    }
}
