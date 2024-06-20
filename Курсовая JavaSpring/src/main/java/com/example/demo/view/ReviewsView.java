package com.example.demo.view;

import com.example.demo.entity.Promotions;
import com.example.demo.entity.Reviews;
import com.example.demo.service.PromotionsService;
import com.example.demo.service.ReviewsService;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Route(value = "reviews", layout = MainLayout.class)
@RolesAllowed("ROLE_USER")
public class ReviewsView extends VerticalLayout {
    private final ReviewsService reviewsService;
    private final TextField nameField = new TextField();
    private final Button searchButton = new Button("Search");


    private final Grid<Reviews> grid = new Grid<>(Reviews.class);

    @Autowired
    public ReviewsView(ReviewsService reviewsService) {
        this.reviewsService = reviewsService;
        HorizontalLayout buttonsLayout = new HorizontalLayout();
        buttonsLayout.add(nameField);
        buttonsLayout.add(searchButton);
        buttonsLayout.add(new Button("Add Reviews", click -> navigateToAddReviews()));
        buttonsLayout.add(new Button("View Reviews", click -> {
            Reviews selectedReviews = grid.asSingleSelect().getValue();
            if (selectedReviews != null) {
                navigateToViewReviews(selectedReviews);
            } else {
                Notification.show("Please select a reviews first.");
            }
        }));
        add(buttonsLayout);
        searchButton.addClickListener(e -> updateList());
        updateList();
        add(grid);
        listReviews();
    }
    private void listReviews() {
        grid.setItems(reviewsService.getReviews());
    }
    private void updateList() {
        List<Reviews> reviewsList;
        if (nameField.getValue() == null || nameField.getValue().isEmpty()) {
            reviewsList = reviewsService.findAll();
        } else {
            reviewsList = reviewsService.findByComment(nameField.getValue());
        }
        grid.setItems(reviewsList);
    }
    private void navigateToAddReviews() {
        UI.getCurrent().navigate(AddReviewsView.class);
    }
    private void navigateToViewReviews(Reviews reviews) {
        UI.getCurrent().navigate("viewReviews/" + reviews.getId());
    }
}
@Route("addReviews")
@RolesAllowed("ROLE_USER")
class AddReviewsView extends VerticalLayout {
    @Autowired
    public AddReviewsView(ReviewsService reviewsService) {
        Dialog dialog = new Dialog();
        FormLayout formLayout = new FormLayout();
        DatePicker birthDateField = new DatePicker();
        birthDateField.setLabel("Date");
        formLayout.add(birthDateField);
        TextField commentField = new TextField();
        commentField.setLabel("Comment");
        formLayout.add(commentField);
        Button saveButton = new Button("Save", click -> {
            if (birthDateField.isEmpty() || commentField.isEmpty()) {
                Notification.show("Please fill in all fields.");
            } else {
                Reviews reviews = new Reviews();
                reviews.setComment(commentField.getValue());
                reviews.setBirthDate(birthDateField.getValue());
                reviewsService.saveReviews(reviews);
                dialog.close();
                UI.getCurrent().navigate("reviews");
            }
        });
        formLayout.add(saveButton);
        dialog.add(formLayout);
        dialog.open();
    }
}
@Route("viewReviews")
@RolesAllowed("ROLE_USER")
class ViewReviewsView extends VerticalLayout implements HasUrlParameter<String> {
    private final ReviewsService reviewsService;

    @Autowired
    public ViewReviewsView(ReviewsService reviewsService) {
        this.reviewsService = reviewsService;
    }

    @Override
    public void setParameter(BeforeEvent event, String reviewsId) {
        Reviews reviews = reviewsService.getReviewsById(Long.valueOf(reviewsId));
        if (reviews != null) {
            viewReviews(reviews);
        } else {
            Notification.show("reviews not found.");
        }
    }

    public void viewReviews(Reviews reviews) {
        Dialog dialog = new Dialog();
        FormLayout formLayout = new FormLayout();
        TextField nameField = new TextField("Reviews Name", reviews.getComment(), "");
        nameField.setReadOnly(true);
        formLayout.add(nameField);
        DatePicker birthDateField = new DatePicker("Date", reviews.getBirthDate());
        birthDateField.setReadOnly(true);
        formLayout.add(birthDateField);
        dialog.add(formLayout);
        dialog.open();
        Notification.show("Reviews view.");
        Button backButton = new Button("Back to Reviews", click -> {
            dialog.close();
            Notification.show("Reviews view closed.");
            UI.getCurrent().navigate("reviews");
        });
        formLayout.add(backButton);
        dialog.add(formLayout);
        dialog.open();
    }
}