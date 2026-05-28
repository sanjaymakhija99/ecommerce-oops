package com.ecommerce.notification;

import com.ecommerce.model.Order;
import com.ecommerce.model.User;

/**
 * =====================================================================
 * OOP CONCEPT: ABSTRACTION + POLYMORPHISM (Interface)
 * =====================================================================
 * NotificationService contract. All channels (SMS, Email, Push, WhatsApp)
 * implement the same send() method. OrderService fires all of them in a
 * loop — zero knowledge of which channel does what.
 *
 * Real-world: Amazon/Meesho order updates — when your order ships, you
 * get SMS + Email + App notification simultaneously. All via one loop.
 * =====================================================================
 */
public interface NotificationService {
    void sendOrderUpdate(User user, Order order, String message);
    String getChannelName();
}
