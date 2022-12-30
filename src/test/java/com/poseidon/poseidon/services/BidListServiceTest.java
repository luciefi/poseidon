package com.poseidon.poseidon.services;

import com.poseidon.poseidon.domain.BidList;
import com.poseidon.poseidon.exceptions.BidListNotFoundException;
import com.poseidon.poseidon.repositories.BidListRepository;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BidListServiceTest {

    final int BID_LIST_ID = 1;
    final String BID_LIST_ACCOUNT = "account";
    final String BID_LIST_TYPE = "type";
    final Double BID_LIST_QUANTITY = 1d;

    @InjectMocks
    private BidListService service;

    @Mock
    private BidListRepository repository;

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
        BidList newBidList = new BidList();
        newBidList.setBidQuantity(BID_LIST_QUANTITY);
        newBidList.setAccount(BID_LIST_ACCOUNT);
        newBidList.setType(BID_LIST_TYPE);

        // Act
        service.save(newBidList);

        // Assert
        ArgumentCaptor<BidList> bidList = ArgumentCaptor.forClass(BidList.class);
        verify(repository, times(1)).save(bidList.capture());
        assertEquals(BID_LIST_QUANTITY, bidList.getValue().getBidQuantity(), 0);
        assertEquals(BID_LIST_ACCOUNT, bidList.getValue().getAccount());
        assertEquals(BID_LIST_TYPE, bidList.getValue().getType());
    }

    @Test
    public void testUpdate() {
        // Arrange
        BidList bidListFromRepo = new BidList();
        bidListFromRepo.setBidListId(BID_LIST_ID);
        when(repository.findById(BID_LIST_ID)).thenReturn(Optional.of(bidListFromRepo));
        BidList updatedBidList = new BidList();
        updatedBidList.setBidQuantity(BID_LIST_QUANTITY);
        updatedBidList.setAccount(BID_LIST_ACCOUNT);
        updatedBidList.setType(BID_LIST_TYPE);

        // Act
        service.update(updatedBidList, BID_LIST_ID);

        // Assert
        ArgumentCaptor<BidList> bidList = ArgumentCaptor.forClass(BidList.class);
        verify(repository, times(1)).save(bidList.capture());
        assertEquals(BID_LIST_ID, bidList.getValue().getBidListId(), 0);
        assertEquals(BID_LIST_QUANTITY, bidList.getValue().getBidQuantity(), 0);
        assertEquals(BID_LIST_ACCOUNT, bidList.getValue().getAccount());
        assertEquals(BID_LIST_TYPE, bidList.getValue().getType());
        verify(repository, times(1)).findById(BID_LIST_ID);
    }

    @Test
    public void testFindById() {
        // Arrange
        BidList bidListFromRepo = new BidList();
        bidListFromRepo.setBidListId(BID_LIST_ID);
        when(repository.findById(BID_LIST_ID)).thenReturn(Optional.of(bidListFromRepo));

        // Act
        BidList bidList = service.findById(BID_LIST_ID);

        // Assert
        assertEquals(BID_LIST_ID, bidList.getBidListId(), 0);
        verify(repository, times(1)).findById(BID_LIST_ID);
    }

    @Test
    public void testFindByIdNotFound() {
        // Arrange

        when(repository.findById(BID_LIST_ID)).thenReturn(Optional.empty());

        // Act
        assertThrows(BidListNotFoundException.class, () -> service.findById(BID_LIST_ID));

        // Assert
        verify(repository, times(1)).findById(BID_LIST_ID);
    }

    @Test
    public void testDelete() {
        // Act
        service.delete(BID_LIST_ID);

        // Assert
        verify(repository, times(1)).deleteById(BID_LIST_ID);
    }
}