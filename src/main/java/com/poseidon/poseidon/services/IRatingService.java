package com.poseidon.poseidon.services;

import com.poseidon.poseidon.domain.Rating;

import java.util.List;

public interface IRatingService {
    List<Rating> findAll();

    void save(Rating rating);

    void update(Rating rating, int id);

    Rating findById(Integer id);

    void delete(Integer id);
}
