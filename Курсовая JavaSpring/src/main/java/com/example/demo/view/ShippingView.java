package com.example.demo.view;

import com.example.demo.entity.Delivery;
import com.example.demo.entity.Human;
import com.example.demo.entity.Shipping;
import com.example.demo.service.DeliveryService;
import com.example.demo.service.HumanService;
import com.example.demo.service.ShippingService;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.listbox.ListBox;
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

@Route(value = "shipping", layout = MainLayout.class)
@RolesAllowed("ROLE_USER")
public class ShippingView extends VerticalLayout {
    private final ShippingService shippingService;
    private final HumanService humanService;
    private final DeliveryService deliveryService;
    private final TextField nameField = new TextField();
    private final Button searchButton = new Button("Search");

    private final Grid<Shipping> grid = new Grid<>(Shipping.class);
    private Grid<Human> humanGrid = new Grid<>(Human.class);
    private Grid<Delivery> deliveryGrid = new Grid<>(Delivery.class);
    @Autowired
    public ShippingView(ShippingService shippingService, HumanService humanService,DeliveryService deliveryService) {
        this.shippingService = shippingService;
        this.humanService = humanService;
        this.deliveryService = deliveryService;
        HorizontalLayout buttonsLayout = new HorizontalLayout();
        buttonsLayout.add(nameField);
        buttonsLayout.add(searchButton);
        buttonsLayout.add(new Button("Add Shipping", click -> navigateToAddShipping()));
        buttonsLayout.add(new Button("Delete Shipping", click -> {
            Shipping selectedShipping = grid.asSingleSelect().getValue();
            if (selectedShipping != null) {
                navigateToDeleteShipping(selectedShipping);
            } else {
                Notification.show("Please select a Shipping first.");
            }
        }));
        buttonsLayout.add(new Button("View Shipping", click -> {
            Shipping selectedShipping = grid.asSingleSelect().getValue();
            if (selectedShipping != null) {
                navigateToViewShipping(selectedShipping);
            } else {
                Notification.show("Please select a Shipping first.");
            }
        }));
        buttonsLayout.add(new Button("Add Human", click -> navigateToAddHuman()));
        buttonsLayout.add(new Button("Delete Human", click -> {
            Human selectedHuman = humanGrid.asSingleSelect().getValue();
            if (selectedHuman!= null) {
                navigateToDeleteHuman(selectedHuman);
            } else {
                Notification.show("Please select a Human first.");
            }
        }));
        buttonsLayout.add(new Button("Add Delivery", click -> navigateToAddDelivery()));
        buttonsLayout.add(new Button("Delete Delivery", click -> {
            Delivery selectedDelivery = deliveryGrid.asSingleSelect().getValue();
            if (selectedDelivery!= null) {
                navigateToDeleteDelivery(selectedDelivery);
            } else {
                Notification.show("Please select a Delivery first.");
            }
        }));
        add(buttonsLayout);
        searchButton.addClickListener(e -> updateList());
        updateList();
        add(grid);
        listShippings();
        add(humanGrid);
        listHuman();
        add(deliveryGrid);
        listDelivery();
    }

    private void listHuman() {
        List<Human> humanList = humanService.getHumans();
        humanGrid.setItems(humanList);
    }
    private void listDelivery() {
        List<Delivery> deliveryList = deliveryService.getDelivers();
        deliveryGrid.setItems(deliveryList);
    }
    private void updateList() {
        List<Shipping> shippingList;
        if (nameField.getValue() == null || nameField.getValue().isEmpty()) {
            shippingList = shippingService.findAll();
        } else {
            shippingList = shippingService.findByName(nameField.getValue());
        }
        grid.setItems(shippingList);
    }

    private void listShippings() {
        grid.setItems(shippingService.getShippings());
    }

    private void navigateToAddShipping() {
        UI.getCurrent().navigate(AddShippingView.class);
    }
    private void navigateToAddHuman() {
        UI.getCurrent().navigate(AddHumanView.class);
    }
    private void navigateToAddDelivery() {
        UI.getCurrent().navigate(AddDeliveryView.class);
    }
    private void navigateToDeleteShipping(Shipping shipping) {
        UI.getCurrent().navigate("deleteShipping/" + shipping.getId());
    }

    private void navigateToViewShipping(Shipping shipping) {
        UI.getCurrent().navigate("viewShipping/" + shipping.getId());
    }
    private void navigateToDeleteHuman(Human human) {
        UI.getCurrent().navigate("deleteHuman/" + human.getId());
    }
    private void navigateToDeleteDelivery(Delivery delivery) {
        UI.getCurrent().navigate("deleteDelivery/" + delivery.getId());
    }

}


