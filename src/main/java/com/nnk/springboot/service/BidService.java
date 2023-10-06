package com.nnk.springboot.service;

import com.nnk.springboot.domain.BidList;
import com.nnk.springboot.exception.BidListNotFoundException;
import com.nnk.springboot.repositories.BidListRepository;
import org.joda.time.LocalDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BidService {

    private final Logger logger = LoggerFactory.getLogger(BidService.class);

    @Autowired
    BidListRepository bidListRepository;

    /**
     * Get a list of all users
     * @return list of User containing all users
     */
    public List<BidList> getAllBidList() {
        logger.info("Service: display bids list");
        return bidListRepository.findAll();
    }

    /**
     * Save a new BidList
     * @param bidList
     * @return BidList saved
     */
    public BidList saveBidList (BidList bidList) {
        logger.info("Service: save new bidList to DB");
        bidList.setCreationDate(new LocalDateTime().now());
        return bidListRepository.save(bidList);
    }

    /**
     * Get bidList by id.
     * @param id Integer that containing id of the BidList
     * @return Instance of BidList
     */
    public BidList getBidListById(Integer id) {
        Optional<BidList> bidList = bidListRepository.findById(id);
        if(bidList.isPresent()) {
            logger.info("Service: BidList with Id "+id+" was found");
            return bidList.get();
        } else {
            throw new BidListNotFoundException("BidList not found");
        }
    }

    /**
     * Update existing bidList
     * @param bidList instance
     * @return the BidList updated
     */
    public BidList updateBidList (BidList bidList) {
        BidList bidListForUpdate = bidListRepository.getById(bidList.getBidListId());
        if(bidListForUpdate==null){
            throw new BidListNotFoundException("BidList not found");
        }
        bidListForUpdate.setAccount(bidList.getAccount());
        bidListForUpdate.setType(bidList.getType());
        bidListForUpdate.setBidQuantity(bidList.getBidQuantity());
        logger.info("Service: User updated with ID: " + bidList.getBidListId());
        return bidListRepository.save(bidListForUpdate);
    }

    /**
     * Delete a BidList by id
     * @param id Integer that containing id of the BidList
     */
    public void deleteBidList(Integer id) {
        logger.info("Service: BidList deleted with ID: " + id);
        bidListRepository.deleteById(id);
    }
}
