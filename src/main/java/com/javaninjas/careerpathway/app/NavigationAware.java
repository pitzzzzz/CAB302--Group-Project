package com.javaninjas.careerpathway.app;

/**
 * Optional interface controllers can implement to receive NavigationService
 * and lifecycle notifications.
 */
public interface NavigationAware {
    default void setNavigationService(NavigationService nav) { /* optional */ }
    default void onShow() { /* optional */ }
}
