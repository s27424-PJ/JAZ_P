package org.example.bookproject.feignclient;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@FeignClient(name = "book-orders", url = "http://localhost:8080")
@RestController
public interface BookOrderFeignClient {


    @GetMapping("/book-orders")
    List<BookOrder> getAllOrders();
    @PostMapping("/book-orders")

    BookOrder createBookOrder(@RequestBody BookOrderRequest request);

}