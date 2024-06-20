package com.example.demo;

import com.vaadin.flow.server.VaadinSession;

public class SecurityUtils {
    public static boolean isUserInRole(String role) {
        Object attribute = VaadinSession.getCurrent().getAttribute("userRole");
        //System.out.println("Attribute: " + attribute);
        if (attribute == null) {
            return false;
        }
        return attribute.equals(role);
    }
}