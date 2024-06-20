package com.example.demo.view;

import com.example.demo.entity.Stock;
import com.example.demo.service.StockService;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
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


@Route(value = "stocks", layout = MainLayout.class)
@RolesAllowed("USER")
public class StockView extends VerticalLayout {
    private final StockService stockService;
    private final TextField nameField = new TextField();
    private final Button searchButton = new Button("Search");


    private final Grid<Stock> grid = new Grid<>(Stock.class);

    @Autowired
    public StockView(StockService stockService) {
        this.stockService = stockService;
        HorizontalLayout buttonsLayout = new HorizontalLayout();
        buttonsLayout.add(nameField);
        buttonsLayout.add(searchButton);
        buttonsLayout.add(new Button("Add Stock", click -> navigateToAddStock()));
        buttonsLayout.add(new Button("View Stock", click -> {
            Stock selectedStock = grid.asSingleSelect().getValue();
            if (selectedStock != null) {
                navigateToViewStock(selectedStock);
            } else {
                Notification.show("Please select a stock first.");
            }
        }));
        add(buttonsLayout);
        searchButton.addClickListener(e -> updateList());
        updateList();
        add(grid);
        listStocks();
    }
    private void updateList() {
        List<Stock> carList;
        if (nameField.getValue() == null || nameField.getValue().isEmpty()) {
            carList = stockService.findAll();
        } else {
            carList = stockService.findByName(nameField.getValue());
        }
        grid.setItems(carList);
    }
    private void listStocks() {
        grid.setItems(stockService.getStocks());
    }
    private void navigateToAddStock() {
        UI.getCurrent().navigate(AddStockView.class);
    }
    private void navigateToViewStock(Stock stock) {
        UI.getCurrent().navigate("viewStock/" + stock.getId());
    }
}
@Route("addStock")
@RolesAllowed("USER")
class AddStockView extends VerticalLayout {

    @Autowired
    public AddStockView(StockService stockService) {
        Dialog dialog = new Dialog();
        FormLayout formLayout = new FormLayout();
        TextField nameField = new TextField();
        nameField.setLabel("Stock Name");
        formLayout.add(nameField);
        TextField addressField = new TextField();
        addressField.setLabel("Address");
        formLayout.add(addressField);
        IntegerField countField = new IntegerField();
        countField.setLabel("Count");
        formLayout.add(countField);
        IntegerField categoryCountField = new IntegerField();
        categoryCountField.setLabel("Category Count");
        formLayout.add(categoryCountField);
        Button saveButton = new Button("Save", click -> {
            if (nameField.isEmpty() ||  addressField.isEmpty() ||countField.isEmpty()||categoryCountField.isEmpty()) {
                Notification.show("Please fill in all fields.");
            } else {
                Stock stock = new Stock();
                stock.setName(nameField.getValue());
                stock.setAddress(addressField.getValue());
                stock.setCount((long) countField.getValue());
                stock.setCategoryCount((long) categoryCountField.getValue());
                stockService.saveStock(stock);
                dialog.close();
                UI.getCurrent().navigate("stocks");
            }
        });
        formLayout.add(saveButton);
        dialog.add(formLayout);
        dialog.open();
    }
}

@Route("viewStock")
@RolesAllowed("USER")
class ViewStockView extends VerticalLayout implements HasUrlParameter<String> {
    private final StockService stockService;

    @Autowired
    public ViewStockView(StockService stockService) {
        this.stockService = stockService;
    }

    @Override
    public void setParameter(BeforeEvent event, String stockId) {
        Stock stock = stockService.getStockById(Long.valueOf(stockId));
        if (stock != null) {
            viewStock(stock);
        } else {
            Notification.show("Stock not found.");
        }
    }


    public void viewStock(Stock stock) {
        Dialog dialog = new Dialog();
        FormLayout formLayout = new FormLayout();

        TextField nameField = new TextField("Stock Name", stock.getName(), "");
        nameField.setReadOnly(true);
        formLayout.add(nameField);
        TextField addressField = new TextField("Stock Address", stock.getAddress(), "");
        addressField.setReadOnly(true);
        formLayout.add(addressField);
        IntegerField countField = new IntegerField("Stock Count", String.valueOf(stock.getCount().intValue()));
        countField.setReadOnly(true);
        formLayout.add(countField);
        dialog.add(formLayout);
        dialog.open();

        Notification.show("Stock view.");
        Button backButton = new Button("Back to Stocks", click -> {
            dialog.close();
            Notification.show("Stock view closed.");
            UI.getCurrent().navigate("stocks");

        });
        formLayout.add(backButton);
        dialog.add(formLayout);
        dialog.open();
    }
}