@Route("addShipping")
@RolesAllowed("ROLE_USER")
class AddShippingView extends VerticalLayout {
    @Autowired
    public AddShippingView(ShippingService shippingService) {
        Dialog dialog = new Dialog();
        FormLayout formLayout = new FormLayout();
        TextField nameField = new TextField();
        nameField.setLabel("Name");
        formLayout.add(nameField);
        DatePicker birthDateField = new DatePicker();
        birthDateField.setLabel("locale Date");
        formLayout.add(birthDateField);
        Button saveButton = new Button("Save", click -> {
            if (nameField.isEmpty() ||  birthDateField.isEmpty() ) {
                Notification.show("Please fill in all fields.");
            } else {
                Shipping shipping = new Shipping();
                shipping.setName(nameField.getValue());
                shipping.setOrderDate(birthDateField.getValue());
                shippingService.saveShipping(shipping);
                dialog.close();
                UI.getCurrent().navigate("shipping");
            }
        });
        formLayout.add(saveButton);
        dialog.add(formLayout);
        dialog.open();
    }
}

@Route("addHuman")
@RolesAllowed("ROLE_USER")
class AddHumanView extends VerticalLayout {
    private final ShippingService shippingService;
    @Autowired
    public AddHumanView(HumanService humanService, ShippingService shippingService) {
        this.shippingService = shippingService;
        System.out.println("hahahaha");
        Dialog dialog = new Dialog();
        FormLayout formLayout = new FormLayout();
        TextField nameField = new TextField();
        nameField.setLabel("Name");
        formLayout.add(nameField);
        ListBox<Shipping> shippingIdField = getShippingListBox();
        formLayout.add(shippingIdField);
        Button saveButton = new Button("Save", click -> {
            if (nameField.isEmpty()) {
                Notification.show("Please fill in all fields.");
            } else {
                Human human = new Human();
                human.setName(nameField.getValue());
                human.setShipping(shippingIdField.getValue());
                humanService.saveHuman(human);
                dialog.close();
                UI.getCurrent().navigate("shipping");
            }
        });
        formLayout.add(saveButton);
        dialog.add(formLayout);
        dialog.open();
    }
    private ListBox<Shipping> getShippingListBox() {
        ListBox<Shipping> shippingField = new ListBox<>();
        List<Shipping> shippingList = shippingService.getShippings();
        if (!shippingList.isEmpty()) {
            shippingField.setItems(shippingList);
            shippingField.setValue(shippingList.get(0));
        } else {
            shippingField.setEnabled(false);
        }
        return shippingField;
    }
}

@Route("addDelivery")
@RolesAllowed("ROLE_USER")
class AddDeliveryView extends VerticalLayout {
    private final ShippingService shippingService;
    @Autowired
    public AddDeliveryView(DeliveryService deliveryService, ShippingService shippingService) {
        this.shippingService = shippingService;
        Dialog dialog = new Dialog();
        FormLayout formLayout = new FormLayout();
        TextField nameField = new TextField();
        nameField.setLabel("Name");
        formLayout.add(nameField);
        ListBox<Shipping> shippingIdField = getShippingListBox();
        formLayout.add(shippingIdField);
        Button saveButton = new Button("Save", click -> {
            if (nameField.isEmpty()) {
                Notification.show("Please fill in all fields.");
            } else {
                Delivery delivery = new Delivery();
                delivery.setName(nameField.getValue());
                delivery.setShipping(shippingIdField.getValue());
                deliveryService.saveDelivery(delivery);
                dialog.close();
                UI.getCurrent().navigate("shipping");
            }
        });
        formLayout.add(saveButton);
        dialog.add(formLayout);
        dialog.open();
    }
    private ListBox<Shipping> getShippingListBox() {
        ListBox<Shipping> shippingIdField = new ListBox<>();
        List<Shipping> shippingList = shippingService.getShippings();
        if (!shippingList.isEmpty()) {
            shippingIdField.setItems(shippingList);
            shippingIdField.setValue(shippingList.get(0));
        } else {
            shippingIdField.setEnabled(false);
        }
        return shippingIdField;
    }
}

@Route("deleteShipping")
@RolesAllowed("ROLE_USER")
class DeleteShippingView extends VerticalLayout implements HasUrlParameter<String> {
    private final ShippingService shippingService;

    @Autowired
    public DeleteShippingView(ShippingService shippingService) {
        this.shippingService = shippingService;
    }

    @Override
    public void setParameter(BeforeEvent event, String shippingId) {
        Shipping shipping = shippingService.getShippingById(Long.valueOf(shippingId));
        if (shipping != null) {
            confirmAndDeleteShipping(shipping);
        } else {
            Notification.show("shipping not found.");
        }
    }

