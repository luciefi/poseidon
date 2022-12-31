package com.poseidon.poseidon.services;

import com.poseidon.poseidon.domain.Trade;

import java.util.List;

public interface ITradeService {
    List<Trade> findAll();

    void save(Trade trade);

    void update(Trade trade, int id);

    Trade findById(Integer id);

    void delete(Integer id);
}
