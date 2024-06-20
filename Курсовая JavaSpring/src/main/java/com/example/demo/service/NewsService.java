package com.example.demo.service;

import com.example.demo.entity.News;
import com.example.demo.entity.Shop;
import com.example.demo.repository.NewsRepository;
import com.example.demo.repository.ShopRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@Scope("singleton")
public class NewsService {
    private final NewsRepository newsRepository;

    @Autowired
    public NewsService(NewsRepository newsRepository) {
        this.newsRepository = newsRepository;
    }

    public void saveNews(News news) {
        newsRepository.save(news);
    }

    public List<News> getNews() {
        return newsRepository.findAll();
    }


    public News getNewsById(Long id) {
        return newsRepository.findById(id).orElse(null);
    }


    public News updateNews(Long id, News news) {
        News existingNews = newsRepository.findById(id).orElse(null);
        if (existingNews != null) {
            existingNews.setName(news.getName());
            existingNews.setComment(news.getComment());
            
            return newsRepository.save(existingNews);
        }
        return null;
    }


    public String deleteNews(Long id) {
        newsRepository.deleteById(id);
        return "news with ID " + id + " has been deleted.";
    }
    public List<News> findByName(String name) {
        return newsRepository.findByNameContainingIgnoreCase(name);
    }
    public List<News> findAll() {
        return newsRepository.findAll();
    }
}
