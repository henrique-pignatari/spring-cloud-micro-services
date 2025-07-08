package com.henrique.exchange_service.controller;

import com.henrique.exchange_service.environment.InstanceInformationService;
import com.henrique.exchange_service.model.Exchange;
import com.henrique.exchange_service.repository.ExchangeRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@Tag(name = "Book Endpoint")
@RestController
@RequestMapping("exchange-service")
public class ExchangeController {

    private final Logger logger = LoggerFactory.getLogger(ExchangeController.class);

    private final InstanceInformationService informationService;
    private final ExchangeRepository exchangeRepository;

    public ExchangeController(InstanceInformationService informationService, ExchangeRepository exchangeRepository) {
        this.informationService = informationService;
        this.exchangeRepository = exchangeRepository;
    }

    @Operation(summary = "Get an exchange from amount of currency")
    @GetMapping(value = "/{amount}/{from}/{to}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Exchange getExchange(@PathVariable("amount") BigDecimal amount, @PathVariable("from") String from, @PathVariable("to") String to) {
        logger.info("getExchange is called with -> {}, {} and {}", amount, from, to);
        Exchange exchange = exchangeRepository.findByFromAndTo(from, to)
                .orElseThrow(() -> new RuntimeException("could not find an exchange from: " + from + "to: " + to));
        BigDecimal conversionFactor = exchange.getConversionFactor();
        BigDecimal convertedValue =  conversionFactor.multiply(amount);
        exchange.setConvertedValue(convertedValue);
        exchange.setEnvironment("PORT " + informationService.retrieveServerPort());
        return exchange;
    }
}
