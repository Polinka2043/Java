package com.example.demo.view;

import com.example.demo.entity.Promotions;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.server.StreamResource;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.vaadin.flow.theme.lumo.LumoUtility;

import java.io.InputStream;


@AnonymousAllowed
@Route(value = "")
@CssImport("./styles/views/login/login-view.css")
public class MainLayout extends AppLayout {

    public MainLayout() {
        createHeader();
        createDrawerStaff();
        createDrawerOrder();
        createDrawerProduct();
        createDrawerShipping();
        createDrawerStock();
        createDrawerReviews();
        createDrawerLoginNews();
        VerticalLayout content = new VerticalLayout();
        content.setWidthFull();
        content.setHeightFull();
        content.setJustifyContentMode(FlexComponent.JustifyContentMode.CENTER);
        content.setAlignItems(FlexComponent.Alignment.CENTER);

        Image image = new Image();

        ClassLoader classLoader = getClass().getClassLoader();
        InputStream inputStream = classLoader.getResourceAsStream("image/logo.png");
        StreamResource streamResource = new StreamResource("logo.png", () -> inputStream);
        image.setSrc(streamResource);
        image.getStyle().set("width", "90%");
        image.getStyle().set("height", "100%");
        content.add(image);
        setContent(content);
    }

    private void createHeader() {
        H1 logo = new H1("Products");
        logo.addClassNames(
                LumoUtility.FontSize.LARGE,
                LumoUtility.Margin.MEDIUM);
        var header = new HorizontalLayout(new DrawerToggle(), logo );
        header.setDefaultVerticalComponentAlignment(FlexComponent.Alignment.CENTER);
        header.setWidthFull();
        header.addClassNames(
                LumoUtility.Padding.Vertical.NONE,
                LumoUtility.Padding.Horizontal.MEDIUM);
        addToNavbar(header);

    }

    private void createDrawerStaff() {
        addToDrawer(new VerticalLayout(
                new RouterLink("Staff", StaffView.class)
        ));
    }
    private void createDrawerOrder() {
        addToDrawer(new VerticalLayout(
                new RouterLink("Order", OrderView.class)
        ));
    }
    private void createDrawerStock() {
        addToDrawer(new VerticalLayout(
                new RouterLink("Stock", StockView.class)
        ));
    }
    private void createDrawerShipping() {
        addToDrawer(new VerticalLayout(
                new RouterLink("Shipping", ShippingView.class)
        ));
    }
    private void createDrawerProduct() {
        addToDrawer(new VerticalLayout(
                new RouterLink("Product", ProductView.class)
        ));
    }
    private void createDrawerReviews() {
        addToDrawer(new VerticalLayout(
                new RouterLink("Reviews", ReviewsView.class)
        ));
    }
    private void createDrawerLoginNews() {
        VerticalLayout drawerLayout = new VerticalLayout();
        drawerLayout.addClassName("drawer-login");

        RouterLink newsLink = new RouterLink("News", NewsView.class);
        newsLink.addClassName("my-blue-button");
        drawerLayout.add(newsLink);

        RouterLink promotionsLink = new RouterLink("Promotions", PromotionsView.class);
        promotionsLink.addClassName("my-blue-button");
        drawerLayout.add(promotionsLink);

        RouterLink loginLink = new RouterLink("Login", LoginView.class);
        loginLink.addClassName("my-blue-button");
        drawerLayout.add(loginLink);

        addToDrawer(drawerLayout);
    }
    /*private void createDrawerLogin() {
        RouterLink loginLink = new RouterLink("Login", LoginView.class);
        loginLink.addClassName("my-blue-button");
        VerticalLayout loginLayout = new VerticalLayout(loginLink);
        loginLayout.addClassName("drawer-login");
        addToDrawer(loginLayout);
    }
    private void createDrawerNews() {
        RouterLink newsLink = new RouterLink("News", NewsView.class);
        newsLink.addClassName("my-blue-button");
        VerticalLayout loginLayout = new VerticalLayout(newsLink);
        loginLayout.addClassName("drawer-login");
        addToDrawer(loginLayout);
    }
    private void createDrawerDepartment() {
        addToDrawer(new VerticalLayout(
                new RouterLink("Department", DepartmentView.class)
        ));
    }
    private void createDrawerOrganisation() {
        addToDrawer(new VerticalLayout(
                new RouterLink("Organisation", OrganisationView.class)
        ));
    }
    private void createDrawerProject() {
        addToDrawer(new VerticalLayout(
                new RouterLink("Project", ProjectView.class)
        ));
    }
    private void createDrawerCar() {
        addToDrawer(new VerticalLayout(
                new RouterLink("Car", CarView.class)
        ));
    }*/
}