package com.example.demo.view;

import com.example.demo.entity.Shop;
import com.example.demo.entity.Staff;

import com.example.demo.service.ShopService;
import com.example.demo.service.StaffService;
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
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Route(value = "staffs", layout = MainLayout.class)
@RolesAllowed("USER")
public class StaffView extends VerticalLayout {
    private final StaffService staffService;
    private final ShopService shopService;
    private final TextField nameField = new TextField();
    private final Button searchButton = new Button("Search");

    private final Grid<Staff> grid = new Grid<>(Staff.class);
    private Grid<Shop> shopGrid = new Grid<>(Shop.class);

    @Autowired
    public StaffView(StaffService staffService, ShopService shopService) {
        this.staffService = staffService;
        this.shopService = shopService;
        HorizontalLayout buttonsLayout = new HorizontalLayout();
        buttonsLayout.add(nameField);
        buttonsLayout.add(searchButton);
        buttonsLayout.add(new Button("Add Staff", click -> navigateToAddStaff()));
        buttonsLayout.add(new Button("Delete Staff", click -> {
            Staff selectedStaff = grid.asSingleSelect().getValue();
            if (selectedStaff != null) {
                navigateToDeleteStaff(selectedStaff);
            } else {
                Notification.show("Please select a staff first.");
            }
        }));
        buttonsLayout.add(new Button("Update Staff", click -> {
            Staff selectedStaff = grid.asSingleSelect().getValue();
            if (selectedStaff != null) {
                navigateToUpdateStaff(selectedStaff);
            } else {
                Notification.show("Please select a staff first.");
            }
        }));
        buttonsLayout.add(new Button("View Staff", click -> {
            Staff selectedStaff = grid.asSingleSelect().getValue();
            if (selectedStaff != null) {
                navigateToViewStaff(selectedStaff);
            } else {
                Notification.show("Please select a staff first.");
            }
        }));
        buttonsLayout.add(new Button("Add Shop", click -> navigateToAddShop()));
        buttonsLayout.add(new Button("Delete Shop", click -> {
            Shop selectedShop = shopGrid.asSingleSelect().getValue();
            if (selectedShop!= null) {
                navigateToDeleteShop(selectedShop);
            } else {
                Notification.show("Please select a shop first.");
            }
        }));
        add(buttonsLayout);
        searchButton.addClickListener(e -> updateList());
        updateList();
        add(grid);
        listStaffs();
        add(shopGrid);
        listShops();
    }

    private void listShops() {
        List<Shop> shopList = shopService.getShops();
        shopGrid.setItems(shopList);
    }
    private void updateList() {
        List<Staff> staffList;
        if (nameField.getValue() == null || nameField.getValue().isEmpty()) {
            staffList = staffService.findAll();
        } else {
            staffList = staffService.findByName(nameField.getValue());
        }
        grid.setItems(staffList);
    }

    private void listStaffs() {
        grid.setItems(staffService.getStaffs());
    }

    private void navigateToAddStaff() {
        UI.getCurrent().navigate(AddStaffView.class);
    }
    private void navigateToAddShop() {
        UI.getCurrent().navigate(AddShopView.class);
    }
    private void navigateToDeleteStaff(Staff staff) {
        UI.getCurrent().navigate("deleteStaff/" + staff.getId());
    }

    private void navigateToUpdateStaff(Staff staff) {
        UI.getCurrent().navigate("updateStaff/" + staff.getId());
    }

    private void navigateToViewStaff(Staff staff) {
        UI.getCurrent().navigate("viewStaff/" + staff.getId());
    }
    private void navigateToDeleteShop(Shop shop) {
        UI.getCurrent().navigate("deleteShop/" + shop.getId());
    }

}


