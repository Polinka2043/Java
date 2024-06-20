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

@Route(value = "product", layout = MainLayout.class)
@RolesAllowed("ROLE_USER")
public class ProductView extends VerticalLayout {

    private final ProductService productService;
    private final ProductBrandService productBrandService;
    private final ProductCategoryService productCategoryService;
    private final TextField nameField = new TextField();
    private final Button searchButton = new Button("Search");

    private final Grid<Product> grid = new Grid<>(Product.class);
    private Grid<ProductBrand> productBrandGrid = new Grid<>(ProductBrand.class);
    private Grid<ProductCategory> productCategoryGrid = new Grid<>(ProductCategory.class);
    @Autowired
    public ProductView(ProductService productService, ProductBrandService productBrandService, ProductCategoryService productCategoryService) {
        this.productService = productService;
        this.productBrandService = productBrandService;
        this.productCategoryService = productCategoryService;
        HorizontalLayout buttonsLayout = new HorizontalLayout();
        buttonsLayout.add(nameField);
        buttonsLayout.add(searchButton);
        buttonsLayout.add(new Button("Add Product", click -> navigateToAddProduct()));
        buttonsLayout.add(new Button("Delete Product", click -> {
            Product selectedProduct = grid.asSingleSelect().getValue();
            if (selectedProduct != null) {
                navigateToDeleteProduct(selectedProduct);
            } else {
                Notification.show("Please select a Product first.");
            }
        }));
        buttonsLayout.add(new Button("View Product", click -> {
            Product selectedProduct = grid.asSingleSelect().getValue();
            if (selectedProduct != null) {
                navigateToViewProduct(selectedProduct);
            } else {
                Notification.show("Please select a Product first.");
            }
        }));
        buttonsLayout.add(new Button("Add ProductBrand", click -> navigateToAddProductBrand()));
        buttonsLayout.add(new Button("Delete ProductBrand", click -> {
            ProductBrand selectedProductBrand = productBrandGrid.asSingleSelect().getValue();
            if (selectedProductBrand!= null) {
                navigateToDeleteProductBrand(selectedProductBrand);
            } else {
                Notification.show("Please select a Product Brand first.");
            }
        }));
        buttonsLayout.add(new Button("Add ProductCategory", click -> navigateToAddProductCategory()));
        buttonsLayout.add(new Button("Delete ProductCategory", click -> {
            ProductCategory selectedProductCategory = productCategoryGrid.asSingleSelect().getValue();
            if (selectedProductCategory!= null) {
                navigateToDeleteProductCategory(selectedProductCategory);
            } else {
                Notification.show("Please select a Product Category first.");
            }
        }));
        add(buttonsLayout);
        searchButton.addClickListener(e -> updateList());
        updateList();
        add(grid);
        listProducts();
        add(productBrandGrid);
        listProductBrand();
        add(productCategoryGrid);
        listProductCategory();
    }

    private void listProductBrand() {
        List<ProductBrand> productBrandList = productBrandService.getProductBrands();
        productBrandGrid.setItems(productBrandList);
    }
    private void listProductCategory() {
        List<ProductCategory> productCategoryList = productCategoryService.getProductCategorys();
        productCategoryGrid.setItems(productCategoryList);
    }
    private void updateList() {
        List<Product> productList;
        if (nameField.getValue() == null || nameField.getValue().isEmpty()) {
            productList = productService.findAll();
        } else {
            productList = productService.findByName(nameField.getValue());
        }
        grid.setItems(productList);
    }

    private void listProducts() {
        grid.setItems(productService.getProducts());
    }

    private void navigateToAddProduct() {
        UI.getCurrent().navigate(AddProductView.class);
    }
    private void navigateToAddProductBrand() {
        UI.getCurrent().navigate(AddProductBrandView.class);
    }
    private void navigateToAddProductCategory() {
        UI.getCurrent().navigate(AddProductCategoryView.class);
    }
    private void navigateToDeleteProduct(Product product) {
        UI.getCurrent().navigate("deleteProduct/" + product.getId());
    }

    private void navigateToViewProduct(Product product) {
        UI.getCurrent().navigate("viewProduct/" + product.getId());
    }
    private void navigateToDeleteProductBrand(ProductBrand productBrand) {
        UI.getCurrent().navigate("deleteProductBrand/" + productBrand.getId());
    }
    private void navigateToDeleteProductCategory(ProductCategory productCategory) {
        UI.getCurrent().navigate("deleteProductCategory/" + productCategory.getId());
    }

}


