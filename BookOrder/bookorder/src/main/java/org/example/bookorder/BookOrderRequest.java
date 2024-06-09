package org.example.bookorder;


import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class BookOrderRequest {

    private int bookId;
    private int quantity;

}