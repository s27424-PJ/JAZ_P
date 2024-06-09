package org.example.bookproject;

import io.swagger.client.model.BookRequest;
import io.swagger.client.model.BookResponse;
import org.example.bookproject.feignclient.BookOrderFeignClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private BookMapper bookMapper = Mappers.getMapper(BookMapper.class);

    @Mock
    private AuthorRepository authorRepository;

    @Mock
    private BookOrderFeignClient bookOrderFeignClient;

    @InjectMocks
    private BookService bookService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllBooks_ShouldReturnNoContent_WhenNoBooksExist() {
        when(bookRepository.findAll()).thenReturn(Collections.emptyList());

        ResponseEntity<List<BookResponse>> response = bookService.getAllBooks();

        assertNotNull(response);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(bookRepository, times(1)).findAll();
        verify(authorRepository, times(0)).findById(anyInt());
    }

    @Test
    void getBookById_ShouldReturnBook_WhenBookExists() {
        Book book = new Book();
        book.setId(1);
        book.setAuthorId(1);
        book.setLicznikOdwiedzin(0); // Initial value to simulate incrementing the counter
        Author author = new Author();
        author.setId(1);
        author.setName("Author Name");

        when(bookRepository.findById(anyInt())).thenReturn(Optional.of(book));
        when(bookMapper.mapEntityToResponse(any(Book.class))).thenReturn(new BookResponse());
        when(authorRepository.findById(anyInt())).thenReturn(Optional.of(author));

        ResponseEntity<BookResponse> response = bookService.getBookById(1);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());

        verify(bookRepository, times(2)).findById(anyInt());
        verify(bookRepository, times(1)).save(any(Book.class));
        verify(authorRepository, times(1)).findById(anyInt());
    }

    @Test
    void getBookById_ShouldThrowResourceNotFoundException_WhenBookDoesNotExist() {
        when(bookRepository.findById(anyInt())).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> {
            bookService.getBookById(1);
        });

        verify(bookRepository, times(1)).findById(anyInt());
        verify(authorRepository, times(0)).findById(anyInt());
    }

    @Test
    void saveBook_ShouldReturnSavedBook() {
        BookRequest bookRequest = new BookRequest();
        Book book = new Book();
        book.setId(1);
        book.setAuthorId(1);

        when(bookRepository.findTopByOrderByIdDesc()).thenReturn(book);
        when(bookMapper.mapToBook(any(BookRequest.class))).thenReturn(book);
        when(bookRepository.save(any(Book.class))).thenReturn(book);
        when(bookMapper.mapEntityToResponse(any(Book.class))).thenReturn(new BookResponse());

        ResponseEntity<BookResponse> response = bookService.saveBook(bookRequest);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        verify(bookRepository, times(1)).findTopByOrderByIdDesc();
        verify(bookRepository, times(1)).save(any(Book.class));
    }

    @Test
    void purchaseBook_ShouldReturnUpdatedBook_WhenBookIsInStock() {
        Book book = new Book();
        book.setId(1);
        book.setAuthorId(1);
        book.setSztuki(5);
        Author author = new Author();
        author.setId(1);
        author.setName("Author Name");

        when(bookRepository.findById(anyInt())).thenReturn(Optional.of(book));
        when(bookMapper.mapEntityToResponse(any(Book.class))).thenReturn(new BookResponse());
        when(authorRepository.findById(anyInt())).thenReturn(Optional.of(author));

        ResponseEntity<BookResponse> response = bookService.purchaseBook(1);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        verify(bookRepository, times(1)).findById(anyInt());
        verify(bookRepository, times(1)).save(any(Book.class));
        verify(authorRepository, times(1)).findById(anyInt());
    }

    @Test
    void purchaseBook_ShouldThrowOutOfStockException_WhenBookIsOutOfStock() {
        Book book = new Book();
        book.setId(1);
        book.setAuthorId(1);
        book.setSztuki(0);

        when(bookRepository.findById(anyInt())).thenReturn(Optional.of(book));

        assertThrows(OutOfStockException.class, () -> {
            bookService.purchaseBook(1);
        });

        verify(bookRepository, times(1)).findById(anyInt());
        verify(bookRepository, times(0)).save(any(Book.class));
    }
}