@Route("addProduct")
@RolesAllowed("ROLE_USER")
class AddProductView extends VerticalLayout {
    @Autowired
    public AddProductView(ProductService productService) {
        Dialog dialog = new Dialog();
        FormLayout formLayout = new FormLayout();
        TextField nameField = new TextField();
        nameField.setLabel("Name");
        formLayout.add(nameField);
        TextField statusField = new TextField();
        statusField.setLabel("Category");
        formLayout.add(statusField);
        IntegerField countField = new IntegerField();
        countField.setLabel("Price");
        formLayout.add(countField);
        Button saveButton = new Button("Save", click -> {
            if (nameField.isEmpty() ||  countField.isEmpty() ||statusField.isEmpty() ) {
                Notification.show("Please fill in all fields.");
            } else {
                Product product = new Product();
                product.setName(nameField.getValue());
                product.setPrice((long) countField.getValue());
                product.setCategory(statusField.getValue());
                productService.saveProduct(product);
                dialog.close();
                UI.getCurrent().navigate("product");
            }
        });
        formLayout.add(saveButton);
        dialog.add(formLayout);
        dialog.open();
    }
}

@Route("addProductBrand")
@RolesAllowed("ROLE_USER")
class AddProductBrandView extends VerticalLayout {
    private final ProductService productService;
    @Autowired
    public AddProductBrandView(ProductBrandService productBrandService, ProductService productService) {
        this.productService = productService;
        Dialog dialog = new Dialog();
        FormLayout formLayout = new FormLayout();
        TextField nameField = new TextField();
        nameField.setLabel("Name");
        formLayout.add(nameField);
        TextField commentField = new TextField();
        commentField.setLabel("Comment");
        formLayout.add(commentField);
        ListBox<Product> productIdField = getProductListBox();
        formLayout.add(productIdField);
        Button saveButton = new Button("Save", click -> {
            if (nameField.isEmpty()) {
                Notification.show("Please fill in all fields.");
            } else {
                ProductBrand productBrand = new ProductBrand();
                productBrand.setName(nameField.getValue());
                productBrand.setComment(commentField.getValue());
                productBrand.setProduct(productIdField.getValue());
                productBrandService.saveProductBrand(productBrand);
                dialog.close();
                UI.getCurrent().navigate("product");
            }
        });
        formLayout.add(saveButton);
        dialog.add(formLayout);
        dialog.open();
    }
    private ListBox<Product> getProductListBox() {
        ListBox<Product> productIdField = new ListBox<>();
        List<Product> productList = productService.getProducts();
        if (!productList.isEmpty()) {
            productIdField.setItems(productList);
            productIdField.setValue(productList.get(0));
        } else {
            productIdField.setEnabled(false);
        }
        return productIdField;
    }
}

@Route("addProductCategory")
@RolesAllowed("ROLE_USER")
class AddProductCategoryView extends VerticalLayout {
    private final ProductService productService;
    @Autowired
    public AddProductCategoryView(ProductCategoryService productCategoryService, ProductService productService) {
        this.productService = productService;
        Dialog dialog = new Dialog();
        FormLayout formLayout = new FormLayout();
        TextField nameField = new TextField();
        nameField.setLabel("Name");
        formLayout.add(nameField);
        TextField commentField = new TextField();
        commentField.setLabel("Comment");
        formLayout.add(commentField);
        ListBox<Product> productIdField = getProductListBox();
        formLayout.add(productIdField);
        Button saveButton = new Button("Save", click -> {
            if (nameField.isEmpty()) {
                Notification.show("Please fill in all fields.");
            } else {
                ProductCategory productCategory = new ProductCategory();
                productCategory.setName(nameField.getValue());
                productCategory.setComment(commentField.getValue());
                productCategory.setProduct(productIdField.getValue());
                productCategoryService.saveProductCategory(productCategory);
                dialog.close();
                UI.getCurrent().navigate("product");
            }
        });
        formLayout.add(saveButton);
        dialog.add(formLayout);
        dialog.open();
    }
    private ListBox<Product> getProductListBox() {
        ListBox<Product> productIdField = new ListBox<>();
        List<Product> productList = productService.getProducts();
        if (!productList.isEmpty()) {
            productIdField.setItems(productList);
            productIdField.setValue(productList.get(0));
        } else {
            productIdField.setEnabled(false);
        }
        return productIdField;
    }
}

@Route("deleteProduct")
@RolesAllowed("ROLE_USER")
class DeleteProductView extends VerticalLayout implements HasUrlParameter<String> {
    private final ProductService productService;

    @Autowired
    public DeleteProductView(ProductService productService) {
        this.productService = productService;
    }

    @Override
    public void setParameter(BeforeEvent event, String productId) {
        Product product = productService.getProductById(Long.valueOf(productId));
        if (product != null) {
            confirmAndDeleteProduct(product);
        } else {
            Notification.show("product not found.");
        }
    }

    public void confirmAndDeleteProduct(Product product) {
        Dialog dialog = new Dialog();
        FormLayout formLayout = new FormLayout();

        TextField nameField = new TextField("Product Name", product.getName(), "");
        nameField.setReadOnly(true);
        formLayout.add(nameField);

        Button confirmButton = new Button("Confirm Delete", click -> {
            productService.deleteProduct(product.getId());
            dialog.close();
            Notification.show("product deleted.");
            UI.getCurrent().navigate("product");
        });

        Button cancelButton = new Button("Cancel", click -> {
            dialog.close();
            Notification.show("The product has not been deleted.");
            UI.getCurrent().navigate("product");
        });

        formLayout.add(confirmButton, cancelButton);
        dialog.add(formLayout);
        dialog.open();
    }
}

