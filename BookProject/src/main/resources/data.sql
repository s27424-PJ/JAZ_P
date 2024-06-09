CREATE TABLE IF NOT EXISTS AUTHOR (
                                      id INT PRIMARY KEY,
                                      name VARCHAR(255) NOT NULL
    );
INSERT INTO AUTHOR (id, name)
VALUES
    ('1', 'John Smith'),
    ('2', 'Alice Johnson');

CREATE TABLE IF NOT EXISTS BOOK (
                                    id int PRIMARY KEY,
                                    nazwa VARCHAR(255) NOT NULL,
    gatunek VARCHAR(255) NOT NULL,
    cena DOUBLE NOT NULL,
    iloscStron INT NOT NULL,
    licznikOdwiedzin INT NOT NULL,
    sztuki INT NOT NULL,
    author_id INT,
    FOREIGN KEY (author_id) REFERENCES AUTHOR(id)
    );



INSERT INTO BOOK (ID, NAZWA, GATUNEK, CENA, ILOSC_STRON, LICZNIK_ODWIEDZIN, SZTUKI, AUTHOR_ID)
VALUES
    ('1', 'The Catcher in the Rye', 'Novel', 29.99, 277, 9, 20, '1'),
    ('2', 'To Kill a Mockingbird', 'Novel', 24.99, 336,9, 50, '2'),
    ('3', '1984', 'Dystopian', 19.99, 328, 20, 0, '1'),
    ('4', 'Pride and Prejudice', 'Romance', 22.99, 279, 25, 60, '1');
