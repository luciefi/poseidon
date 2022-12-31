package com.poseidon.poseidon.services;

import com.poseidon.poseidon.domain.Trade;
import com.poseidon.poseidon.exceptions.TradeNotFoundException;
import com.poseidon.poseidon.repositories.TradeRepository;
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
public class TradeServiceTest {

    final int TRADE_ID = 1;
    final String TRADE_ACCOUNT = "account";
    final String TRADE_TYPE = "type";
    final Double TRADE_QUANTITY = 1d;


    @InjectMocks
    private TradeService service;

    @Mock
    private TradeRepository repository;

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
        Trade newTrade = new Trade();
        newTrade.setBuyQuantity(TRADE_QUANTITY);
        newTrade.setAccount(TRADE_ACCOUNT);
        newTrade.setType(TRADE_TYPE);

        // Act
        service.save(newTrade);

        // Assert
        ArgumentCaptor<Trade> trade = ArgumentCaptor.forClass(Trade.class);
        verify(repository, times(1)).save(trade.capture());
        assertEquals(TRADE_QUANTITY, trade.getValue().getBuyQuantity(), 0);
        assertEquals(TRADE_ACCOUNT, trade.getValue().getAccount());
        assertEquals(TRADE_TYPE, trade.getValue().getType());
    }

    @Test
    public void testUpdate() {
        // Arrange
        Trade tradeFromRepo = new Trade();
        tradeFromRepo.setTradeId(TRADE_ID);
        when(repository.findById(TRADE_ID)).thenReturn(Optional.of(tradeFromRepo));
        Trade updatedTrade = new Trade();
        updatedTrade.setBuyQuantity(TRADE_QUANTITY);
        updatedTrade.setAccount(TRADE_ACCOUNT);
        updatedTrade.setType(TRADE_TYPE);

        // Act
        service.update(updatedTrade, TRADE_ID);

        // Assert
        ArgumentCaptor<Trade> trade = ArgumentCaptor.forClass(Trade.class);
        verify(repository, times(1)).save(trade.capture());
        assertEquals(TRADE_ID, trade.getValue().getTradeId(), 0);
        assertEquals(TRADE_QUANTITY, trade.getValue().getBuyQuantity(), 0);
        assertEquals(TRADE_ACCOUNT, trade.getValue().getAccount());
        assertEquals(TRADE_TYPE, trade.getValue().getType());
        verify(repository, times(1)).findById(TRADE_ID);
    }

    @Test
    public void testFindById() {
        // Arrange
        Trade tradeFromRepo = new Trade();
        tradeFromRepo.setTradeId(TRADE_ID);
        when(repository.findById(TRADE_ID)).thenReturn(Optional.of(tradeFromRepo));

        // Act
        Trade trade = service.findById(TRADE_ID);

        // Assert
        assertEquals(TRADE_ID, trade.getTradeId(), 0);
        verify(repository, times(1)).findById(TRADE_ID);
    }

    @Test
    public void testFindByIdNotFound() {
        // Arrange

        when(repository.findById(TRADE_ID)).thenReturn(Optional.empty());

        // Act
        assertThrows(TradeNotFoundException.class, () -> service.findById(TRADE_ID));

        // Assert
        verify(repository, times(1)).findById(TRADE_ID);
    }

    @Test
    public void testDelete() {
        // Act
        service.delete(TRADE_ID);

        // Assert
        verify(repository, times(1)).deleteById(TRADE_ID);
    }
}