@Route("viewProduct")
@RolesAllowed("ROLE_USER")
class ViewProductView extends VerticalLayout implements HasUrlParameter<String> {
    private final ProductService productService;

    @Autowired
    public ViewProductView(ProductService productService) {
        this.productService = productService;
    }

    @Override
    public void setParameter(BeforeEvent event, String productId) {
        Product product =productService.getProductById(Long.valueOf(productId));
        if (product != null) {
            viewProduct(product);
        } else {
            Notification.show("Product not found.");
        }
    }



    public void viewProduct(Product product) {
        Dialog dialog = new Dialog();
        FormLayout formLayout = new FormLayout();

        TextField nameField = new TextField("Product Name", product.getName(), "");
        nameField.setReadOnly(true);
        formLayout.add(nameField);

        IntegerField countField = new IntegerField("Product Count", String.valueOf(product.getPrice().intValue()));
        countField.setReadOnly(true);
        formLayout.add(countField);

        TextField positionField = new TextField("Category", product.getCategory(), "");
        positionField.setReadOnly(true);
        formLayout.add(positionField);

        dialog.add(formLayout);
        dialog.open();


        Notification.show("product view.");
        Button backButton = new Button("Back to products", click -> {
            dialog.close();
            Notification.show("product view closed.");
            UI.getCurrent().navigate("product");

        });

        formLayout.add(backButton);
        dialog.add(formLayout);
        dialog.open();


    }
}
@Route("deleteProductBrand")
@RolesAllowed("ROLE_USER")
class DeleteProductBrandView extends VerticalLayout implements HasUrlParameter<String> {
    private final ProductBrandService productBrandService;

    @Autowired
    public DeleteProductBrandView(ProductBrandService productBrandService) {
        this.productBrandService = productBrandService;
    }

    @Override
    public void setParameter(BeforeEvent event, String productBrandId) {
        ProductBrand productBrand = productBrandService.getProductBrandById(Long.valueOf(productBrandId));
        if (productBrand != null) {
            confirmAndDeleteProductBrand(productBrand);
        } else {
            Notification.show("Product Brand not found.");
        }
    }

    public void confirmAndDeleteProductBrand(ProductBrand productBrand) {
        Dialog dialog = new Dialog();
        FormLayout formLayout = new FormLayout();

        TextField nameField = new TextField("Product Brand Name", productBrand.getName(), "");
        nameField.setReadOnly(true);
        formLayout.add(nameField);

        Button confirmButton = new Button("Confirm Delete", click -> {
            productBrandService.deleteProductBrand(productBrand.getId());
            dialog.close();
            Notification.show("Product Brand deleted.");
            UI.getCurrent().navigate("product");
        });

        Button cancelButton = new Button("Cancel", click -> {
            dialog.close();
            Notification.show("The product has not been deleted.");
            UI.getCurrent().navigate("product");
        });

        formLayout.add(confirmButton, cancelButton);
        dialog.add(formLayout);
        dialog.open();
    }
}
@Route("deleteProductCategory")
@RolesAllowed("ROLE_USER")
class DeleteProductCategoryView extends VerticalLayout implements HasUrlParameter<String> {
    private final ProductCategoryService productCategoryService;

    @Autowired
    public DeleteProductCategoryView(ProductCategoryService productCategoryService) {
        this.productCategoryService = productCategoryService;
    }

    @Override
    public void setParameter(BeforeEvent event, String productCategoryId) {
        ProductCategory productCategory = productCategoryService.getProductCategoryById(Long.valueOf(productCategoryId));
        if (productCategory != null) {
            confirmAndDeleteProductCategory(productCategory);
        } else {
            Notification.show("ProductCategory not found.");
        }
    }

    public void confirmAndDeleteProductCategory(ProductCategory productCategory) {
        Dialog dialog = new Dialog();
        FormLayout formLayout = new FormLayout();

        TextField nameField = new TextField("Product Category Name", productCategory.getName(), "");
        nameField.setReadOnly(true);
        formLayout.add(nameField);

        Button confirmButton = new Button("Confirm Delete", click -> {
            productCategoryService.deleteProductCategory(productCategory.getId());
            dialog.close();
            Notification.show("product deleted.");
            UI.getCurrent().navigate("product");
        });

        Button cancelButton = new Button("Cancel", click -> {
            dialog.close();
            Notification.show("The product has not been deleted.");
            UI.getCurrent().navigate("product");
        });

        formLayout.add(confirmButton, cancelButton);
        dialog.add(formLayout);
        dialog.open();
    }
}
