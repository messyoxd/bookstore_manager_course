package com.messyo.bookstoremanager.controller;

import com.messyo.bookstoremanager.dto.BookDTO;
import com.messyo.bookstoremanager.dto.MessageResponseDTO;
import com.messyo.bookstoremanager.service.BookService;
import org.hamcrest.core.Is;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.view.json.MappingJackson2JsonView;

import static com.messyo.bookstoremanager.utils.BookUtils.asJsonString;
import static com.messyo.bookstoremanager.utils.BookUtils.createFakeBookDTO;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class BookControllerTest {
    private static final String BOOK_API_URL_PATH = "/api/v1/books";
    private MockMvc mockMvc;
    @Mock
    private BookService bookService;
    @InjectMocks
    private BookController bookController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(bookController)
                .setCustomArgumentResolvers(new PageableHandlerMethodArgumentResolver())
                .setViewResolvers((viewName, locale) -> new MappingJackson2JsonView())
                .build();
    }

    @Test
    void testWhenPOSTIsCalledThenABookShouldBeCreated() throws Exception {
        BookDTO bookDTO = createFakeBookDTO();
        MessageResponseDTO expectedMessageResponse = MessageResponseDTO.builder()
                .message("Book created with ID " + bookDTO.getId())
                .build();
        when(bookService.create(bookDTO)).thenReturn(expectedMessageResponse);

        mockMvc.perform(
                post(BOOK_API_URL_PATH)
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString((bookDTO))))
                .andExpect(status().isOk())
        .andExpect(jsonPath("$.message", Is.is(expectedMessageResponse.getMessage())));
    }
    @Test
    void testWhenPOSTwithInvalidISBNIsCalledThenABookShouldBeCreated() throws Exception {
        BookDTO bookDTO = createFakeBookDTO();
        bookDTO.setIsbn("Invalid isbn");

        mockMvc.perform(
                post(BOOK_API_URL_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString((bookDTO))))
                .andExpect(status().isBadRequest());
    }
}
