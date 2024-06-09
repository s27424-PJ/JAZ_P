package org.example.bookproject.feignclient;

import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public class BookOrderMapper {
    public BookOrderResponse mapToResponse(BookOrder bookOrder) {
        BookOrderResponse response = new BookOrderResponse();
        response.setId(bookOrder.getId());
        response.setBookId(bookOrder.getBookId());
        response.setQuantity(bookOrder.getQuantity());
       
        return response;
    }
}
