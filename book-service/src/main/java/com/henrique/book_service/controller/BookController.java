package com.henrique.book_service.controller;

import com.henrique.book_service.dto.Exchange;
import com.henrique.book_service.environment.InstanceInformationService;
import com.henrique.book_service.model.Book;
import com.henrique.book_service.repository.BookRepository;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Date;
import java.util.HashMap;

@RestController
@RequestMapping("book-service")
public class BookController {

    private final InstanceInformationService informationService;
    private final BookRepository bookRepository;

    public BookController(InstanceInformationService informationService, BookRepository bookRepository) {
        this.informationService = informationService;
        this.bookRepository = bookRepository;
    }

    @GetMapping(value = "/{id}/{currency}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Book findBook(@PathVariable("id") Long id, @PathVariable("currency") String currency) {
        String port = "PORT " + informationService.retrieveServerPort();
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("could not find book with id: " + id));
        HashMap<String, String> params = new HashMap<>();
        params.put("amount", book.getPrice().toString());
        params.put("from", "USD");
        params.put("to", currency);
        var response = new RestTemplate()
                .getForEntity("http://localhost:8000/exchange-service/{amount}/{from}/{to}", Exchange.class, params);
        Exchange exchange = response.getBody();
        book.setPrice(exchange.getConvertedValue());
        book.setCurrency(currency);
        book.setEnvironment(port);
        return book;
    }
}
