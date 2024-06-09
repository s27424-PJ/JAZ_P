package org.example.bookorder;

import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;


import org.springframework.http.MediaType;


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;


import java.util.List;
import java.util.UUID;

import java.util.stream.Collectors;

@Service
public class BookOrderService {

    private final BookOrderRepository bookOrderRepository;
    private final BookOrderMapper bookOrderMapper;

    @Autowired
    public BookOrderService(BookOrderRepository bookOrderRepository, BookOrderMapper bookOrderMapper) {
        this.bookOrderRepository = bookOrderRepository;
        this.bookOrderMapper = bookOrderMapper;
    }
    public ResponseEntity<InputStreamResource> printAllOrders() {
        List<BookOrder> orders = bookOrderRepository.findAll();

        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            Document document = new Document();
            PdfWriter.getInstance(document, out);
            document.open();

            for (BookOrder order : orders) {
                document.add(new Paragraph("Order ID: " + order.getId()));
                document.add(new Paragraph("Book ID: " + order.getBookId()));
                document.add(new Paragraph("Quantity: " + order.getQuantity()));
                document.add(new Paragraph("\n"));
            }

            document.close();

            ByteArrayInputStream bis = new ByteArrayInputStream(out.toByteArray());

            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(new InputStreamResource(bis));
        } catch (Exception e) {
            throw new RuntimeException("Could not generate PDF", e);
        }
    }
    public ResponseEntity<InputStreamResource> printOrder(UUID id) {
        BookOrder bookOrder = bookOrderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book order not found with id: " + id));

        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            Document document = new Document();
            PdfWriter.getInstance(document, out);
            document.open();
            document.add(new Paragraph("Order ID: " + bookOrder.getId()));
            document.add(new Paragraph("Book ID: " + bookOrder.getBookId()));
            document.add(new Paragraph("Quantity: " + bookOrder.getQuantity()));
            document.close();

            ByteArrayInputStream bis = new ByteArrayInputStream(out.toByteArray());

            return ResponseEntity.ok()
                    .contentType(MediaType.APPLICATION_PDF)
                    .body(new InputStreamResource(bis));
        } catch (Exception e) {
            throw new RuntimeException("Could not generate PDF", e);
        }
    }

    public ResponseEntity<List<BookOrderResponse>> getAllBookOrders() {

        List<BookOrder> foundBookOrders = bookOrderRepository.findAll();
        List<BookOrderResponse> responses = foundBookOrders.stream()
                .map(bookOrderMapper::mapEntityToResponse)
                .collect(Collectors.toList());
        if (responses.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(responses, HttpStatus.OK);
    }

    public ResponseEntity<BookOrderResponse> getBookOrderById(UUID id) {
        BookOrder bookOrder = bookOrderRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Book order not found with id: " + id));
        BookOrderResponse response = bookOrderMapper.mapEntityToResponse(bookOrder);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    public ResponseEntity<BookOrderResponse> createBookOrder(@NotNull BookOrderRequest createRequest) {
        System.out.println(createRequest.getBookId());
        System.out.println(createRequest.getQuantity());
        BookOrder bookOrder = bookOrderMapper.mapToOrderBook(createRequest);
        System.out.println(bookOrder.getBookId());
        BookOrder savedBookOrder = bookOrderRepository.save(bookOrder);
        BookOrderResponse response = bookOrderMapper.mapEntityToResponse(savedBookOrder);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    public ResponseEntity<BookOrderResponse> updateBookOrder(UUID id, BookOrderRequest updateRequest) {
        BookOrder bookOrder = bookOrderRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Book order not found with id: " + id));
        bookOrder = bookOrderMapper.update(updateRequest, bookOrder);
        BookOrderResponse response = bookOrderMapper.mapEntityToResponse(bookOrderRepository.save(bookOrder));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    public ResponseEntity<Void> deleteBookOrder(UUID id) {
        bookOrderRepository.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}