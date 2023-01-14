package com.poseidon.poseidon.controllers;

import com.poseidon.poseidon.domain.CurvePoint;
import com.poseidon.poseidon.exceptions.CurvePointNotFoundException;
import com.poseidon.poseidon.services.CurvePointService;
import com.poseidon.poseidon.services.ICurvePointService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
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
@WithMockUser
class CurveControllerTest {

    final int CURVE_POINT_ID = 1;
    final int CURVE_POINT_CURVE_ID = 1;
    final Double CURVE_POINT_TERM = 1d;
    final Double CURVE_POINT_VALUE = 2d;

    @MockBean
    ICurvePointService service;

    @Autowired
    public MockMvc mockMvc;

    @Test
    void home() throws Exception {
        mockMvc.perform(get("/curvePoint/list")).andDo(print()).andExpect(status().isOk()).andExpect(view().name("curvePoint/list")).andExpect(content().string(containsString("Curve Point List")));
        verify(service, Mockito.times(1)).findAll();
    }

    @Test
    void addCurvePointForm() throws Exception {
        mockMvc.perform(get("/curvePoint/add")).andDo(print()).andExpect(status().isOk()).andExpect(view().name("curvePoint/add")).andExpect(content().string(containsString("Add New Curve Point")));
    }

    @Test
    void validate() throws Exception {
        String content = "curveId=" + CURVE_POINT_CURVE_ID + "&term=" + CURVE_POINT_TERM + "&value=" + CURVE_POINT_VALUE;
        mockMvc.perform(post("/curvePoint/validate").contentType(MediaType.APPLICATION_FORM_URLENCODED).content(content)).andDo(print()).andExpect(status().isFound()).andExpect(view().name("redirect:/curvePoint/list"));
        ArgumentCaptor<CurvePoint> curvePoint = ArgumentCaptor.forClass(CurvePoint.class);
        verify(service, Mockito.times(1)).save(curvePoint.capture());

        Assertions.assertEquals(CURVE_POINT_CURVE_ID, curvePoint.getValue().getCurveId(), 0);
        Assertions.assertEquals(CURVE_POINT_TERM, curvePoint.getValue().getTerm(), 0);
        Assertions.assertEquals(CURVE_POINT_VALUE, curvePoint.getValue().getValue());
    }

    @Test
    public void testValidateFormError() throws Exception {
        String content = "curveId=" + CURVE_POINT_CURVE_ID + "&term=" + CURVE_POINT_TERM + "&value=";
        mockMvc.perform(post("/curvePoint/validate").contentType(MediaType.APPLICATION_FORM_URLENCODED).content(content)).andDo(print()).andExpect(status().isOk()).andExpect(view().name("curvePoint/add"));
        verify(service, Mockito.never()).save(any(CurvePoint.class));
    }

    @Test
    public void testShowUpdateForm() throws Exception {
        when(service.findById(CURVE_POINT_ID)).thenReturn(new CurvePoint());
        mockMvc.perform(get("/curvePoint/update/" + CURVE_POINT_ID)).andDo(print()).andExpect(status().isOk()).andExpect(view().name("curvePoint/update")).andExpect(content().string(containsString("Update CurvePoint")));
        verify(service, Mockito.times(1)).findById(CURVE_POINT_ID);
    }

    @Test
    public void updateCurvePoint() throws Exception {
        String content = "curveId=" + CURVE_POINT_CURVE_ID + "&term=" + CURVE_POINT_TERM + "&value=" + CURVE_POINT_VALUE;
        mockMvc.perform(post("/curvePoint/update/" + CURVE_POINT_ID).contentType(MediaType.APPLICATION_FORM_URLENCODED).content(content)).andDo(print()).andExpect(status().isFound()).andExpect(view().name("redirect:/curvePoint/list"));
        ArgumentCaptor<CurvePoint> curvePoint = ArgumentCaptor.forClass(CurvePoint.class);
        ArgumentCaptor<Integer> id = ArgumentCaptor.forClass(Integer.class);
        verify(service, Mockito.times(1)).update(curvePoint.capture(), id.capture());
        Assertions.assertEquals(CURVE_POINT_ID, id.getValue(), 0);
        Assertions.assertEquals(CURVE_POINT_CURVE_ID, curvePoint.getValue().getCurveId(), 0);
        Assertions.assertEquals(CURVE_POINT_TERM, curvePoint.getValue().getTerm());
        Assertions.assertEquals(CURVE_POINT_VALUE, curvePoint.getValue().getValue());
    }

    @Test
    public void updateCurvePointFormError() throws Exception {
        String content = "curveId=" + CURVE_POINT_CURVE_ID + "&term=" + CURVE_POINT_TERM + "&value=";
        mockMvc.perform(post("/curvePoint/update/" + CURVE_POINT_ID).contentType(MediaType.APPLICATION_FORM_URLENCODED).content(content)).andDo(print()).andExpect(status().isOk()).andExpect(view().name("curvePoint/update"));
        verify(service, Mockito.never()).update(any(CurvePoint.class), anyInt());
    }

    @Test
    public void updateCurvePointNotFound() throws Exception {
        String content = "curveId=" + CURVE_POINT_CURVE_ID + "&term=" + CURVE_POINT_TERM + "&value=" + CURVE_POINT_VALUE;
        doThrow(new CurvePointNotFoundException()).when(service).update(any(CurvePoint.class), anyInt());
        mockMvc.perform(post("/curvePoint/update/" + CURVE_POINT_ID).contentType(MediaType.APPLICATION_FORM_URLENCODED).content(content)).andDo(print()).andExpect(status().isOk()).andExpect(view().name("curvePoint/update"));
        ArgumentCaptor<CurvePoint> curvePoint = ArgumentCaptor.forClass(CurvePoint.class);
        ArgumentCaptor<Integer> id = ArgumentCaptor.forClass(Integer.class);
        verify(service, Mockito.times(1)).update(curvePoint.capture(), id.capture());
        Assertions.assertEquals(CURVE_POINT_ID, id.getValue(), 0);
        Assertions.assertEquals(CURVE_POINT_CURVE_ID, curvePoint.getValue().getCurveId(), 0);
        Assertions.assertEquals(CURVE_POINT_TERM, curvePoint.getValue().getTerm());
        Assertions.assertEquals(CURVE_POINT_VALUE, curvePoint.getValue().getValue());
    }

    @Test
    void deleteCurvePoint() throws Exception {
        mockMvc.perform(get("/curvePoint/delete/" + CURVE_POINT_ID)).andDo(print()).andExpect(status().isFound()).andExpect(view().name("redirect:/curvePoint/list"));
        verify(service, Mockito.times(1)).delete(CURVE_POINT_ID);
    }
}