package com.nnk.springboot.service;

import com.nnk.springboot.domain.Trade;
import com.nnk.springboot.exception.TradeNotFoundException;
import com.nnk.springboot.repositories.TradeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TradeService {

    private final Logger logger = LoggerFactory.getLogger(TradeService.class);

    @Autowired
    TradeRepository tradeRepository;

    /**
     * Get a list of all trade
     * @return list of Trade containing all trades
     */
    public List<Trade> getAllTrade() {
        logger.info("Service: display trade list");
        return tradeRepository.findAll();
    }

    /**
     * Save a new Trade
     * @param trade
     * @return Trade saved
     */
    public Trade saveTrade (Trade trade) {
        logger.info("Service: save new trade to DB");
        return tradeRepository.save(trade);
    }

    /**
     * Get trade by id.
     * @param id Trade that containing id of the Trade
     * @return Instance of Trade
     */
    public Trade getTradeNameById(Integer id) {
        Optional<Trade> trade = tradeRepository.findById(id);
        if(trade.isPresent()){
            logger.info("Service: Trade with Id "+id+" was found");
            return trade.get();
        }else {
            throw new TradeNotFoundException("Trade not found");
        }
    }

    /**
     * Update existing trade
     * @param trade instance
     * @return the Trade updated
     */
    public Trade updateTrade(Trade trade) {
        Trade tradeForUpdate = tradeRepository.getById(trade.getTradeId());
        tradeForUpdate.setAccount(trade.getAccount());
        tradeForUpdate.setType(trade.getType());
        tradeForUpdate.setBuyQuantity(trade.getBuyQuantity());
        logger.info("Service: Trade updated with ID: " + trade.getTradeId());
        return tradeRepository.save(tradeForUpdate);
    }

    /**
     * Delete a Trade by id
     * @param id Integer that containing id of the Trade
     */
    public void deleteTrade(Integer id) {
        logger.info("Service: Trade deleted with ID: " + id);
        tradeRepository.deleteById(id);
    }
}
