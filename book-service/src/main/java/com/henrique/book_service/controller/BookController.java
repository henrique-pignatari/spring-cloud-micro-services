package com.henrique.book_service.controller;

import com.henrique.book_service.dto.Exchange;
import com.henrique.book_service.environment.InstanceInformationService;
import com.henrique.book_service.model.Book;
import com.henrique.book_service.proxy.ExchangeProxy;
import com.henrique.book_service.repository.BookRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Book Endpoint")
@RestController
@RequestMapping("book-service")
public class BookController {

    private final InstanceInformationService informationService;
    private final BookRepository bookRepository;
    private final ExchangeProxy exchangeProxy;

    public BookController(InstanceInformationService informationService, BookRepository bookRepository, ExchangeProxy exchangeProxy) {
        this.informationService = informationService;
        this.bookRepository = bookRepository;
        this.exchangeProxy = exchangeProxy;
    }

    @Operation(summary = "Find a specific book by its Id")
    @GetMapping(value = "/{id}/{currency}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Book findBook(@PathVariable("id") Long id, @PathVariable("currency") String currency) {
        String port = "PORT " + informationService.retrieveServerPort();
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("could not find book with id: " + id));
        Exchange exchange = exchangeProxy.getExchange(book.getPrice(),"USD", currency);
        book.setPrice(exchange.getConvertedValue());
        book.setCurrency(currency);
        book.setEnvironment("BOOK PORT: " + port + " EXCHANGE PORT: " + exchange.getEnvironment());
        return book;
    }
}
