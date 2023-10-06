package com.nnk.springboot.service;

import com.nnk.springboot.domain.Trade;
import com.nnk.springboot.exception.CurvePointNotFoundException;
import com.nnk.springboot.exception.TradeNotFoundException;
import com.nnk.springboot.repositories.TradeRepository;
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
class TradeServiceTest {

    @Mock
    private TradeRepository tradeRepository;

    @InjectMocks
    TradeService tradeService;

    @Test
    public void getAllTrade() {
        Trade trade1 = new Trade();
            trade1.setTradeId(1);
            trade1.setAccount("account1");
            trade1.setType("type1");
            trade1.setBuyQuantity(1.0);
        Trade trade2 = new Trade();
            trade2.setTradeId(2);
            trade2.setAccount("account2");
            trade2.setType("type2");
            trade2.setBuyQuantity(2.0);
        List<Trade> tradeList = new ArrayList<>();
            tradeList.add(trade1);
            tradeList.add(trade2);

        when(tradeRepository.findAll()).thenReturn(tradeList);

        List<Trade> result = tradeService.getAllTrade();

        assertThat(result.size()).isEqualTo(2);
        assertThat(result).isEqualTo(tradeList);
        verify(tradeRepository).findAll();
    }

    @Test
    public void saveTrade() {
        Trade trade = new Trade();
            trade.setTradeId(1);
            trade.setAccount("account1");
            trade.setType("type1");
            trade.setBuyQuantity(1.0);

        when(tradeRepository.save(any(Trade.class))).thenReturn(trade);
        Trade result = tradeService.saveTrade(trade);

        assertEquals(result, trade);
        assertThat(1).isEqualTo(result.getTradeId());
        assertThat("account1").isEqualTo(result.getAccount());
        assertThat("type1").isEqualTo(result.getType());
        assertThat(1.0).isEqualTo(result.getBuyQuantity());
    }

    @Test
    public void getTradeNameById() {
        Trade trade = new Trade();
            trade.setTradeId(1);
            trade.setAccount("account1");
            trade.setType("type1");
            trade.setBuyQuantity(1.0);

        when(tradeRepository.findById(anyInt())).thenReturn(Optional.of(trade));

        Trade result = tradeService.getTradeNameById(1);

        assertEquals(result, trade);
        assertThat(1).isEqualTo(result.getTradeId());
        assertThat("account1").isEqualTo(result.getAccount());
        assertThat("type1").isEqualTo(result.getType());
        assertThat(1.0).isEqualTo(result.getBuyQuantity());
    }

    @Test
    public void getTradeByIdDoTrowTradeNotFoundException() {
        when(tradeRepository.findById(anyInt())).thenReturn(Optional.empty());
        assertThrows(TradeNotFoundException.class,()->tradeService.getTradeNameById(1));
        verify(tradeRepository).findById(isA(Integer.class));
    }

    @Test
    public void updateTrade() {
        Trade tradeBeforeUpdate = new Trade();
            tradeBeforeUpdate.setTradeId(1);
            tradeBeforeUpdate.setAccount("account1");
            tradeBeforeUpdate.setType("type1");
            tradeBeforeUpdate.setBuyQuantity(1.0);
        Trade tradeAfterUpdate = new Trade();
            tradeAfterUpdate.setTradeId(1);
            tradeAfterUpdate.setAccount("account2");
            tradeAfterUpdate.setType("type2");
            tradeAfterUpdate.setBuyQuantity(2.0);

        when(tradeRepository.getById(anyInt())).thenReturn(tradeBeforeUpdate);
        when(tradeRepository.save(any(Trade.class))).thenReturn(tradeAfterUpdate);

        Trade result = tradeService.updateTrade(tradeBeforeUpdate);

        assertEquals(result, tradeAfterUpdate);
        assertThat(1).isEqualTo(result.getTradeId());
        assertThat("account2").isEqualTo(result.getAccount());
        assertThat("type2").isEqualTo(result.getType());
        assertThat(2.0).isEqualTo(result.getBuyQuantity());
    }

    @Test
    public void deleteTrade() {
        Trade trade = new Trade();
            trade.setTradeId(1);
            trade.setAccount("account1");
            trade.setType("type1");
            trade.setBuyQuantity(1.0);

        tradeService.deleteTrade(trade.getTradeId());

        verify(tradeRepository).deleteById(1);
    }
}