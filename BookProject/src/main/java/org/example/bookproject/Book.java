package org.example.bookproject;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Setter
@Getter
@Data
@Entity
public class Book {
    @Id
    @GeneratedValue
    private int id;
    private String nazwa;
    @ValidateBookType
    private String gatunek;
    private double cena;
    private int iloscStron;
    private int licznikOdwiedzin;
    private int sztuki;

    private int authorId;
}
