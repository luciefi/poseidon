package com.poseidon.poseidon.services;

import com.poseidon.poseidon.domain.BidList;

import java.util.List;

public interface IBidService {
    List<BidList> findAll();

    void save(BidList bid);

    void update(BidList bidList, int id);

    BidList findById(Integer id);

    void delete(Integer id);
}
