package org.example.bookproject.feignclient;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter

public class BookOrderResponse {
    private UUID id;
    private int bookId;
    private int quantity;
}