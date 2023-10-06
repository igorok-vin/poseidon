package com.nnk.springboot.service;

import com.nnk.springboot.domain.BidList;
import com.nnk.springboot.exception.BidListNotFoundException;
import com.nnk.springboot.repositories.BidListRepository;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BidServiceTest {
    @Mock
    private BidListRepository bidListRepository;
    @InjectMocks
    BidService bidService;

    @Test
    public void getAllBidList() {
        BidList bidList1 = new BidList();
                bidList1.setBidListId(1);
                bidList1.setAccount("account1");
                bidList1.setType("type1");
                bidList1.setBidQuantity(2.0);
                bidList1.setAskQuantity(4.0);
        BidList bidList2 = new BidList();
                bidList2.setBidListId(2);
                bidList2.setAccount("account2");
                bidList2.setType("type2");
                bidList2.setBidQuantity(6.0);
                bidList2.setAskQuantity(8.0);
        List<BidList> bidLists = new ArrayList<>();
                bidLists.add(bidList1);
                bidLists.add(bidList2);

        when(bidListRepository.findAll()).thenReturn(bidLists);

        List<BidList> result =  bidService.getAllBidList();

        assertThat(result.size()).isEqualTo(2);
        assertThat(result).isEqualTo(bidLists);
        verify(bidListRepository,times(1)).findAll();
    }

    @Test
    public void saveBidList() {
        BidList bidList1 = new BidList();
        bidList1.setBidListId(1);
        bidList1.setAccount("account1");
        bidList1.setType("type1");
        bidList1.setBidQuantity(2.0);
        bidList1.setAskQuantity(4.0);

        when(bidListRepository.save(any(BidList.class))).thenReturn(bidList1);

        BidList result = bidService.saveBidList(bidList1);

        assertEquals(result,bidList1);
        assertThat(1).isEqualTo(result.getBidListId());
        assertThat("account1").isEqualTo(result.getAccount());
        assertThat("type1").isEqualTo(result.getType());
        assertThat(2.0).isEqualTo(result.getBidQuantity());
        assertThat(4.0).isEqualTo(result.getAskQuantity());
        verify(bidListRepository,times(1)).save(isA(BidList.class));
    }

    @Test
    public void getBidListById() {
        BidList bidList = new BidList();
        bidList.setBidListId(1);
        bidList.setAccount("account");
        bidList.setType("type1");
        bidList.setBidQuantity(2.0);
        bidList.setAskQuantity(4.0);

        when(bidListRepository.findById(anyInt())).thenReturn(Optional.of(bidList));

        BidList result = bidService.getBidListById(1);

        assertEquals(result,bidList);
        assertThat(1).isEqualTo(result.getBidListId());
        assertThat("account").isEqualTo(result.getAccount());
        assertThat("type1").isEqualTo(result.getType());
        assertThat(2.0).isEqualTo(result.getBidQuantity());
        assertThat(4.0).isEqualTo(result.getAskQuantity());
        verify(bidListRepository,times(1)).findById(anyInt());
    }

    @Test
    public void getBidListByIdDoTrowBidListNotFounfException() {
        when(bidListRepository.findById(anyInt())).thenReturn(Optional.empty());
        assertThrows(BidListNotFoundException.class,()->bidService.getBidListById(1));
        verify(bidListRepository).findById(isA(Integer.class));
    }

    @Test
    public void updateBidList() {
        BidList bidListBeforeUpdate = new BidList();
            bidListBeforeUpdate.setBidListId(1);
            bidListBeforeUpdate.setAccount("account1");
            bidListBeforeUpdate.setType("type1");
            bidListBeforeUpdate.setBidQuantity(2.0);
        BidList bidListAfterUpdate = new BidList();
            bidListAfterUpdate.setBidListId(1);
            bidListAfterUpdate.setAccount("account2");
            bidListAfterUpdate.setType("type2");
            bidListAfterUpdate.setBidQuantity(6.0);
        when(bidListRepository.getById(1)).thenReturn(bidListBeforeUpdate);
        when(bidListRepository.save(isA(BidList.class))).thenReturn(bidListAfterUpdate);

        BidList result = bidService.updateBidList(bidListBeforeUpdate);

        assertEquals(result,bidListAfterUpdate);
        assertThat(1).isEqualTo(result.getBidListId());
        assertThat("account2").isEqualTo(result.getAccount());
        assertThat("type2").isEqualTo(result.getType());
        assertThat(6.0).isEqualTo(result.getBidQuantity());

    }

    @Test
    public void updateBidListDoThrowBidListNotFounfException() {
        BidList bidList = new BidList();
        bidList.setBidListId(1);
        bidList.setAccount("account");
        bidList.setType("type");
        bidList.setBidQuantity(2.0);

        when(bidListRepository.getById(anyInt())).thenReturn(null);

        assertThrows(BidListNotFoundException.class,()->bidService.updateBidList(bidList));
        verify(bidListRepository).getById(isA(Integer.class));
    }

    @Test
    public void deleteBidList() {
        BidList bidList = new BidList();
        bidList.setBidListId(1);
        bidList.setAccount("account");
        bidList.setType("type");
        bidList.setBidQuantity(2.0);

        bidService.deleteBidList(bidList.getBidListId());

        verify(bidListRepository).deleteById(1);
    }
}