    public void confirmAndDeleteShipping(Shipping shipping) {
        Dialog dialog = new Dialog();
        FormLayout formLayout = new FormLayout();

        TextField nameField = new TextField("Shipping Name", shipping.getName(), "");
        nameField.setReadOnly(true);
        formLayout.add(nameField);

        Button confirmButton = new Button("Confirm Delete", click -> {
            shippingService.deleteShipping(shipping.getId());
            dialog.close();
            Notification.show("Shipping deleted.");
            UI.getCurrent().navigate("shipping");
        });

        Button cancelButton = new Button("Cancel", click -> {
            dialog.close();
            Notification.show("The Shipping has not been deleted.");
            UI.getCurrent().navigate("Shipping");
        });

        formLayout.add(confirmButton, cancelButton);
        dialog.add(formLayout);
        dialog.open();
    }
}

@Route("viewShipping")
@RolesAllowed("ROLE_USER")
class ViewShippingView extends VerticalLayout implements HasUrlParameter<String> {
    private final ShippingService shippingService;

    @Autowired
    public ViewShippingView(ShippingService shippingService) {
        this.shippingService = shippingService;
    }

    @Override
    public void setParameter(BeforeEvent event, String shippingId) {
        Shipping shipping = shippingService.getShippingById(Long.valueOf(shippingId));
        if (shipping != null) {
            viewShipping(shipping);
        } else {
            Notification.show("shipping not found.");
        }
    }



    public void viewShipping(Shipping shipping) {
        Dialog dialog = new Dialog();
        FormLayout formLayout = new FormLayout();

        TextField nameField = new TextField("shipping Name",shipping.getName(), "");
        nameField.setReadOnly(true);
        formLayout.add(nameField);

        DatePicker birthDateField = new DatePicker("Date", shipping.getOrderDate());
        birthDateField.setReadOnly(true);
        formLayout.add(birthDateField);

        dialog.add(formLayout);
        dialog.open();


        Notification.show("shipping view.");
        Button backButton = new Button("Back to shipping", click -> {
            dialog.close();
            Notification.show("shipping view closed.");
            UI.getCurrent().navigate("shipping");

        });

        formLayout.add(backButton);
        dialog.add(formLayout);
        dialog.open();


    }
}
@Route("deleteHuman")
@RolesAllowed("ROLE_USER")
class DeleteHumanView extends VerticalLayout implements HasUrlParameter<String> {
    private final HumanService humanService;

    @Autowired
    public DeleteHumanView(HumanService humanService) {
        this.humanService = humanService;
    }

    @Override
    public void setParameter(BeforeEvent event, String humanId) {
        Human human = humanService.getHumanById(Long.valueOf(humanId));
        if (human != null) {
            confirmAndDeleteHuman(human);
        } else {
            Notification.show("Human not found.");
        }
    }

    public void confirmAndDeleteHuman(Human human) {
        Dialog dialog = new Dialog();
        FormLayout formLayout = new FormLayout();

        TextField nameField = new TextField("human Name", human.getName(), "");
        nameField.setReadOnly(true);
        formLayout.add(nameField);

        Button confirmButton = new Button("Confirm Delete", click -> {
            humanService.deleteHuman(human.getId());
            dialog.close();
            Notification.show("human deleted.");
            UI.getCurrent().navigate("shipping");
        });

        Button cancelButton = new Button("Cancel", click -> {
            dialog.close();
            Notification.show("The human has not been deleted.");
            UI.getCurrent().navigate("shipping");
        });

        formLayout.add(confirmButton, cancelButton);
        dialog.add(formLayout);
        dialog.open();
    }
}
@Route("deleteDelivery")
@RolesAllowed("ROLE_USER")
class DeleteDeliveryView extends VerticalLayout implements HasUrlParameter<String> {
    private final DeliveryService deliveryService;

    @Autowired
    public DeleteDeliveryView(DeliveryService deliveryService) {
        this.deliveryService = deliveryService;
    }

    @Override
    public void setParameter(BeforeEvent event, String deliveryId) {
        Delivery delivery = deliveryService.getDeliveryById(Long.valueOf(deliveryId));
        if (delivery != null) {
            confirmAndDeleteDelivery(delivery);
        } else {
            Notification.show("delivery not found.");
        }
    }

    public void confirmAndDeleteDelivery(Delivery delivery) {
        Dialog dialog = new Dialog();
        FormLayout formLayout = new FormLayout();

        TextField nameField = new TextField("delivery Name", delivery.getName(), "");
        nameField.setReadOnly(true);
        formLayout.add(nameField);

        Button confirmButton = new Button("Confirm Delete", click -> {
            deliveryService.deleteDelivery(delivery.getId());
            dialog.close();
            Notification.show("delivery deleted.");
            UI.getCurrent().navigate("shipping");
        });

        Button cancelButton = new Button("Cancel", click -> {
            dialog.close();
            Notification.show("The delivery has not been deleted.");
            UI.getCurrent().navigate("shipping");
        });

        formLayout.add(confirmButton, cancelButton);
        dialog.add(formLayout);
        dialog.open();
    }
}
