package com.poseidon.poseidon.services;

import com.poseidon.poseidon.domain.Rating;
import com.poseidon.poseidon.exceptions.RatingNotFoundException;
import com.poseidon.poseidon.repositories.RatingRepository;
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
public class RatingServiceTest {

    final Integer RATING_ID = 1;
    final String RATING_MOODYS = "moodys";
    final String RATING_SANDP = "sandp";
    final String RATING_FITCH = "fitch";
    final Integer RATING_ORDER_NUMBER = 2;

    @InjectMocks
    private RatingService service;

    @Mock
    private RatingRepository repository;


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
        Rating newRating = new Rating();
        newRating.setMoodysRating(RATING_MOODYS);
        newRating.setSandPRating(RATING_SANDP);
        newRating.setFitchRating(RATING_FITCH);
        newRating.setOrderNumber(RATING_ORDER_NUMBER);

        // Act
        service.save(newRating);

        // Assert
        ArgumentCaptor<Rating> rating = ArgumentCaptor.forClass(Rating.class);
        verify(repository, times(1)).save(rating.capture());
        assertEquals(RATING_MOODYS, rating.getValue().getMoodysRating());
        assertEquals(RATING_SANDP, rating.getValue().getSandPRating());
        assertEquals(RATING_FITCH, rating.getValue().getFitchRating());
        assertEquals(RATING_ORDER_NUMBER, rating.getValue().getOrderNumber());
    }

    @Test
    public void testUpdate() {
        // Arrange
        Rating RatingFromRepo = new Rating();
        RatingFromRepo.setId(RATING_ID);
        when(repository.findById(RATING_ID)).thenReturn(Optional.of(RatingFromRepo));
        Rating updatedRating = new Rating();
        updatedRating.setMoodysRating(RATING_MOODYS);
        updatedRating.setSandPRating(RATING_SANDP);
        updatedRating.setFitchRating(RATING_FITCH);
        updatedRating.setOrderNumber(RATING_ORDER_NUMBER);

        // Act
        service.update(updatedRating, RATING_ID);

        // Assert
        ArgumentCaptor<Rating> rating = ArgumentCaptor.forClass(Rating.class);
        verify(repository, times(1)).save(rating.capture());
        assertEquals(RATING_ID, rating.getValue().getId(), 0);
        assertEquals(RATING_MOODYS, rating.getValue().getMoodysRating());
        assertEquals(RATING_SANDP, rating.getValue().getSandPRating());
        assertEquals(RATING_FITCH, rating.getValue().getFitchRating());
        assertEquals(RATING_ORDER_NUMBER, rating.getValue().getOrderNumber());
        verify(repository, times(1)).findById(RATING_ID);
    }

    @Test
    public void testFindById() {
        // Arrange
        Rating ratingFromRepo = new Rating();
        ratingFromRepo.setId(RATING_ID);
        when(repository.findById(RATING_ID)).thenReturn(Optional.of(ratingFromRepo));

        // Act
        Rating rating = service.findById(RATING_ID);

        // Assert
        assertEquals(RATING_ID, rating.getId(), 0);
        verify(repository, times(1)).findById(RATING_ID);
    }

    @Test
    public void testFindByIdNotFound() {
        // Arrange
        when(repository.findById(RATING_ID)).thenReturn(Optional.empty());

        // Act
        assertThrows(RatingNotFoundException.class, () -> service.findById(RATING_ID));

        // Assert
        verify(repository, times(1)).findById(RATING_ID);
    }

    @Test
    public void testDelete() {
        // Act
        service.delete(RATING_ID);

        // Assert
        verify(repository, times(1)).deleteById(RATING_ID);
    }
}
