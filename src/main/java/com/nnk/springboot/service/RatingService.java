package com.nnk.springboot.service;

import com.nnk.springboot.domain.Rating;
import com.nnk.springboot.exception.RatingNotFoundException;
import com.nnk.springboot.repositories.RatingRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RatingService {
    private final Logger logger = LoggerFactory.getLogger(RatingService.class);
    @Autowired
    RatingRepository ratingRepository;

    /**
     * Get a list of all rating
     * @return list of Rating containing all ratings
     */
    public List<Rating> getAllRating() {
        logger.info("Service: display rating list");
        return ratingRepository.findAll();
    }

    /**
     * Save a new Rating
     * @param rating
     * @return Rating saved
     */
    public Rating saveRating (Rating rating) {
        logger.info("Service: save new rating to DB");
        return ratingRepository.save(rating);
    }

    /**
     * Get rating by id.
     * @param id Rating that containing id of the Rating
     * @return Instance of Rating
     */
    public Rating getRatingById(Integer id) {
        Optional<Rating> rating = ratingRepository.findById(id);
        if(rating.isPresent()){
            logger.info("Service: Rating with Id "+id+" was found");
            return rating.get();
        }else {
            throw new RatingNotFoundException("CurvePoint not found");
        }
    }

    /**
     * Update existing rating
     * @param rating instance
     * @return the Rating updated
     */
    public Rating updateRating(Rating rating) {
        Rating ratingForUpdate = ratingRepository.getById(rating.getId());
        ratingForUpdate.setFitchRating(rating.getFitchRating());
        ratingForUpdate.setMoodysRating(rating.getMoodysRating());
        ratingForUpdate.setSandPRating(ratingForUpdate.getSandPRating());
        logger.info("Service: Rating updated with ID: " + rating.getId());
        return ratingRepository.save(ratingForUpdate);
    }

    /**
     * Delete a Rating by id
     * @param id Integer that containing id of the Rating
     */
    public void deleteRating(Integer id) {
        logger.info("Service: Rating deleted with ID: " + id);
        ratingRepository.deleteById(id);
    }
}
