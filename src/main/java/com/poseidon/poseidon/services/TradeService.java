package com.poseidon.poseidon.services;

import com.poseidon.poseidon.domain.Trade;
import com.poseidon.poseidon.exceptions.TradeNotFoundException;
import com.poseidon.poseidon.repositories.TradeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TradeService implements ITradeService {

    @Autowired
    TradeRepository repository;

    @Override
    public List<Trade> findAll() {
        return repository.findAll();
    }

    @Override
    public void save(Trade trade) {
        repository.save(trade);
    }

    @Override
    public void update(Trade updatedTrade, int id) {
        Trade oldTrade = findById(id);
        repository.save(updateFields(oldTrade, updatedTrade));
    }

    @Override
    public Trade findById(Integer id) {
        return repository.findById(id).orElseThrow(TradeNotFoundException::new);
    }

    @Override
    public void delete(Integer id) {
        findById(id);
        repository.deleteById(id);
    }

    private Trade updateFields(Trade trade, Trade updatedTrade) {
        trade.setAccount(updatedTrade.getAccount());
        trade.setType(updatedTrade.getType());
        trade.setBuyQuantity(updatedTrade.getBuyQuantity());
        return trade;
    }
}
