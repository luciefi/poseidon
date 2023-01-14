package com.poseidon.poseidon.controllers;

import com.poseidon.poseidon.domain.Trade;
import com.poseidon.poseidon.exceptions.TradeNotFoundException;
import com.poseidon.poseidon.services.ITradeService;
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
import static org.junit.jupiter.api.Assertions.assertEquals;
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
public class TradeControllerTest {

    final int TRADE_ID = 1;
    final String TRADE_ACCOUNT = "account";
    final String TRADE_TYPE = "type";
    final Double TRADE_QUANTITY = 1d;

    @MockBean
    ITradeService service;

    @Autowired
    public MockMvc mockMvc;

    @Test
    public void testHome() throws Exception {
        mockMvc.perform(get("/trade/list"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("trade/list"))
                .andExpect(content().string(containsString("Trade List")));
        verify(service, Mockito.times(1)).findAll();
    }

    @Test
    public void testAddTradeForm() throws Exception {
        mockMvc.perform(get("/trade/add"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("trade/add"))
                .andExpect(content().string(containsString("Add New Trade")));
    }

    @Test
    public void testValidate() throws Exception {
        String content = "account=" + TRADE_ACCOUNT + "&type=" + TRADE_TYPE + "&buyQuantity=" + TRADE_QUANTITY;
        mockMvc.perform(post("/trade/validate")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .content(content)
                )
                .andDo(print())
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/trade/list"));
        ArgumentCaptor<Trade> trade = ArgumentCaptor.forClass(Trade.class);
        verify(service, Mockito.times(1)).save(trade.capture());
        assertEquals(TRADE_QUANTITY, trade.getValue().getBuyQuantity(), 0);
        assertEquals(TRADE_ACCOUNT, trade.getValue().getAccount());
        assertEquals(TRADE_TYPE, trade.getValue().getType());
    }

    @Test
    public void testValidateFormError() throws Exception {
        String s = "account=" + TRADE_ACCOUNT + "&type=" + TRADE_TYPE + "&buyQuantity=";
        mockMvc.perform(post("/trade/validate")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .content(s)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("trade/add"));
        verify(service, Mockito.never()).save(any(Trade.class));
    }

    @Test
    public void testShowUpdateForm() throws Exception {
        when(service.findById(TRADE_ID)).thenReturn(new Trade());
        mockMvc.perform(get("/trade/update/" + TRADE_ID))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("trade/update"))
                .andExpect(content().string(containsString("Update Trade")));
        verify(service, Mockito.times(1)).findById(TRADE_ID);
    }

    @Test
    public void testUpdateTrade() throws Exception {
        String content = "account=" + TRADE_ACCOUNT + "&type=" + TRADE_TYPE + "&buyQuantity=" + TRADE_QUANTITY;
        mockMvc.perform(post("/trade/update/" + TRADE_ID)
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .content(content)
                )
                .andDo(print())
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/trade/list"));
        ArgumentCaptor<Trade> trade = ArgumentCaptor.forClass(Trade.class);
        ArgumentCaptor<Integer> id = ArgumentCaptor.forClass(Integer.class);
        verify(service, Mockito.times(1)).update(trade.capture(), id.capture());
        assertEquals(TRADE_ID, id.getValue(), 0);
        assertEquals(TRADE_QUANTITY, trade.getValue().getBuyQuantity(), 0);
        assertEquals(TRADE_ACCOUNT, trade.getValue().getAccount());
        assertEquals(TRADE_TYPE, trade.getValue().getType());
    }

    @Test
    public void testUpdateTradeFormError() throws Exception {
        String content = "account=" + TRADE_ACCOUNT + "&type=" + TRADE_TYPE + "&buyQuantity=";
        mockMvc.perform(post("/trade/update/" + TRADE_ID)
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .content(content)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("trade/update"));
        verify(service, Mockito.never()).update(any(Trade.class), anyInt());
    }

    @Test
    public void testUpdateTradeNotFound() throws Exception {
        String content = "account=" + TRADE_ACCOUNT + "&type=" + TRADE_TYPE + "&buyQuantity=" + TRADE_QUANTITY;
        doThrow(new TradeNotFoundException()).when(service).update(any(Trade.class), anyInt());
        mockMvc.perform(post("/trade/update/" + TRADE_ID)
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .content(content)
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(view().name("trade/update"));
        ArgumentCaptor<Trade> trade = ArgumentCaptor.forClass(Trade.class);
        ArgumentCaptor<Integer> id = ArgumentCaptor.forClass(Integer.class);
        verify(service, Mockito.times(1)).update(trade.capture(), id.capture());
        assertEquals(TRADE_ID, id.getValue(), 0);
        assertEquals(TRADE_QUANTITY, trade.getValue().getBuyQuantity(), 0);
        assertEquals(TRADE_ACCOUNT, trade.getValue().getAccount());
        assertEquals(TRADE_TYPE, trade.getValue().getType());
    }


    @Test
    public void testDeleteTrade() throws Exception {
        mockMvc.perform(get("/trade/delete/" + TRADE_ID))
                .andDo(print())
                .andExpect(status().isFound())
                .andExpect(view().name("redirect:/trade/list"));
        verify(service, Mockito.times(1)).delete(TRADE_ID);
    }
}
