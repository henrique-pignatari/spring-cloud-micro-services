package com.henrique.exchange_service.repository;

import com.henrique.exchange_service.model.Exchange;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ExchangeRepository extends JpaRepository<Exchange, Long> {
    Optional<Exchange> findByFromAndTo(String from, String to);
}
