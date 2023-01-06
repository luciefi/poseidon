package com.poseidon.poseidon.services;

import com.poseidon.poseidon.domain.BidList;
import com.poseidon.poseidon.domain.Rating;
import com.poseidon.poseidon.exceptions.RatingNotFoundException;
import com.poseidon.poseidon.repositories.RatingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RatingService implements IRatingService {

    @Autowired
    RatingRepository repository;

    @Override
    public List<Rating> findAll() {
        return repository.findAll();
    }

    @Override
    public void save(Rating rating) {
        repository.save(rating);
    }

    @Override
    public void update(Rating updatedRating, int id) {
        Rating oldRating = findById(id);
        repository.save(updateFields(oldRating, updatedRating));
    }

    @Override
    public Rating findById(Integer id) {
        return repository.findById(id).orElseThrow(RatingNotFoundException::new);
    }

    @Override
    public void delete(Integer id) {
        findById(id);
        repository.deleteById(id);
    }

    private Rating updateFields(Rating rating, Rating updatedRating) {
        rating.setMoodysRating(updatedRating.getMoodysRating());
        rating.setFitchRating(updatedRating.getFitchRating());
        rating.setSandPRating(updatedRating.getSandPRating());
        rating.setOrderNumber(updatedRating.getOrderNumber());
        return rating;
    }
}
