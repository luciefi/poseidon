package com.nnk.springboot.services;

import com.nnk.springboot.domain.BidList;
import com.nnk.springboot.exceptions.BidListNotFoundException;
import com.nnk.springboot.repositories.BidListRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BidListService implements IBidService {

    @Autowired
    BidListRepository repository;

    @Override
    public List<BidList> findAll() {
        List<BidList> bidLists = repository.findAll();
        return bidLists;
    }

    @Override
    public void save(BidList bid) {

    }

    @Override
    public void update(BidList bidList) {

    }

    @Override
    public BidList findById(Integer id){
        return repository.findById(id).orElseThrow(BidListNotFoundException::new);
    }

    @Override
    public void delete(Integer id) {
        repository.deleteById(id);
    }
}
