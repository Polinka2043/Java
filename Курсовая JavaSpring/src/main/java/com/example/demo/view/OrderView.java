package com.example.demo.view;

import com.example.demo.entity.*;
import com.example.demo.service.*;
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

@Route(value = "orderzak", layout = MainLayout.class)
@RolesAllowed("ROLE_USER")
public class OrderView extends VerticalLayout {
    private final OrderService orderService;
    private final PersonService personService;
    private final PaymentsService paymentsService;
    private final TextField nameField = new TextField();
    private final Button searchButton = new Button("Search");

    private final Grid<OrderZak> grid = new Grid<>(OrderZak.class);
    private Grid<Person> personGrid = new Grid<>(Person.class);
    private Grid<Payments> paymentsGrid = new Grid<>(Payments.class);
    @Autowired
    public OrderView(OrderService orderService, PersonService personService,PaymentsService paymentsService) {
        this.orderService = orderService;
        this.personService = personService;
        this.paymentsService= paymentsService;
        HorizontalLayout buttonsLayout = new HorizontalLayout();
        buttonsLayout.add(nameField);
        buttonsLayout.add(searchButton);
        buttonsLayout.add(new Button("Add Order", click -> navigateToAddOrder()));
        buttonsLayout.add(new Button("Delete Order", click -> {
            OrderZak selectedOrder = grid.asSingleSelect().getValue();
            if (selectedOrder != null) {
                navigateToDeleteOrder(selectedOrder);
            } else {
                Notification.show("Please select a order first.");
            }
        }));
        buttonsLayout.add(new Button("View Order", click -> {
            OrderZak selectedOrder = grid.asSingleSelect().getValue();
            if (selectedOrder != null) {
                navigateToViewOrder(selectedOrder);
            } else {
                Notification.show("Please select a Order first.");
            }
        }));
        buttonsLayout.add(new Button("Add Person", click -> navigateToAddPerson()));
        buttonsLayout.add(new Button("Delete Person", click -> {
            Person selectedPerson = personGrid.asSingleSelect().getValue();
            if (selectedPerson!= null) {
                navigateToDeletePerson(selectedPerson);
            } else {
                Notification.show("Please select a Person first.");
            }
        }));
        buttonsLayout.add(new Button("Add Payments", click -> navigateToAddPayments()));
        buttonsLayout.add(new Button("Delete Payments", click -> {
            Payments selectedPayments = paymentsGrid.asSingleSelect().getValue();
            if (selectedPayments!= null) {
                navigateToDeletePayments(selectedPayments);
            } else {
                Notification.show("Please select a Payments first.");
            }
        }));
        add(buttonsLayout);
        searchButton.addClickListener(e -> updateList());
        updateList();
        add(grid);
        listOrders();
        add(personGrid);
        listPerson();
        add(paymentsGrid);
        listPayments();
    }

    private void listPerson() {
        List<Person> personList = personService.getPersons();
        personGrid.setItems(personList);
    }
    private void listPayments() {
        List<Payments> paymentsList = paymentsService.getPayments();
        paymentsGrid.setItems(paymentsList );
    }
    private void updateList() {
        List<OrderZak> orderList;
        if (nameField.getValue() == null || nameField.getValue().isEmpty()) {
            orderList = orderService.findAll();
        } else {
            orderList = orderService.findByName(nameField.getValue());
        }
        grid.setItems(orderList);
    }

    private void listOrders() {
        grid.setItems(orderService.getOrders());
    }

    private void navigateToAddOrder() {
        UI.getCurrent().navigate(AddOrderView.class);
    }
    private void navigateToAddPerson() {
        UI.getCurrent().navigate(AddPersonView.class);
    }
    private void navigateToAddPayments() {
        UI.getCurrent().navigate(AddPaymentsView.class);
    }
    private void navigateToDeleteOrder(OrderZak order) {
        UI.getCurrent().navigate("deleteOrder/" + order.getId());
    }

    private void navigateToViewOrder(OrderZak order) {
        UI.getCurrent().navigate("viewOrder/" + order.getId());
    }
    private void navigateToDeletePerson(Person person) {
        UI.getCurrent().navigate("deletePerson/" + person.getId());
    }
    private void navigateToDeletePayments(Payments payments) {
        UI.getCurrent().navigate("deletePayments/" + payments.getId());
    }

}