@Route("addStaff")
@RolesAllowed("USER")
class AddStaffView extends VerticalLayout {
    @Autowired
    public AddStaffView(StaffService staffService) {
        Dialog dialog = new Dialog();
        FormLayout formLayout = new FormLayout();
        TextField nameField = new TextField();
        nameField.setLabel("Staff Name");
        formLayout.add(nameField);
        DatePicker birthDateField = new DatePicker();
        birthDateField.setLabel("Birth Date");
        formLayout.add(birthDateField);
        TextField positionField = new TextField();
        positionField.setLabel("Position");
        formLayout.add(positionField);
        Button saveButton = new Button("Save", click -> {
            if (nameField.isEmpty() ||  birthDateField.isEmpty() || positionField.isEmpty()) {
                Notification.show("Please fill in all fields.");
            } else {
                Staff staff = new Staff();
                staff.setName(nameField.getValue());
                staff.setBirthDate(birthDateField.getValue());
                staff.setPosition(positionField.getValue());
                staffService.saveStaff(staff);
                dialog.close();
                UI.getCurrent().navigate("staffs");
            }
        });
        formLayout.add(saveButton);
        dialog.add(formLayout);
        dialog.open();
    }
}

@Route("addShop")
@RolesAllowed("USER")
class AddShopView extends VerticalLayout {
    private final StaffService staffService;
    @Autowired
    public AddShopView(ShopService shopService, StaffService staffService) {
        this.staffService = staffService;
        Dialog dialog = new Dialog();
        FormLayout formLayout = new FormLayout();
        TextField nameField = new TextField();
        nameField.setLabel("Name");
        formLayout.add(nameField);
        TextField addressField = new TextField();
        addressField.setLabel("Address");
        formLayout.add(addressField);
        ListBox<Staff> staffIdField = getStaffListBox();
        formLayout.add(staffIdField);
        Button saveButton = new Button("Save", click -> {
            if (nameField.isEmpty()|| addressField.isEmpty()) {
                Notification.show("Please fill in all fields.");
            } else {
                Shop shop = new Shop();
                shop.setName(nameField.getValue());
                shop.setAddress(addressField.getValue());
                shop.setHeadOfShop(staffIdField.getValue());
                shopService.saveShop(shop);
                dialog.close();
                UI.getCurrent().navigate("staffs");
            }
        });
        formLayout.add(saveButton);
        dialog.add(formLayout);
        dialog.open();
    }
    private ListBox<Staff> getStaffListBox() {
        ListBox<Staff> headOfShopField = new ListBox<>();
        List<Staff> staffList = staffService.getStaffs();
        if (!staffList.isEmpty()) {
            headOfShopField.setItems(staffList);
            headOfShopField.setValue(staffList.get(0));
        } else {
            headOfShopField.setEnabled(false);
        }
        return headOfShopField;
    }
}
@Route("deleteStaff")
@RolesAllowed("ROLE_USER")
class DeleteStaffView extends VerticalLayout implements HasUrlParameter<String> {
    private final StaffService staffService;

    @Autowired
    public DeleteStaffView(StaffService staffService) {
        this.staffService = staffService;
    }

    @Override
    public void setParameter(BeforeEvent event, String staffId) {
        Staff staff = staffService.getStaffById(Long.valueOf(staffId));
        if (staff != null) {
            confirmAndDeleteStaff(staff);
        } else {
            Notification.show("Staff not found.");
        }
    }

    public void confirmAndDeleteStaff(Staff staff) {
        Dialog dialog = new Dialog();
        FormLayout formLayout = new FormLayout();

        TextField nameField = new TextField("Staff Name", staff.getName(), "");
        nameField.setReadOnly(true);
        formLayout.add(nameField);

        Button confirmButton = new Button("Confirm Delete", click -> {
            staffService.deleteStaff(staff.getId());
            dialog.close();
            Notification.show("Staff deleted.");
            UI.getCurrent().navigate("staffs");
        });

        Button cancelButton = new Button("Cancel", click -> {
            dialog.close();
            Notification.show("The staff has not been deleted.");
            UI.getCurrent().navigate("staffs");
        });

        formLayout.add(confirmButton, cancelButton);
        dialog.add(formLayout);
        dialog.open();
    }
}

@Route("updateStaff")
@RolesAllowed("USER")
class UpdateStaffView extends VerticalLayout implements HasUrlParameter<String> {
    private final StaffService staffService;

    @Autowired
    public UpdateStaffView(StaffService staffService) {
        this.staffService = staffService;
    }

    @Override
    public void setParameter(BeforeEvent event, String staffId) {
        Staff staff = staffService.getStaffById(Long.valueOf(staffId));
        if (staff != null) {
            updateStaff(staff);
        } else {
            Notification.show("Staff not found.");
        }
    }

