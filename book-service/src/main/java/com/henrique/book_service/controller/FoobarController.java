package com.henrique.book_service.controller;

import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "FooBar Endpoint")
@RestController
@RequestMapping("book-service")
public class FoobarController {

    private final Logger logger = LoggerFactory.getLogger(FoobarController.class);

    @GetMapping("/foo-bar")
    /*
        @Retry(name = "foo-bar") //retries amount of times configured on yml file
        @Retry(name = "foo-bar", fallbackMethod = "fallbackMethod") // same as before but has a fallback method
        @CircuitBreaker(name = "default", fallbackMethod = "fallbackMethod")
        @RateLimiter(name = "default")
    */
    @Bulkhead(name = "default")
    public String fooBar() {
        logger.info("Request to foo-bar received!");
//        var response = new RestTemplate()
//                .getForEntity("http://localhost:8080/foo-bar", String.class);
//        return response.getBody();
        return "FOO BAR!!";
    }

    public String fallbackMethod(Exception ex) {
        return "fallbackMethod foo-bar";
    }
}