@Route("addOrder")
@RolesAllowed("ROLE_USER")
class AddOrderView extends VerticalLayout {
    @Autowired
    public AddOrderView(OrderService orderService) {
        Dialog dialog = new Dialog();
        FormLayout formLayout = new FormLayout();
        TextField nameField = new TextField();
        nameField.setLabel("Name");
        formLayout.add(nameField);
        TextField statusField = new TextField();
        statusField.setLabel("Status");
        formLayout.add(statusField);
        DatePicker birthDateField = new DatePicker();
        birthDateField.setLabel("Birth Date");
        formLayout.add(birthDateField);
        Button saveButton = new Button("Save", click -> {
            if (nameField.isEmpty() ||  birthDateField.isEmpty() ||statusField.isEmpty() ) {
                Notification.show("Please fill in all fields.");
            } else {
                OrderZak order = new OrderZak();
                order.setName(nameField.getValue());
                order.setOrderDate(birthDateField.getValue());
                order.setStatus(statusField.getValue());
                orderService.saveOrder(order);
                dialog.close();
                UI.getCurrent().navigate("orderzak");
            }
        });
        formLayout.add(saveButton);
        dialog.add(formLayout);
        dialog.open();
    }
}

@Route("addPerson")
@RolesAllowed("ROLE_USER")
class AddPersonView extends VerticalLayout {
    private final OrderService orderService;
    @Autowired
    public AddPersonView(PersonService personService, OrderService orderService) {
        this.orderService = orderService;
        Dialog dialog = new Dialog();
        FormLayout formLayout = new FormLayout();
        TextField nameField = new TextField();
        nameField.setLabel("Name");
        formLayout.add(nameField);
        ListBox<OrderZak> orderIdField = getOrderListBox();
        formLayout.add(orderIdField);
        Button saveButton = new Button("Save", click -> {
            if (nameField.isEmpty()) {
                Notification.show("Please fill in all fields.");
            } else {
                Person person = new Person();
                person.setName(nameField.getValue());
                person.setOrderzak(orderIdField.getValue());
                personService.savePerson(person);
                dialog.close();
                UI.getCurrent().navigate("orderzak");
            }
        });
        formLayout.add(saveButton);
        dialog.add(formLayout);
        dialog.open();
    }
    private ListBox<OrderZak> getOrderListBox() {
        ListBox<OrderZak> orderIdField = new ListBox<>();
        List<OrderZak> orderList = orderService.getOrders();
        if (!orderList.isEmpty()) {
            orderIdField.setItems(orderList);
            orderIdField.setValue(orderList.get(0));
        } else {
            orderIdField.setEnabled(false);
        }
        return orderIdField;
    }
}

@Route("addPayments")
@RolesAllowed("ROLE_USER")
class AddPaymentsView extends VerticalLayout {
    private final OrderService orderService;
    @Autowired
    public AddPaymentsView(PaymentsService paymentsService, OrderService orderService) {
        this.orderService = orderService;
        Dialog dialog = new Dialog();
        FormLayout formLayout = new FormLayout();
        TextField nameField = new TextField();
        nameField.setLabel("Name");
        formLayout.add(nameField);
        TextField methodField = new TextField();
        methodField.setLabel("method");
        formLayout.add(methodField);
        IntegerField amountField = new IntegerField();
        amountField.setLabel("Amount");
        formLayout.add(amountField);
        ListBox<OrderZak> orderIdField = getOrderListBox();
        formLayout.add(orderIdField);
        Button saveButton = new Button("Save", click -> {
            if (nameField.isEmpty()) {
                Notification.show("Please fill in all fields.");
            } else {
                Payments payments = new Payments();
                payments.setName(nameField.getValue());
                payments.setOrderzak(orderIdField.getValue());
                payments.setMethod(methodField.getValue());
                payments.setAmount((long) amountField.getValue());
                paymentsService.savePayments(payments);
                dialog.close();
                UI.getCurrent().navigate("orderzak");
            }
        });
        formLayout.add(saveButton);
        dialog.add(formLayout);
        dialog.open();
    }
    private ListBox<OrderZak> getOrderListBox() {
        ListBox<OrderZak> orderIdField = new ListBox<>();
        List<OrderZak> orderList = orderService.getOrders();
        if (!orderList.isEmpty()) {
            orderIdField.setItems(orderList);
            orderIdField.setValue(orderList.get(0));
        } else {
            orderIdField.setEnabled(false);
        }
        return orderIdField;
    }
}

@Route("deleteOrder")
@RolesAllowed("ROLE_USER")
class DeleteOrderView extends VerticalLayout implements HasUrlParameter<String> {
    private final OrderService orderService;

    @Autowired
    public DeleteOrderView(OrderService orderService) {
        this.orderService = orderService;
    }

    @Override
    public void setParameter(BeforeEvent event, String orderId) {
        OrderZak order = orderService.getOrderById(Long.valueOf(orderId));
        if (order != null) {
            confirmAndDeleteOrder(order);
        } else {
            Notification.show("Order not found.");
        }
    }

