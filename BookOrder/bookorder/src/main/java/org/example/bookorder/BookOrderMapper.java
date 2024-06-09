package org.example.bookorder;


import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface BookOrderMapper {
    @Mapping(target = "id", ignore = true)

    BookOrder mapToOrderBook(BookOrderRequest request);

    BookOrderResponse mapEntityToResponse(BookOrder entity);
    @Mapping(target = "id", ignore = true)
    BookOrder update(BookOrderRequest request, @MappingTarget BookOrder bookOrder);
}
