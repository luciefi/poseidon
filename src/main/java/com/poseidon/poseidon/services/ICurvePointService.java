package com.poseidon.poseidon.services;

import com.poseidon.poseidon.domain.BidList;
import com.poseidon.poseidon.domain.CurvePoint;

import java.util.List;

public interface ICurvePointService {
    List<CurvePoint> findAll();

    void save(CurvePoint curvePoint);

    void update(CurvePoint curvePoint, int id);

    CurvePoint findById(Integer id);

    void delete(Integer id);
}
