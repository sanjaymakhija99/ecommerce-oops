package com.ecommerce.notification;

import com.ecommerce.model.Order;
import com.ecommerce.model.User;

/**
 * =====================================================================
 * OOP CONCEPT: POLYMORPHISM — 4 Notification Implementations
 * =====================================================================
 * Each class implements NotificationService differently internally,
 * but the caller sees one interface: sendOrderUpdate().
 *
 * Real-world: Zomato/Blinkit — order confirmation triggers SMS (Twilio),
 * Email (SendGrid), Push (FCM), WhatsApp (Meta API) simultaneously.
 * Adding Telegram tomorrow = new class, zero changes to OrderService.
 * =====================================================================
 */

class SMSNotification implements NotificationService {
    @Override
    public void sendOrderUpdate(User user, Order order, String message) {
        System.out.printf("📱 SMS  → %s : [Order %s] %s%n",
                user.getPhone(), order.getOrderId(), message);
        // Real: Twilio/MSG91 API call with OTP-style message
    }

    @Override
    public String getChannelName() { return "SMS"; }
}

class EmailNotification implements NotificationService {
    @Override
    public void sendOrderUpdate(User user, Order order, String message) {
        System.out.printf("📧 EMAIL → %s%n   Subject: Order %s Update%n   Body   : %s%n",
                user.getEmail(), order.getOrderId(), message);
        // Real: SendGrid / Amazon SES HTML email with order tracking link
    }

    @Override
    public String getChannelName() { return "Email"; }
}

class PushNotification implements NotificationService {
    @Override
    public void sendOrderUpdate(User user, Order order, String message) {
        System.out.printf("🔔 PUSH → App Notification for %s%n   [%s] %s%n",
                user.getName(), order.getOrderId(), message);
        // Real: Firebase FCM — device token lookup + payload dispatch
    }

    @Override
    public String getChannelName() { return "Push Notification"; }
}

class WhatsAppNotification implements NotificationService {
    @Override
    public void sendOrderUpdate(User user, Order order, String message) {
        System.out.printf("💬 WHATSAPP → %s : Order %s - %s%n",
                user.getPhone(), order.getOrderId(), message);
        // Real: Meta Cloud API — template message approval + delivery receipt
    }

    @Override
    public String getChannelName() { return "WhatsApp"; }
}

/**
 * NotificationDispatcher holds all channels and fires them all at once.
 * Caller just calls dispatch() — doesn't loop or know about channels.
 */
public class NotificationDispatcher {

    private final java.util.List<NotificationService> channels;

    public NotificationDispatcher() {
        channels = java.util.List.of(
            new SMSNotification(),
            new EmailNotification(),
            new PushNotification(),
            new WhatsAppNotification()
        );
    }

    public void dispatch(User user, Order order, String message) {
        System.out.println("\n📣 Dispatching notifications via " + channels.size() + " channels:");
        // POLYMORPHISM in action — same method call, different behavior per class
        channels.forEach(channel -> channel.sendOrderUpdate(user, order, message));
    }
}
