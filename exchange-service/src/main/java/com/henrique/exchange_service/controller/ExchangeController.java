package com.henrique.exchange_service.controller;

import com.henrique.exchange_service.environment.InstanceInformationService;
import com.henrique.exchange_service.model.Exchange;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
@RequestMapping("exchange-service")
public class ExchangeController {

    private final InstanceInformationService informationService;

    public ExchangeController(InstanceInformationService informationService) {
        this.informationService = informationService;
    }

    @GetMapping(value = "/{amount}/{from}/{to}", produces = MediaType.APPLICATION_JSON_VALUE)
    public Exchange getExchange(@PathVariable("amount") BigDecimal amount, @PathVariable("from") String from, @PathVariable("to") String to) {
        return new Exchange(1L, from, to, amount, BigDecimal.ONE, "PORT " + informationService.retrieveServerPort());
    }
}
