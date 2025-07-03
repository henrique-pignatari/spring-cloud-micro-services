package com.henrique.book_service.proxy;

import com.henrique.book_service.dto.Exchange;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "exchange-service")
public interface ExchangeProxy {
    @GetMapping(value = "/exchange-service/{amount}/{from}/{to}")
    Exchange getExchange(@PathVariable("amount") Double amount, @PathVariable("from") String from, @PathVariable("to") String to);
}
