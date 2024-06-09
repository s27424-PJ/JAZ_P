package org.example.bookproject.feignclient;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class BookOrderFeignController {
    private final BookOrderFeignClient bookOrderFeignClient;
    private final BookOrderMapper bookOrderMapper;

    @Autowired
    public BookOrderFeignController(BookOrderFeignClient bookOrderFeignClient, BookOrderMapper bookOrderMapper) {
        this.bookOrderFeignClient = bookOrderFeignClient;
        this.bookOrderMapper = bookOrderMapper;
    }

    @PostMapping("/test")
    public BookOrder createBookOrder(@RequestBody BookOrderRequest request) {
        return bookOrderFeignClient.createBookOrder(request);
    }

    @GetMapping("/order-report")
    public ResponseEntity<List<BookOrderResponse>> getOrderReport() {
        List<BookOrder> orders = bookOrderFeignClient.getAllOrders();
        List<BookOrderResponse> orderResponses = orders.stream()
                .map(bookOrderMapper::mapToResponse)
                .collect(Collectors.toList());
        return new ResponseEntity<>(orderResponses, HttpStatus.OK);
    }
}
