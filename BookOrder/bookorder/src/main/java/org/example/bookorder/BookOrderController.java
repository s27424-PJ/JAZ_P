package org.example.bookorder;


import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/book-orders")
public class BookOrderController {
    private final BookOrderService bookOrderService;

    @Autowired
    public BookOrderController(BookOrderService bookOrderService) {
        this.bookOrderService = bookOrderService;
    }

    @GetMapping
    public ResponseEntity<List<BookOrderResponse>> getAllBookOrders() {
        return bookOrderService.getAllBookOrders();
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookOrderResponse> getBookOrderById(@PathVariable UUID id) {
        return bookOrderService.getBookOrderById(id);
    }

    @PostMapping
    public ResponseEntity<BookOrderResponse> createBookOrder(@RequestBody BookOrderRequest bookOrderRequest) {
        return bookOrderService.createBookOrder(bookOrderRequest);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BookOrderResponse> updateBookOrder(@PathVariable UUID id, @RequestBody BookOrderRequest bookOrderRequest) {
        return bookOrderService.updateBookOrder(id, bookOrderRequest);
    }
    @GetMapping("/print/{id}")
    public ResponseEntity<InputStreamResource> printOrder(@PathVariable UUID id) {
        return bookOrderService.printOrder(id);
    }
    @GetMapping("/print-all")
    public ResponseEntity<InputStreamResource> printAllOrders() {
        return bookOrderService.printAllOrders();
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBookOrder(@PathVariable UUID id) {
        return bookOrderService.deleteBookOrder(id);
    }
}