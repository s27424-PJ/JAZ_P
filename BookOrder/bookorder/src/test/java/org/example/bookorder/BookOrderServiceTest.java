package org.example.bookorder;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class BookOrderServiceTest {

    @Mock
    private BookOrderRepository bookOrderRepository;

    @Mock
    private BookOrderMapper bookOrderMapper;

    @InjectMocks
    private BookOrderService bookOrderService;

    private BookOrder bookOrder;
    private BookOrderRequest bookOrderRequest;
    private BookOrderResponse bookOrderResponse;

    @BeforeEach
    public void setUp() {
        bookOrder = new BookOrder();
        bookOrder.setId(UUID.randomUUID());
        bookOrder.setBookId(1);
        bookOrder.setQuantity(10);

        bookOrderRequest = new BookOrderRequest();
        bookOrderRequest.setBookId(1);
        bookOrderRequest.setQuantity(10);

        bookOrderResponse = new BookOrderResponse();
        bookOrderResponse.setId(bookOrder.getId());
        bookOrderResponse.setBookId(1);
        bookOrderResponse.setQuantity(10);
    }

    @Test
    public void testGetAllBookOrders() {
        List<BookOrder> bookOrders = new ArrayList<>();
        bookOrders.add(bookOrder);

        when(bookOrderRepository.findAll()).thenReturn(bookOrders);
        when(bookOrderMapper.mapEntityToResponse(any(BookOrder.class))).thenReturn(bookOrderResponse);

        ResponseEntity<List<BookOrderResponse>> responseEntity = bookOrderService.getAllBookOrders();
        List<BookOrderResponse> responses = responseEntity.getBody();

        assertNotNull(responses);
        assertEquals(1, responses.size());
        assertEquals(bookOrderResponse, responses.get(0));

        verify(bookOrderRepository, times(1)).findAll();
        verify(bookOrderMapper, times(1)).mapEntityToResponse(any(BookOrder.class));
    }

    @Test
    public void testGetBookOrderById() {
        when(bookOrderRepository.findById(any(UUID.class))).thenReturn(Optional.of(bookOrder));
        when(bookOrderMapper.mapEntityToResponse(any(BookOrder.class))).thenReturn(bookOrderResponse);

        ResponseEntity<BookOrderResponse> responseEntity = bookOrderService.getBookOrderById(bookOrder.getId());
        BookOrderResponse response = responseEntity.getBody();

        assertNotNull(response);
        assertEquals(bookOrderResponse, response);

        verify(bookOrderRepository, times(1)).findById(eq(bookOrder.getId()));
        verify(bookOrderMapper, times(1)).mapEntityToResponse(any(BookOrder.class));
    }

    @Test
    public void testCreateBookOrder() {
        when(bookOrderMapper.mapToOrderBook(any(BookOrderRequest.class))).thenReturn(bookOrder);
        when(bookOrderRepository.save(any(BookOrder.class))).thenReturn(bookOrder);
        when(bookOrderMapper.mapEntityToResponse(any(BookOrder.class))).thenReturn(bookOrderResponse);

        ResponseEntity<BookOrderResponse> responseEntity = bookOrderService.createBookOrder(bookOrderRequest);
        BookOrderResponse response = responseEntity.getBody();

        assertNotNull(response);
        assertEquals(bookOrderResponse, response);

        verify(bookOrderMapper, times(1)).mapToOrderBook(any(BookOrderRequest.class));
        verify(bookOrderRepository, times(1)).save(any(BookOrder.class));
        verify(bookOrderMapper, times(1)).mapEntityToResponse(any(BookOrder.class));
    }

    @Test
    public void testUpdateBookOrder() {
        when(bookOrderRepository.findById(any(UUID.class))).thenReturn(Optional.of(bookOrder));
        when(bookOrderMapper.update(any(BookOrderRequest.class), any(BookOrder.class))).thenReturn(bookOrder);
        when(bookOrderRepository.save(any(BookOrder.class))).thenReturn(bookOrder);
        when(bookOrderMapper.mapEntityToResponse(any(BookOrder.class))).thenReturn(bookOrderResponse);

        ResponseEntity<BookOrderResponse> responseEntity = bookOrderService.updateBookOrder(bookOrder.getId(), bookOrderRequest);
        BookOrderResponse response = responseEntity.getBody();

        assertNotNull(response);
        assertEquals(bookOrderResponse, response);

        verify(bookOrderRepository, times(1)).findById(eq(bookOrder.getId()));
        verify(bookOrderMapper, times(1)).update(any(BookOrderRequest.class), any(BookOrder.class));
        verify(bookOrderRepository, times(1)).save(any(BookOrder.class));
        verify(bookOrderMapper, times(1)).mapEntityToResponse(any(BookOrder.class));
    }

    @Test
    public void testDeleteBookOrder() {
        doNothing().when(bookOrderRepository).deleteById(any(UUID.class));

        ResponseEntity<Void> responseEntity = bookOrderService.deleteBookOrder(bookOrder.getId());

        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        verify(bookOrderRepository, times(1)).deleteById(eq(bookOrder.getId()));
    }

    @Test
    public void testPrintOrder() {
        when(bookOrderRepository.findById(any(UUID.class))).thenReturn(Optional.of(bookOrder));

        ResponseEntity<InputStreamResource> responseEntity = bookOrderService.printOrder(bookOrder.getId());

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        verify(bookOrderRepository, times(1)).findById(eq(bookOrder.getId()));
    }

    @Test
    public void testPrintAllOrders() {
        List<BookOrder> bookOrders = new ArrayList<>();
        bookOrders.add(bookOrder);

        when(bookOrderRepository.findAll()).thenReturn(bookOrders);

        ResponseEntity<InputStreamResource> responseEntity = bookOrderService.printAllOrders();

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());

        verify(bookOrderRepository, times(1)).findAll();
    }
}
