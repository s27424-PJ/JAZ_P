package org.example.bookproject;

import io.swagger.client.model.BookRequest;
import io.swagger.client.model.BookResponse;
import org.example.bookproject.feignclient.BookOrderFeignClient;
import org.example.bookproject.feignclient.BookOrderRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookService  {
    private final BookRepository bookRepository;
    private final BookMapper bookMapper;
    private final AuthorRepository authorRepository;
    private final BookOrderFeignClient bookOrderFeignClient;
    @Autowired
    public BookService(BookRepository bookRepository, BookMapper bookMapper, AuthorRepository authorRepository, BookOrderFeignClient bookOrderFeignClient) {
        this.bookRepository = bookRepository;
        this.bookMapper = bookMapper;
        this.authorRepository = authorRepository;
        this.bookOrderFeignClient = bookOrderFeignClient;
    }




    public ResponseEntity<List<BookResponse>> getAllBooks() {
        List<BookResponse> foundBooks = bookRepository.findAll().stream()
                .map(bookMapper::mapEntityToResponse)
                .peek(bookResponse -> {
                    Author author = authorRepository.findById(bookResponse.getAuthorId())
                            .orElseThrow(() -> new ResourceNotFoundException("Author not found for book with id: " + bookResponse.getId()));
                    bookResponse.setAuthorName(author.getName());
                })
                .collect(Collectors.toList());

        if (foundBooks.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return new ResponseEntity<>(foundBooks, HttpStatus.OK);
    }


    public ResponseEntity<BookResponse> getBookById(int id) {
        Book book = bookRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Book not found with id: " + id));
        book.setLicznikOdwiedzin(book.getLicznikOdwiedzin() + 1);
        bookRepository.save(book);
        checkAndCreateBookOrder(id);
        BookResponse bookResponse = bookMapper.mapEntityToResponse(book);
        Author author = authorRepository.findById(book.getAuthorId()).orElseThrow(() -> new ResourceNotFoundException("Author not found for book with id: " + id));
        bookResponse.setAuthorName(author.getName());
        return new ResponseEntity<>(bookResponse, HttpStatus.OK);
    }

    // W BookService
    public ResponseEntity<List<BookResponse>> findBooksByGenre(String genre) {
        List<Book> books = bookRepository.findByGatunek(genre);
        List<BookResponse> bookResponses = books.stream().map(book -> {
            BookResponse bookResponse = bookMapper.mapEntityToResponse(book);
            Author author = authorRepository.findById(book.getAuthorId())
                    .orElseThrow(() -> new ResourceNotFoundException("Author not found for book with id: " + book.getId()));
            bookResponse.setAuthorName(author.getName());
            return bookResponse;
        }).collect(Collectors.toList());
        return new ResponseEntity<>(bookResponses, HttpStatus.OK);
    }


    public ResponseEntity<BookResponse> saveBook(BookRequest createRequest) {
        Book lastBook = bookRepository.findTopByOrderByIdDesc();
        System.out.println(lastBook.getId());
        Book book = bookMapper.mapToBook(createRequest);
        System.out.println(book.getId());
        book.setId(lastBook.getId() + 1);
        Book savedBook = bookRepository.save(book);


        return new ResponseEntity<>(bookMapper.mapEntityToResponse(savedBook), HttpStatus.OK);
    }
    public void checkAndCreateBookOrder(int bookId) {
        Book book = bookRepository.findById(bookId).orElseThrow(() -> new ResourceNotFoundException("Book not found with id: " + bookId));
        if (book.getLicznikOdwiedzin() % 10 == 0 && book.getLicznikOdwiedzin() > 0) {
            BookOrderRequest request = new BookOrderRequest();
            request.setBookId(bookId);
            request.setQuantity(1);
            bookOrderFeignClient.createBookOrder(request);
        }
    }
    public ResponseEntity<BookResponse> purchaseBook(int id) {
        Book book = bookRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Book not found with id " + id));
        if (book.getSztuki() > 0) {
            book.setSztuki(book.getSztuki() - 1);
            bookRepository.save(book);
            BookResponse bookResponse = bookMapper.mapEntityToResponse(book);
            Author author = authorRepository.findById(book.getAuthorId()).orElseThrow(() -> new ResourceNotFoundException("Author not found for book with id: " + id));
            bookResponse.setAuthorName(author.getName());


            BookOrderRequest request = new BookOrderRequest();
            request.setBookId(id);
            request.setQuantity(1);
            bookOrderFeignClient.createBookOrder(request);

            return new ResponseEntity<>(bookResponse, HttpStatus.OK);
        } else {
            throw new OutOfStockException("Book with id " + id + " is out of stock");
        }
    }

    public ResponseEntity<BookResponse> updateBook(int id, BookRequest updateRequest) {
        Book book = bookRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Book not found with id: " + id));

        Book updatedBook = bookMapper.update(updateRequest, book);
        return new ResponseEntity<>(bookMapper.mapEntityToResponse(bookRepository.save(updatedBook)), HttpStatus.OK);
    }

    public ResponseEntity<Void> deleteBook(int id) {
        Book book = bookRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Book not found with id: " + id));

        bookRepository.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
