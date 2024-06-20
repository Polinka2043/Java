package com.example.demo.view;

import com.example.demo.entity.News;
import com.example.demo.entity.Promotions;
import com.example.demo.service.NewsService;
import com.example.demo.service.PromotionsService;
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

@Route(value = "promotions", layout = MainLayout.class)
@RolesAllowed("ROLE_USER")
public class PromotionsView extends VerticalLayout{
    private final PromotionsService promotionsService;
    private final TextField nameField = new TextField();
    private final Button searchButton = new Button("Search");


    private final Grid<Promotions> grid = new Grid<>(Promotions.class);

    @Autowired
    public PromotionsView(PromotionsService promotionsService) {
        this.promotionsService = promotionsService;
        HorizontalLayout buttonsLayout = new HorizontalLayout();
        buttonsLayout.add(nameField);
        buttonsLayout.add(searchButton);
        buttonsLayout.add(new Button("Add Promotions", click -> navigateToAddPromotions()));
        buttonsLayout.add(new Button("View Promotions", click -> {
            Promotions selectedPromotions = grid.asSingleSelect().getValue();
            if (selectedPromotions != null) {
                navigateToViewPromotions(selectedPromotions);
            } else {
                Notification.show("Please select a promo first.");
            }
        }));
        add(buttonsLayout);
        searchButton.addClickListener(e -> updateList());
        updateList();
        add(grid);
        listPromotions();
    }
    private void listPromotions() {
        grid.setItems(promotionsService.getPromotions());
    }
    private void updateList() {
        List<Promotions> staffList;
        if (nameField.getValue() == null || nameField.getValue().isEmpty()) {
            staffList = promotionsService.findAll();
        } else {
            staffList = promotionsService.findByName(nameField.getValue());
        }
        grid.setItems(staffList);
    }
    private void navigateToAddPromotions() {
        UI.getCurrent().navigate(AddPromotionsView.class);
    }
    private void navigateToViewPromotions(Promotions promotions) {
        UI.getCurrent().navigate("viewPromotions/" + promotions.getId());
    }
}
@Route("addPromotions")
@RolesAllowed("ROLE_USER")
class AddPromotionsView extends VerticalLayout {
    @Autowired
    public AddPromotionsView(PromotionsService promotionsService) {
        Dialog dialog = new Dialog();
        FormLayout formLayout = new FormLayout();
        TextField nameField = new TextField();
        nameField.setLabel("Promotions Name");
        formLayout.add(nameField);
        DatePicker birthDateField = new DatePicker();
        birthDateField.setLabel("Date");
        formLayout.add(birthDateField);
        IntegerField commentField = new IntegerField();
        commentField.setLabel("Discount");
        formLayout.add(commentField);
        Button saveButton = new Button("Save", click -> {
            if (nameField.isEmpty() ||  birthDateField.isEmpty() || commentField.isEmpty()) {
                Notification.show("Please fill in all fields.");
            } else {
                Promotions promotions = new Promotions();
                promotions.setName(nameField.getValue());
                promotions.setStartDate(birthDateField.getValue());
                promotions.setDiscount((long) commentField.getValue());
                promotionsService.savePromotions(promotions);
                dialog.close();
                UI.getCurrent().navigate("promotions");
            }
        });
        formLayout.add(saveButton);
        dialog.add(formLayout);
        dialog.open();
    }
}
@Route("viewPromotions")
@RolesAllowed("USER")
class ViewPromotionsView extends VerticalLayout implements HasUrlParameter<String> {
    private final PromotionsService promotionsService;

    @Autowired
    public ViewPromotionsView(PromotionsService promotionsService) {
        this.promotionsService = promotionsService;
    }

    @Override
    public void setParameter(BeforeEvent event, String promotionsId) {
        Promotions promotions = promotionsService.getPromotionsById(Long.valueOf(promotionsId));
        if (promotions != null) {
            viewPromotions(promotions);
        } else {
            Notification.show("Promotions not found.");
        }
    }

    public void viewPromotions(Promotions promotions) {
        Dialog dialog = new Dialog();
        FormLayout formLayout = new FormLayout();
        TextField nameField = new TextField("Promo Name", promotions.getName(), "");
        nameField.setReadOnly(true);
        formLayout.add(nameField);
        DatePicker birthDateField = new DatePicker("Date", promotions.getStartDate());
        birthDateField.setReadOnly(true);
        formLayout.add(birthDateField);
        IntegerField countField = new IntegerField("Discount", String.valueOf(promotions.getDiscount().intValue()));
        countField.setReadOnly(true);
        formLayout.add(countField);
        dialog.add(formLayout);
        dialog.open();
        Notification.show("Promotions view.");
        Button backButton = new Button("Back to Promotions", click -> {
            dialog.close();
            Notification.show("Promotions view closed.");
            UI.getCurrent().navigate("promotions");
        });
        formLayout.add(backButton);
        dialog.add(formLayout);
        dialog.open();
    }
}