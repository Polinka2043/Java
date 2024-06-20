package com.example.demo.service;

import com.example.demo.entity.Shop;
import com.example.demo.entity.Stock;
import com.example.demo.repository.ShopRepository;
import com.example.demo.repository.StockRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Scope("singleton")
public class StockService {
    private final StockRepository stockRepository;

    @Autowired
    public StockService(StockRepository stockRepository) {
        this.stockRepository = stockRepository;
    }

    public void saveStock(Stock stock) {
        stockRepository.save(stock);
    }

    public List<Stock> getStocks() {
        return stockRepository.findAll();
    }


    public Stock getStockById(Long id) {
        return stockRepository.findById(id).orElse(null);
    }


    public Stock updateStock(Long id, Stock stock) {
        Stock existingStock = stockRepository.findById(id).orElse(null);
        if (existingStock != null) {
            existingStock.setName(stock.getName());
            existingStock.setAddress(stock.getAddress());
            existingStock.setCount(stock.getCount());
            existingStock.setCategoryCount(stock.getCategoryCount());
            return stockRepository.save(existingStock);
        }
        return null;
    }

    public String deleteStock(Long id) {
        stockRepository.deleteById(id);
        return "Stock with ID " + id + " has been deleted.";
    }
    public List<Stock> findByName(String name) {
        return stockRepository.findByNameContainingIgnoreCase(name);
    }
    public List<Stock> findAll() {
        return stockRepository.findAll();
    }

}
