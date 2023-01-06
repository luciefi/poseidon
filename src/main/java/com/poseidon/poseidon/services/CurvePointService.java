package com.poseidon.poseidon.services;

import com.poseidon.poseidon.domain.CurvePoint;
import com.poseidon.poseidon.exceptions.CurvePointNotFoundException;
import com.poseidon.poseidon.repositories.CurvePointRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CurvePointService implements ICurvePointService {

    @Autowired
    CurvePointRepository repository;

    @Override
    public List<CurvePoint> findAll() {
        return repository.findAll();
    }

    @Override
    public void save(CurvePoint curvePoint) {
        repository.save(curvePoint);
    }

    @Override
    public void update(CurvePoint updatedCurvePoint, int id) {
        CurvePoint oldCurvePoint = findById(id);
        repository.save(updateFields(oldCurvePoint, updatedCurvePoint));
    }

    @Override
    public CurvePoint findById(Integer id) {
        return repository.findById(id).orElseThrow(CurvePointNotFoundException::new);
    }

    @Override
    public void delete(Integer id) {
        findById(id);
        repository.deleteById(id);
    }

    private CurvePoint updateFields(CurvePoint curvePoint, CurvePoint updatedCurvePoint) {
        curvePoint.setCurveId(updatedCurvePoint.getCurveId());
        curvePoint.setTerm(updatedCurvePoint.getTerm());
        curvePoint.setValue(updatedCurvePoint.getValue());
        return curvePoint;
    }
}
