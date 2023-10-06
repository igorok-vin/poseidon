package com.nnk.springboot.service;

import com.nnk.springboot.domain.CurvePoint;
import com.nnk.springboot.exception.CurvePointNotFoundException;
import com.nnk.springboot.repositories.CurvePointRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CurvePointService {

    private final Logger logger = LoggerFactory.getLogger(CurvePointService.class);

    @Autowired
    CurvePointRepository curvePointRepository;

    /**
     * Get a list of all curvePoint
     * @return list of CurvePoint containing all curvePoints
     */
    public List<CurvePoint> getAllCurvePoint() {
        logger.info("Service: display curvePoints list");
        return curvePointRepository.findAll();
    }

    /**
     * Save a new CurvePoint
     * @param curvePoint
     * @return CurvePoint saved
     */
    public CurvePoint saveCurvePoint (CurvePoint curvePoint) {
        logger.info("Service: save new curvePoints to DB");
        return curvePointRepository.save(curvePoint);
    }

    /**
     * Get curvePoint by id.
     * @param id Integer that containing id of the CurvePoint
     * @return Instance of CurvePoint
     */
    public CurvePoint getCurvePointById(Integer id) {
        Optional<CurvePoint> curvePoint = curvePointRepository.findById(id);
        if(curvePoint.isPresent()) {
            logger.info("Service: CurvePoint with Id "+id+" was found");
            return curvePoint.get();
        } else {
            throw new CurvePointNotFoundException("CurvePoint not found");
        }
    }

    /**
     * Update existing curvePoint
     * @param curvePoint instance
     * @return the CurvePoint updated
     */
    public CurvePoint updateCurvePoint (CurvePoint curvePoint) {
        CurvePoint curvePointForUpdate = curvePointRepository.getById(curvePoint.getId());
        curvePointForUpdate.setCurveId(curvePoint.getCurveId());
        curvePointForUpdate.setTerm(curvePoint.getTerm());
        curvePointForUpdate.setValue(curvePoint.getValue());
        logger.info("Service: CurvePoint updated with ID: " + curvePoint.getId());
        return curvePointRepository.save(curvePointForUpdate);
    }

    /**
     * Delete a CurvePoint by id
     * @param id Integer that containing id of the CurvePoint
     */
    public void deleteCurvePoint(Integer id) {
        logger.info("Service: CurvePoint deleted with ID: " + id);
        curvePointRepository.deleteById(id);
    }
}
