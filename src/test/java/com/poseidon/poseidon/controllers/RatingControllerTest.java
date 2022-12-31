package com.poseidon.poseidon.controllers;

import com.poseidon.poseidon.domain.Rating;
import com.poseidon.poseidon.exceptions.RatingNotFoundException;
import com.poseidon.poseidon.services.RatingService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class RatingControllerTest {

    final Integer RATING_ID = 1;
    final String RATING_MOODYS = "moodys";
    final String RATING_SANDP = "sandp";
    final String RATING_FITCH = "fitch";
    final Integer RATING_ORDER_NUMBER = 2;

    @MockBean
    RatingService service;

    @Autowired
    public MockMvc mockMvc;

    @Test
    void home() throws Exception {
        mockMvc.perform(get("/rating/list")).andDo(print()).andExpect(status().isOk()).andExpect(view().name("rating/list")).andExpect(content().string(containsString("Rating List")));
        verify(service, Mockito.times(1)).findAll();
    }

    @Test
    void addRatingForm() throws Exception {
        mockMvc.perform(get("/rating/add")).andDo(print()).andExpect(status().isOk()).andExpect(view().name("rating/add")).andExpect(content().string(containsString("Add New Rating")));
    }

    @Test
    void validate() throws Exception {
        String content = "moodysRating=" + RATING_MOODYS + "&sandPRating=" + RATING_SANDP + "&fitchRating=" + RATING_FITCH + "&orderNumber" +
                "=" + RATING_ORDER_NUMBER;
        mockMvc.perform(post("/rating/validate").contentType(MediaType.APPLICATION_FORM_URLENCODED).content(content)).andDo(print()).andExpect(status().isFound()).andExpect(view().name("redirect:/rating/list"));
        ArgumentCaptor<Rating> rating = ArgumentCaptor.forClass(Rating.class);
        verify(service, Mockito.times(1)).save(rating.capture());
        Assertions.assertEquals(RATING_MOODYS, rating.getValue().getMoodysRating());
        Assertions.assertEquals(RATING_SANDP, rating.getValue().getSandPRating());
        Assertions.assertEquals(RATING_FITCH, rating.getValue().getFitchRating());
        Assertions.assertEquals(RATING_ORDER_NUMBER, rating.getValue().getOrderNumber());
    }

    @Test
    public void testValidateFormError() throws Exception {
        String content = "moodysRating=" + RATING_MOODYS + "&sandPRating=" + RATING_SANDP + "&fitchRating=" + RATING_FITCH + "&orderNumber" +
                "=";
        mockMvc.perform(post("/rating/validate").contentType(MediaType.APPLICATION_FORM_URLENCODED).content(content)).andDo(print()).andExpect(status().isOk()).andExpect(view().name("rating/add"));
        verify(service, Mockito.never()).save(any(Rating.class));
    }

    @Test
    public void testShowUpdateForm() throws Exception {
        when(service.findById(RATING_ID)).thenReturn(new Rating());
        mockMvc.perform(get("/rating/update/" + RATING_ID)).andDo(print()).andExpect(status().isOk()).andExpect(view().name("rating/update")).andExpect(content().string(containsString("Update Rating")));
        verify(service, Mockito.times(1)).findById(RATING_ID);
    }

    @Test
    public void updateRating() throws Exception {
        String content = "moodysRating=" + RATING_MOODYS + "&sandPRating=" + RATING_SANDP + "&fitchRating=" + RATING_FITCH + "&orderNumber" +
                "=" + RATING_ORDER_NUMBER;
        mockMvc.perform(post("/rating/update/" + RATING_ID).contentType(MediaType.APPLICATION_FORM_URLENCODED).content(content)).andDo(print()).andExpect(status().isFound()).andExpect(view().name("redirect:/rating/list"));
        ArgumentCaptor<Rating> rating = ArgumentCaptor.forClass(Rating.class);
        ArgumentCaptor<Integer> id = ArgumentCaptor.forClass(Integer.class);
        verify(service, Mockito.times(1)).update(rating.capture(), id.capture());
        Assertions.assertEquals(RATING_ID, id.getValue(), 0);
        Assertions.assertEquals(RATING_MOODYS, rating.getValue().getMoodysRating());
        Assertions.assertEquals(RATING_SANDP, rating.getValue().getSandPRating());
        Assertions.assertEquals(RATING_FITCH, rating.getValue().getFitchRating());
        Assertions.assertEquals(RATING_ORDER_NUMBER, rating.getValue().getOrderNumber());
    }

    @Test
    public void updateRatingFormError() throws Exception {
        String content = "moodysRating=" + RATING_MOODYS + "&sandPRating=" + RATING_SANDP + "&fitchRating=" + RATING_FITCH + "&orderNumber" +
                "=";
        mockMvc.perform(post("/rating/update/" + RATING_ID).contentType(MediaType.APPLICATION_FORM_URLENCODED).content(content)).andDo(print()).andExpect(status().isOk()).andExpect(view().name("rating/update"));
        verify(service, Mockito.never()).update(any(Rating.class), anyInt());
    }

    @Test
    public void updateRatingNotFound() throws Exception {
        String content = "moodysRating=" + RATING_MOODYS + "&sandPRating=" + RATING_SANDP + "&fitchRating=" + RATING_FITCH + "&orderNumber" +
                "=" + RATING_ORDER_NUMBER;
        doThrow(new RatingNotFoundException()).when(service).update(any(Rating.class), anyInt());
        mockMvc.perform(post("/rating/update/" + RATING_ID).contentType(MediaType.APPLICATION_FORM_URLENCODED).content(content)).andDo(print()).andExpect(status().isOk()).andExpect(view().name("rating/update"));
        ArgumentCaptor<Rating> rating = ArgumentCaptor.forClass(Rating.class);
        ArgumentCaptor<Integer> id = ArgumentCaptor.forClass(Integer.class);
        verify(service, Mockito.times(1)).update(rating.capture(), id.capture());
        Assertions.assertEquals(RATING_ID, id.getValue(), 0);
        Assertions.assertEquals(RATING_MOODYS, rating.getValue().getMoodysRating());
        Assertions.assertEquals(RATING_SANDP, rating.getValue().getSandPRating());
        Assertions.assertEquals(RATING_FITCH, rating.getValue().getFitchRating());
        Assertions.assertEquals(RATING_ORDER_NUMBER, rating.getValue().getOrderNumber());
    }

    @Test
    void deleteRating() throws Exception {
        mockMvc.perform(get("/rating/delete/" + RATING_ID)).andDo(print()).andExpect(status().isFound()).andExpect(view().name("redirect:/rating/list"));
        verify(service, Mockito.times(1)).delete(RATING_ID);
    }
}
