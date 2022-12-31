package com.poseidon.poseidon.services;

import com.poseidon.poseidon.domain.BidList;
import com.poseidon.poseidon.exceptions.BidListNotFoundException;
import com.poseidon.poseidon.repositories.BidListRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BidListService implements IBidService {

    @Autowired
    BidListRepository repository;

    @Override
    public List<BidList> findAll() {
        return repository.findAll();
    }

    @Override
    public void save(BidList bidList) {
        repository.save(bidList);
    }

    @Override
    public void update(BidList updatedBidList, int id) {
        BidList oldBidList = findById(id);
        repository.save(updateFields(oldBidList, updatedBidList));
    }

    @Override
    public BidList findById(Integer id){
        return repository.findById(id).orElseThrow(BidListNotFoundException::new);
    }

    @Override
    public void delete(Integer id) {
        // TODO vérifier que le user peut delete ce bidlist ?
        repository.deleteById(id);
    }

    private BidList updateFields(BidList bidList, BidList updatedBidList) {
        bidList.setAccount(updatedBidList.getAccount());
        bidList.setType(updatedBidList.getType());
        bidList.setBidQuantity(updatedBidList.getBidQuantity());
        return bidList;
    }
}
