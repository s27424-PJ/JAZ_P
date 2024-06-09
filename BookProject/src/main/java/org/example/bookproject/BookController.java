package org.example.bookproject;
import io.swagger.client.model.BookResponse;
import io.swagger.client.model.BookRequest;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/books")
public class BookController {
    private final BookService bookService;

    @Autowired
    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping
    public ResponseEntity<List<BookResponse>> getAllBooks() {
        return bookService.getAllBooks();
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookResponse> getBookById(@PathVariable int id) {
        return bookService.getBookById(id);
    }

    @PostMapping
    public ResponseEntity<BookResponse> createBook(@RequestBody @Valid BookRequest bookRequest) {
        return bookService.saveBook(bookRequest);
    }
    @GetMapping("/genre/{genre}")
    public ResponseEntity<List<BookResponse>> findBooksByGenre(@PathVariable String genre) {
        return bookService.findBooksByGenre(genre);
    }
    @PostMapping("/purchase/{id}")
    public ResponseEntity<BookResponse> purchaseBook(@PathVariable int id) {
        return bookService.purchaseBook(id);
    }
    @PutMapping("/{id}")
    public ResponseEntity<BookResponse> updateBook(@PathVariable int id, @RequestBody BookRequest bookRequest) {
        return bookService.updateBook(id, bookRequest);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable int id) {
        return bookService.deleteBook(id);
    }
}
