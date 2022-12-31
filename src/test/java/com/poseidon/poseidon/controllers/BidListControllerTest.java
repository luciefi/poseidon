package com.poseidon.poseidon.controllers;

import com.poseidon.poseidon.domain.BidList;
import com.poseidon.poseidon.exceptions.BidListNotFoundException;
import com.poseidon.poseidon.services.IBidService;
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
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@SpringBootTest
@AutoConfigureMockMvc
public class BidListControllerTest {

    final int BID_LIST_ID = 1;
    final String BID_LIST_ACCOUNT = "account";
    final String BID_LIST_TYPE = "type";
    final Double BID_LIST_QUANTITY = 1d;

    @MockBean
    IBidService service;

    @Autowired
    public MockMvc mockMvc;

    @Test
    public void testHome() throws Exception {
        mockMvc.perform(get("/bidList/list")).andDo(print()).andExpect(status().isOk()).andExpect(view().name("bidList/list")).andExpect(content().string(containsString("Bid List")));
        verify(service, Mockito.times(1)).findAll();
    }

    @Test
    public void testAddBidForm() throws Exception {
        mockMvc.perform(get("/bidList/add")).andDo(print()).andExpect(status().isOk()).andExpect(view().name("bidList/add")).andExpect(content().string(containsString("Add New Bid")));
    }

    @Test
    public void testValidate() throws Exception {
        String content = "account=" + BID_LIST_ACCOUNT + "&type=" + BID_LIST_TYPE + "&bidQuantity=" + BID_LIST_QUANTITY;
        mockMvc.perform(post("/bidList/validate").contentType(MediaType.APPLICATION_FORM_URLENCODED).content(content)).andDo(print()).andExpect(status().isFound()).andExpect(view().name("redirect:/bidList/list"));
        ArgumentCaptor<BidList> bidList = ArgumentCaptor.forClass(BidList.class);
        verify(service, Mockito.times(1)).save(bidList.capture());
        assertEquals(BID_LIST_QUANTITY, bidList.getValue().getBidQuantity(), 0);
        assertEquals(BID_LIST_ACCOUNT, bidList.getValue().getAccount());
        assertEquals(BID_LIST_TYPE, bidList.getValue().getType());
    }

    @Test
    public void testValidateFormError() throws Exception {
        String s = "account=" + BID_LIST_ACCOUNT + "&type=" + BID_LIST_TYPE + "&bidQuantity=";
        mockMvc.perform(post("/bidList/validate").contentType(MediaType.APPLICATION_FORM_URLENCODED).content(s)).andDo(print()).andExpect(status().isOk()).andExpect(view().name("bidList/add"));
        verify(service, Mockito.never()).save(any(BidList.class));
    }

    @Test
    public void testShowUpdateForm() throws Exception {
        when(service.findById(BID_LIST_ID)).thenReturn(new BidList());
        mockMvc.perform(get("/bidList/update/" + BID_LIST_ID)).andDo(print()).andExpect(status().isOk()).andExpect(view().name("bidList/update")).andExpect(content().string(containsString("Update Bid")));
        verify(service, Mockito.times(1)).findById(BID_LIST_ID);
    }

    @Test
    public void testUpdateBid() throws Exception {
        String content = "account=" + BID_LIST_ACCOUNT + "&type=" + BID_LIST_TYPE + "&bidQuantity=" + BID_LIST_QUANTITY;
        mockMvc.perform(post("/bidList/update/" + BID_LIST_ID).contentType(MediaType.APPLICATION_FORM_URLENCODED).content(content)).andDo(print()).andExpect(status().isFound()).andExpect(view().name("redirect:/bidList/list"));
        ArgumentCaptor<BidList> bidList = ArgumentCaptor.forClass(BidList.class);
        ArgumentCaptor<Integer> id = ArgumentCaptor.forClass(Integer.class);
        verify(service, Mockito.times(1)).update(bidList.capture(), id.capture());
        assertEquals(BID_LIST_ID, id.getValue(), 0);
        assertEquals(BID_LIST_QUANTITY, bidList.getValue().getBidQuantity(), 0);
        assertEquals(BID_LIST_ACCOUNT, bidList.getValue().getAccount());
        assertEquals(BID_LIST_TYPE, bidList.getValue().getType());
    }

    @Test
    public void testUpdateBidFormError() throws Exception {
        String content = "account=" + BID_LIST_ACCOUNT + "&type=" + BID_LIST_TYPE + "&bidQuantity=";
        mockMvc.perform(post("/bidList/update/" + BID_LIST_ID).contentType(MediaType.APPLICATION_FORM_URLENCODED).content(content)).andDo(print()).andExpect(status().isOk()).andExpect(view().name("bidList/update"));
        verify(service, Mockito.never()).update(any(BidList.class), anyInt());
    }

    @Test
    public void testUpdateBidNotFound() throws Exception {
        String content = "account=" + BID_LIST_ACCOUNT + "&type=" + BID_LIST_TYPE + "&bidQuantity=" + BID_LIST_QUANTITY;
        doThrow(new BidListNotFoundException()).when(service).update(any(BidList.class), anyInt());
        mockMvc.perform(post("/bidList/update/" + BID_LIST_ID).contentType(MediaType.APPLICATION_FORM_URLENCODED).content(content)).andDo(print()).andExpect(status().isOk()).andExpect(view().name("bidList/update"));
        ArgumentCaptor<BidList> bidList = ArgumentCaptor.forClass(BidList.class);
        ArgumentCaptor<Integer> id = ArgumentCaptor.forClass(Integer.class);
        verify(service, Mockito.times(1)).update(bidList.capture(), id.capture());
        assertEquals(BID_LIST_ID, id.getValue(), 0);
        assertEquals(BID_LIST_QUANTITY, bidList.getValue().getBidQuantity(), 0);
        assertEquals(BID_LIST_ACCOUNT, bidList.getValue().getAccount());
        assertEquals(BID_LIST_TYPE, bidList.getValue().getType());
    }

    @Test
    public void testDeleteBid() throws Exception {
        mockMvc.perform(get("/bidList/delete/" + BID_LIST_ID)).andDo(print()).andExpect(status().isFound()).andExpect(view().name("redirect:/bidList/list"));
        verify(service, Mockito.times(1)).delete(BID_LIST_ID);
    }
}