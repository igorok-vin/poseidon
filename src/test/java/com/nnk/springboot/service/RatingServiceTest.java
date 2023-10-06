package com.nnk.springboot.service;

import com.nnk.springboot.domain.Rating;
import com.nnk.springboot.exception.CurvePointNotFoundException;
import com.nnk.springboot.exception.RatingNotFoundException;
import com.nnk.springboot.repositories.RatingRepository;
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
class RatingServiceTest {

    @Mock
    private RatingRepository ratingRepository;

    @InjectMocks
    RatingService ratingService;

    @Test
    public void getAllRating() {
        Rating rating1 = new Rating();
            rating1.setId(1);
            rating1.setMoodysRating("moodys1");
            rating1.setSandPRating("sand1");
            rating1.setFitchRating("fitch1");
            rating1.setOrderNumber(11);
        Rating rating2 = new Rating();
            rating2.setId(2);
            rating2.setMoodysRating("moodys2");
            rating2.setSandPRating("sand2");
            rating2.setFitchRating("fitch2");
            rating2.setOrderNumber(22);
        List<Rating> ratingList = new ArrayList<>();
            ratingList.add(rating1);
            ratingList.add(rating2);
        when(ratingRepository.findAll()).thenReturn(ratingList);

        List<Rating> result = ratingService.getAllRating();

        assertThat(result.size()).isEqualTo(2);
        assertThat(result).isEqualTo(ratingList);
        verify(ratingRepository).findAll();
    }

    @Test
    public void saveRating() {
        Rating rating = new Rating();
            rating.setId(1);
            rating.setMoodysRating("moodys1");
            rating.setSandPRating("sand1");
            rating.setFitchRating("fitch1");
            rating.setOrderNumber(11);
        when(ratingRepository.save(any(Rating.class))).thenReturn(rating);

        Rating result = ratingService.saveRating(rating);

        assertEquals(result, rating);
        assertThat(1).isEqualTo(result.getId());
        assertThat("moodys1").isEqualTo(result.getMoodysRating());
        assertThat("sand1").isEqualTo(result.getSandPRating());
        assertThat("fitch1").isEqualTo(result.getFitchRating());
        assertThat(11).isEqualTo(result.getOrderNumber());
    }

    @Test
    public void getRatingById() {
        Rating rating = new Rating();
            rating.setId(1);
            rating.setMoodysRating("moodys1");
            rating.setSandPRating("sand1");
            rating.setFitchRating("fitch1");
            rating.setOrderNumber(11);
        when(ratingRepository.findById(anyInt())).thenReturn(Optional.of(rating));

        Rating result = ratingService.getRatingById(1);

        assertEquals(result, rating);
        assertThat(1).isEqualTo(result.getId());
        assertThat("moodys1").isEqualTo(result.getMoodysRating());
        assertThat("sand1").isEqualTo(result.getSandPRating());
        assertThat("fitch1").isEqualTo(result.getFitchRating());
        assertThat(11).isEqualTo(result.getOrderNumber());
    }

    @Test
    public void getRatingByIdDoTrowRatingNotFoundException() {
        when(ratingRepository.findById(anyInt())).thenReturn(Optional.empty());
        assertThrows(RatingNotFoundException.class,()-> ratingService.getRatingById(1));
        verify(ratingRepository).findById(isA(Integer.class));
    }

    @Test
    public void updateRating() {
        Rating ratingBeforeUpdate = new Rating();
            ratingBeforeUpdate.setId(1);
            ratingBeforeUpdate.setMoodysRating("moodys1");
            ratingBeforeUpdate.setSandPRating("sand1");
            ratingBeforeUpdate.setFitchRating("fitch1");
            ratingBeforeUpdate.setOrderNumber(11);
        Rating ratingAfterUpdate = new Rating();
            ratingAfterUpdate.setId(2);
            ratingAfterUpdate.setMoodysRating("moodys2");
            ratingAfterUpdate.setSandPRating("sand2");
            ratingAfterUpdate.setFitchRating("fitch2");
            ratingAfterUpdate.setOrderNumber(22);

        when(ratingRepository.getById(anyInt())).thenReturn(ratingBeforeUpdate);
        when(ratingRepository.save(any(Rating.class))).thenReturn(ratingAfterUpdate);

        Rating result = ratingService.updateRating(ratingBeforeUpdate);

        assertEquals(result, ratingAfterUpdate);
        assertThat(2).isEqualTo(result.getId());
        assertThat("moodys2").isEqualTo(result.getMoodysRating());
        assertThat("sand2").isEqualTo(result.getSandPRating());
        assertThat("fitch2").isEqualTo(result.getFitchRating());
        assertThat(22).isEqualTo(result.getOrderNumber());
    }

    @Test
    public void deleteRating() {
        Rating rating = new Rating();
        rating.setId(1);
        rating.setMoodysRating("moodys1");
        rating.setSandPRating("sand1");
        rating.setFitchRating("fitch1");
        rating.setOrderNumber(11);

        ratingService.deleteRating(rating.getId());

        verify(ratingRepository).deleteById(1);
    }
}