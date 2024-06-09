package org.example.bookproject.feignclient;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Entity
@Getter
@Setter
public class BookOrder {
    @Id
    @GeneratedValue
    private UUID id;
    private int bookId;
    private int quantity;

}