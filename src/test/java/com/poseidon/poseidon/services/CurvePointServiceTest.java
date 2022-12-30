package com.poseidon.poseidon.services;

import com.poseidon.poseidon.domain.CurvePoint;
import com.poseidon.poseidon.exceptions.CurvePointNotFoundException;
import com.poseidon.poseidon.repositories.CurvePointRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CurvePointServiceTest {

    final int CURVE_POINT_ID = 1;
    final int CURVE_POINT_CURVE_ID = 1;
    final Double CURVE_POINT_TERM = 1d;
    final Double CURVE_POINT_VALUE = 2d;

    @InjectMocks
    private CurvePointService service;

    @Mock
    private CurvePointRepository repository;

    @Test
    public void testFindAll() {
        // Act
        service.findAll();

        // Assert
        verify(repository, times(1)).findAll();
    }

    @Test
    public void testSave() {
        // Arrange
        CurvePoint newCurvePoint = new CurvePoint();
        newCurvePoint.setCurveId(CURVE_POINT_CURVE_ID);
        newCurvePoint.setTerm(CURVE_POINT_TERM);
        newCurvePoint.setValue(CURVE_POINT_VALUE);

        // Act
        service.save(newCurvePoint);

        // Assert
        ArgumentCaptor<CurvePoint> curvePoint = ArgumentCaptor.forClass(CurvePoint.class);
        verify(repository, times(1)).save(curvePoint.capture());
        assertEquals(CURVE_POINT_CURVE_ID, curvePoint.getValue().getCurveId(), 0);
        assertEquals(CURVE_POINT_TERM, curvePoint.getValue().getTerm());
        assertEquals(CURVE_POINT_VALUE, curvePoint.getValue().getValue());
    }

    @Test
    public void testUpdate() {
        // Arrange
        CurvePoint CurvePointFromRepo = new CurvePoint();
        CurvePointFromRepo.setId(CURVE_POINT_ID);
        when(repository.findById(CURVE_POINT_ID)).thenReturn(Optional.of(CurvePointFromRepo));
        CurvePoint updatedCurvePoint = new CurvePoint();
        updatedCurvePoint.setCurveId(CURVE_POINT_CURVE_ID);
        updatedCurvePoint.setTerm(CURVE_POINT_TERM);
        updatedCurvePoint.setValue(CURVE_POINT_VALUE);

        // Act
        service.update(updatedCurvePoint, CURVE_POINT_ID);

        // Assert
        ArgumentCaptor<CurvePoint> curvePoint = ArgumentCaptor.forClass(CurvePoint.class);
        verify(repository, times(1)).save(curvePoint.capture());
        assertEquals(CURVE_POINT_ID, curvePoint.getValue().getId(), 0);
        assertEquals(CURVE_POINT_CURVE_ID, curvePoint.getValue().getCurveId(), 0);
        assertEquals(CURVE_POINT_TERM, curvePoint.getValue().getTerm());
        assertEquals(CURVE_POINT_VALUE, curvePoint.getValue().getValue());
        verify(repository, times(1)).findById(CURVE_POINT_ID);
    }

    @Test
    public void testFindById() {
        // Arrange
        CurvePoint curvePointFromRepo = new CurvePoint();
        curvePointFromRepo.setId(CURVE_POINT_ID);
        when(repository.findById(CURVE_POINT_ID)).thenReturn(Optional.of(curvePointFromRepo));

        // Act
        CurvePoint curvePoint = service.findById(CURVE_POINT_ID);

        // Assert
        assertEquals(CURVE_POINT_ID, curvePoint.getId(), 0);
        verify(repository, times(1)).findById(CURVE_POINT_ID);
    }

    @Test
    public void testFindByIdNotFound() {
        // Arrange
        when(repository.findById(CURVE_POINT_ID)).thenReturn(Optional.empty());

        // Act
        assertThrows(CurvePointNotFoundException.class, () -> service.findById(CURVE_POINT_ID));

        // Assert
        verify(repository, times(1)).findById(CURVE_POINT_ID);
    }

    @Test
    public void testDelete() {
        // Act
        service.delete(CURVE_POINT_ID);

        // Assert
        verify(repository, times(1)).deleteById(CURVE_POINT_ID);
    }
}