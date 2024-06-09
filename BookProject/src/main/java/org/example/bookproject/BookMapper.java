package org.example.bookproject;

import io.swagger.client.model.BookRequest;
import io.swagger.client.model.BookResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface BookMapper{
    @Mapping(target = "id", ignore = true)
    Book mapToBook(BookRequest request);
    BookResponse mapEntityToResponse(Book entity);
    @Mapping(target = "id", ignore = true)
    Book update(BookRequest request, @MappingTarget Book book);
}
