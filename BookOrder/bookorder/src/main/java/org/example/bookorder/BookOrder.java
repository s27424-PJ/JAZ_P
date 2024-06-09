package org.example.bookorder;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Setter
@Getter
@Data
@Entity
public class BookOrder {
    @Id
    @GeneratedValue
    private UUID id;
    private int bookId;
    private int quantity;
}