package com.nnk.springboot.service;

import com.nnk.springboot.domain.CurvePoint;
import com.nnk.springboot.exception.CurvePointNotFoundException;
import com.nnk.springboot.repositories.CurvePointRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CurvePointServiceTest {

    @Mock
    private CurvePointRepository curvePointRepository;

    @InjectMocks
    CurvePointService curvePointService;

    @Test
    public void getAllCurvePoint() {
        CurvePoint  curvePoint1 = new CurvePoint();
        curvePoint1.setId(1);
        curvePoint1.setCurveId(1);
        curvePoint1.setValue(2.0);
        curvePoint1.setTerm(2.0);
        CurvePoint  curvePoint2 = new CurvePoint();
        curvePoint2.setId(1);
        curvePoint2.setCurveId(2);
        curvePoint2.setValue(4.0);
        curvePoint2.setTerm(4.0);
        List<CurvePoint> curvePointList = new ArrayList<>();
        curvePointList.add(curvePoint1);
        curvePointList.add(curvePoint2);

        when(curvePointRepository.findAll()).thenReturn(curvePointList);

        List<CurvePoint> result = curvePointService.getAllCurvePoint();

        assertThat(result.size()).isEqualTo(2);
        assertThat(result).isEqualTo(curvePointList);
        verify(curvePointRepository).findAll();
    }

    @Test
    public void saveCurvePoint() {
        CurvePoint curvePoint = new CurvePoint();
        curvePoint.setId(1);
        curvePoint.setCurveId(1);
        curvePoint.setValue(2.0);
        curvePoint.setTerm(2.0);

        when(curvePointRepository.save(any(CurvePoint.class))).thenReturn(curvePoint);

        CurvePoint result = curvePointService.saveCurvePoint(curvePoint);

        assertEquals(result, curvePoint);
        assertThat(1).isEqualTo(result.getId());
        assertThat(1).isEqualTo(result.getCurveId());
        assertThat(2.0).isEqualTo(result.getValue());
        assertThat(2.0).isEqualTo(result.getTerm());
    }

    @Test
    public void getCurvePointById() {
        CurvePoint curvePoint = new CurvePoint();
        curvePoint.setId(1);
        curvePoint.setCurveId(1);
        curvePoint.setValue(2.0);
        curvePoint.setTerm(2.0);

        when(curvePointRepository.findById(anyInt())).thenReturn(Optional.of(curvePoint));

        CurvePoint result = curvePointService.getCurvePointById(1);

        assertEquals(result, curvePoint);
        assertThat(1).isEqualTo(result.getId());
        assertThat(1).isEqualTo(result.getCurveId());
        assertThat(2.0).isEqualTo(result.getValue());
        assertThat(2.0).isEqualTo(result.getTerm());
        assertThat(1).isEqualTo(result.getCurveId());
    }

    @Test
    public void getCurvePointByIdDoTrowCurvePointNotFoundException() {
        when(curvePointRepository.findById(anyInt())).thenReturn(Optional.empty());
        assertThrows(CurvePointNotFoundException.class,()->curvePointService.getCurvePointById(1));
        verify(curvePointRepository).findById(isA(Integer.class));
    }

    @Test
    public void updateCurvePoint() {
        CurvePoint curvePointBeforeUpdate = new CurvePoint();
        curvePointBeforeUpdate.setId(1);
        curvePointBeforeUpdate.setCurveId(1);
        curvePointBeforeUpdate.setValue(2.0);
        curvePointBeforeUpdate.setTerm(2.0);
        CurvePoint curvePointAfterUpdate = new CurvePoint();
        curvePointAfterUpdate.setId(1);
        curvePointAfterUpdate.setCurveId(2);
        curvePointAfterUpdate.setValue(4.0);
        curvePointAfterUpdate.setTerm(6.0);

        when(curvePointRepository.getById(anyInt())).thenReturn(curvePointBeforeUpdate);
        when(curvePointRepository.save(any(CurvePoint.class))).thenReturn(curvePointAfterUpdate);

        CurvePoint result = curvePointService.updateCurvePoint(curvePointBeforeUpdate);

        assertEquals(result, curvePointAfterUpdate);
        assertThat(1).isEqualTo(result.getId());
        assertThat(2).isEqualTo(result.getCurveId());
        assertThat(4.0).isEqualTo(result.getValue());
        assertThat(6.0).isEqualTo(result.getTerm());
    }

    @Test
    public void deleteCurvePoint() {
        CurvePoint curvePoint = new CurvePoint();
        curvePoint.setId(1);
        curvePoint.setCurveId(1);
        curvePoint.setValue(2.0);
        curvePoint.setTerm(2.0);

        curvePointService.deleteCurvePoint(curvePoint.getId());

        verify(curvePointRepository).deleteById(1);
    }
}