    public void confirmAndDeleteOrder(OrderZak order) {
        Dialog dialog = new Dialog();
        FormLayout formLayout = new FormLayout();

        TextField nameField = new TextField("Order Name", order.getName(), "");
        nameField.setReadOnly(true);
        formLayout.add(nameField);

        Button confirmButton = new Button("Confirm Delete", click -> {
            orderService.deleteOrder(order.getId());
            dialog.close();
            Notification.show("Order deleted.");
            UI.getCurrent().navigate("orderzak");
        });

        Button cancelButton = new Button("Cancel", click -> {
            dialog.close();
            Notification.show("The order has not been deleted.");
            UI.getCurrent().navigate("orderzak");
        });

        formLayout.add(confirmButton, cancelButton);
        dialog.add(formLayout);
        dialog.open();
    }
}

@Route("viewOrder")
@RolesAllowed("ROLE_USER")
class ViewOrderView extends VerticalLayout implements HasUrlParameter<String> {
    private final OrderService orderService;

    @Autowired
    public ViewOrderView(OrderService orderService) {
        this.orderService = orderService;
    }

    @Override
    public void setParameter(BeforeEvent event, String orderId) {
        OrderZak order = orderService.getOrderById(Long.valueOf(orderId));
        if (order != null) {
            viewOrder(order);
        } else {
            Notification.show("v not found.");
        }
    }



    public void viewOrder(OrderZak order) {
        Dialog dialog = new Dialog();
        FormLayout formLayout = new FormLayout();

        TextField nameField = new TextField("Order Name", order.getName(), "");
        nameField.setReadOnly(true);
        formLayout.add(nameField);

        /*TextField addressField = new TextField("Staff Address", staff.getAddress(), "");
        addressField.setReadOnly(true);
        formLayout.add(addressField);*/

        DatePicker birthDateField = new DatePicker("Date", order.getOrderDate());
        birthDateField.setReadOnly(true);
        formLayout.add(birthDateField);

        TextField positionField = new TextField("Status", order.getStatus(), "");
        positionField.setReadOnly(true);
        formLayout.add(positionField);

        dialog.add(formLayout);
        dialog.open();


        Notification.show("Order view.");
        Button backButton = new Button("Back to Orders", click -> {
            dialog.close();
            Notification.show("Order view closed.");
            UI.getCurrent().navigate("orderzak");

        });

        formLayout.add(backButton);
        dialog.add(formLayout);
        dialog.open();


    }
}
@Route("deletePerson")
@RolesAllowed("ROLE_USER")
class DeletePersonView extends VerticalLayout implements HasUrlParameter<String> {
    private final PersonService personService;

    @Autowired
    public DeletePersonView(PersonService personService) {
        this.personService = personService;
    }

    @Override
    public void setParameter(BeforeEvent event, String personId) {
        Person person = personService.getPersonById(Long.valueOf(personId));
        if (person != null) {
            confirmAndDeletePerson(person);
        } else {
            Notification.show("Person not found.");
        }
    }

    public void confirmAndDeletePerson(Person person) {
        Dialog dialog = new Dialog();
        FormLayout formLayout = new FormLayout();

        TextField nameField = new TextField("Person Name", person.getName(), "");
        nameField.setReadOnly(true);
        formLayout.add(nameField);

        Button confirmButton = new Button("Confirm Delete", click -> {
            personService.deletePerson(person.getId());
            dialog.close();
            Notification.show("Person deleted.");
            UI.getCurrent().navigate("orderzak");
        });

        Button cancelButton = new Button("Cancel", click -> {
            dialog.close();
            Notification.show("The person has not been deleted.");
            UI.getCurrent().navigate("orderzak");
        });

        formLayout.add(confirmButton, cancelButton);
        dialog.add(formLayout);
        dialog.open();
    }
}
@Route("deletePayments")
@RolesAllowed("ROLE_USER")
class DeletePaymentsView extends VerticalLayout implements HasUrlParameter<String> {
    private final PaymentsService paymentsService;

    @Autowired
    public DeletePaymentsView(PaymentsService paymentsService) {
        this.paymentsService = paymentsService;
    }

    @Override
    public void setParameter(BeforeEvent event, String paymentsId) {
        Payments payments = paymentsService.getPaymentsById(Long.valueOf(paymentsId));
        if (payments != null) {
            confirmAndDeletePayments(payments);
        } else {
            Notification.show("Payments not found.");
        }
    }

    public void confirmAndDeletePayments(Payments payments) {
        Dialog dialog = new Dialog();
        FormLayout formLayout = new FormLayout();

        TextField nameField = new TextField("Person Name", payments.getName(), "");
        nameField.setReadOnly(true);
        formLayout.add(nameField);

        Button confirmButton = new Button("Confirm Delete", click -> {
            paymentsService.deletePayments(payments.getId());
            dialog.close();
            Notification.show("Payments deleted.");
            UI.getCurrent().navigate("orderzak");
        });

        Button cancelButton = new Button("Cancel", click -> {
            dialog.close();
            Notification.show("The payments has not been deleted.");
            UI.getCurrent().navigate("orderzak");
        });

        formLayout.add(confirmButton, cancelButton);
        dialog.add(formLayout);
        dialog.open();
    }
}