    public void updateStaff(Staff staff) {
        Dialog dialog = new Dialog();
        FormLayout formLayout = new FormLayout();
        TextField nameField = new TextField("Staff Name", staff.getName());
        formLayout.add(nameField);
        DatePicker birthDateField = new DatePicker("Birth Date", staff.getBirthDate());
        formLayout.add(birthDateField);
        TextField positionField = new TextField("Position", staff.getPosition());
        formLayout.add(positionField);
        Button saveButton = new Button("Save", click -> {
            if (nameField.isEmpty() ||  birthDateField.isEmpty() || positionField.isEmpty()) {
                Notification.show("Please fill in all fields.");
            } else {
                staff.setName(nameField.getValue());
                staff.setBirthDate(birthDateField.getValue());
                staff.setPosition(positionField.getValue());
                staffService.saveStaff(staff);
                dialog.close();
                Notification.show("Staff updated.");
                UI.getCurrent().navigate("staffs");
            }
        });
        formLayout.add(saveButton);
        dialog.add(formLayout);
        dialog.open();
    }
}

@Route("viewStaff")
@RolesAllowed("USER")
class ViewStaffView extends VerticalLayout implements HasUrlParameter<String> {
    private final StaffService staffService;

    @Autowired
    public ViewStaffView(StaffService staffService) {
        this.staffService = staffService;
    }

    @Override
    public void setParameter(BeforeEvent event, String staffId) {
        Staff staff = staffService.getStaffById(Long.valueOf(staffId));
        if (staff != null) {
            viewStaff(staff);
        } else {
            Notification.show("Staff not found.");
        }
    }



    public void viewStaff(Staff staff) {
        Dialog dialog = new Dialog();
        FormLayout formLayout = new FormLayout();

        TextField nameField = new TextField("Staff Name", staff.getName(), "");
        nameField.setReadOnly(true);
        formLayout.add(nameField);

        /*TextField addressField = new TextField("Staff Address", staff.getAddress(), "");
        addressField.setReadOnly(true);
        formLayout.add(addressField);*/

        DatePicker birthDateField = new DatePicker("Birth Date", staff.getBirthDate());
        birthDateField.setReadOnly(true);
        formLayout.add(birthDateField);

        TextField positionField = new TextField("Position", staff.getPosition(), "");
        positionField.setReadOnly(true);
        formLayout.add(positionField);

        dialog.add(formLayout);
        dialog.open();


        Notification.show("Staff view.");
        Button backButton = new Button("Back to Staffs", click -> {
            dialog.close();
            Notification.show("Staff view closed.");
            UI.getCurrent().navigate("staffs");

        });

        formLayout.add(backButton);
        dialog.add(formLayout);
        dialog.open();


    }
}
@Route("deleteShop")
@RolesAllowed("USER")
class DeleteShopView extends VerticalLayout implements HasUrlParameter<String> {
    private final ShopService shopService;

    @Autowired
    public DeleteShopView(ShopService shopService) {
        this.shopService = shopService;
    }

    @Override
    public void setParameter(BeforeEvent event, String shopId) {
        Shop shop = shopService.getShopById(Long.valueOf(shopId));
        if (shop != null) {
            confirmAndDeleteShop(shop);
        } else {
            Notification.show("Shop not found.");
        }
    }

    public void confirmAndDeleteShop(Shop shop) {
        Dialog dialog = new Dialog();
        FormLayout formLayout = new FormLayout();

        TextField nameField = new TextField("Shop Name", shop.getName(), "");
        nameField.setReadOnly(true);
        formLayout.add(nameField);

        Button confirmButton = new Button("Confirm Delete", click -> {
            shopService.deleteShop(shop.getId());
            dialog.close();
            Notification.show("Shop deleted.");
            UI.getCurrent().navigate("staffs");
        });

        Button cancelButton = new Button("Cancel", click -> {
            dialog.close();
            Notification.show("The shop has not been deleted.");
            UI.getCurrent().navigate("staffs");
        });

        formLayout.add(confirmButton, cancelButton);
        dialog.add(formLayout);
        dialog.open();
    }
}

