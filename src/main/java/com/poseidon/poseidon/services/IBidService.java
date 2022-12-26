package com.nnk.springboot.services;

import com.nnk.springboot.domain.BidList;

import java.util.List;

public interface IBidService {
    List<BidList> findAll();

    void save(BidList bid);

    void update(BidList bidList);

    BidList findById(Integer id);

    void delete(Integer id);
}
