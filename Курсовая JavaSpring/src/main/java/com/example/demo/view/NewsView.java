package com.example.demo.view;

import com.example.demo.entity.News;
import com.example.demo.entity.Staff;
import com.example.demo.service.NewsService;
import com.example.demo.service.StaffService;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Route(value = "news", layout = MainLayout.class)
@RolesAllowed("USER")
public class NewsView extends VerticalLayout{
    private final NewsService newsService;
    private final TextField nameField = new TextField();
    private final Button searchButton = new Button("Search");


    private final Grid<News> grid = new Grid<>(News.class);

    @Autowired
    public NewsView(NewsService newsService) {
        this.newsService = newsService;
        HorizontalLayout buttonsLayout = new HorizontalLayout();
        buttonsLayout.add(nameField);
        buttonsLayout.add(searchButton);
        buttonsLayout.add(new Button("Add News", click -> navigateToAddNews()));
        buttonsLayout.add(new Button("View News", click -> {
            News selectedNews = grid.asSingleSelect().getValue();
            if (selectedNews != null) {
                navigateToViewNews(selectedNews);
            } else {
                Notification.show("Please select a news first.");
            }
        }));
        add(buttonsLayout);
        searchButton.addClickListener(e -> updateList());
        updateList();
        add(grid);
        listNews();
    }
    private void listNews() {
        grid.setItems(newsService.getNews());
    }
    private void updateList() {
        List<News> staffList;
        if (nameField.getValue() == null || nameField.getValue().isEmpty()) {
            staffList = newsService.findAll();
        } else {
            staffList = newsService.findByName(nameField.getValue());
        }
        grid.setItems(staffList);
    }
    private void navigateToAddNews() {
        UI.getCurrent().navigate(AddNewsView.class);
    }
    private void navigateToViewNews(News news) {
        UI.getCurrent().navigate("viewNews/" + news.getId());
    }
}
@Route("addNews")
@RolesAllowed("USER")
class AddNewsView extends VerticalLayout {
    @Autowired
    public AddNewsView(NewsService newsService) {
        Dialog dialog = new Dialog();
        FormLayout formLayout = new FormLayout();
        TextField nameField = new TextField();
        nameField.setLabel("News Name");
        formLayout.add(nameField);
        DatePicker birthDateField = new DatePicker();
        birthDateField.setLabel("Date");
        formLayout.add(birthDateField);
        TextField commentField = new TextField();
        commentField.setLabel("Comment");
        formLayout.add(commentField);
        Button saveButton = new Button("Save", click -> {
            if (nameField.isEmpty() ||  birthDateField.isEmpty() || commentField.isEmpty()) {
                Notification.show("Please fill in all fields.");
            } else {
                News news = new News();
                news.setName(nameField.getValue());
                news.setDate(birthDateField.getValue());
                news.setComment(commentField.getValue());
                newsService.saveNews(news);
                dialog.close();
                UI.getCurrent().navigate("news");
            }
        });
        formLayout.add(saveButton);
        dialog.add(formLayout);
        dialog.open();
    }
}
@Route("viewNews")
@RolesAllowed("USER")
class ViewNewsView extends VerticalLayout implements HasUrlParameter<String> {
    private final NewsService newsService;

    @Autowired
    public ViewNewsView(NewsService newsService) {
        this.newsService = newsService;
    }

    @Override
    public void setParameter(BeforeEvent event, String newsId) {
        News news = newsService.getNewsById(Long.valueOf(newsId));
        if (news != null) {
            viewNews(news);
        } else {
            Notification.show("news not found.");
        }
    }

    public void viewNews(News news) {
        Dialog dialog = new Dialog();
        FormLayout formLayout = new FormLayout();
        TextField nameField = new TextField("news Name", news.getName(), "");
        nameField.setReadOnly(true);
        formLayout.add(nameField);
        DatePicker birthDateField = new DatePicker("Date", news.getDate());
        birthDateField.setReadOnly(true);
        formLayout.add(birthDateField);
        TextField positionField = new TextField("Comment", news.getComment(), "");
        positionField.setReadOnly(true);
        formLayout.add(positionField);
        dialog.add(formLayout);
        dialog.open();
        Notification.show("News view.");
        Button backButton = new Button("Back to News", click -> {
            dialog.close();
            Notification.show("News view closed.");
            UI.getCurrent().navigate("news");
        });
        formLayout.add(backButton);
        dialog.add(formLayout);
        dialog.open();
    }
}