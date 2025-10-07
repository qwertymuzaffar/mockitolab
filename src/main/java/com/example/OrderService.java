package com.example;

/**
 * OrderService contains the business logic for processing orders.
 * This is the class we want to test. It depends on PaymentGateway and EmailService,
 * which we'll mock in our tests to isolate the business logic.
 */
public class OrderService {
    private final PaymentGateway paymentGateway;
    private final EmailService emailService;

    /**
     * Constructor that takes dependencies (Dependency Injection)
     * @param paymentGateway service for processing payments
     * @param emailService service for sending emails
     */
    public OrderService(PaymentGateway paymentGateway, EmailService emailService) {
        this.paymentGateway = paymentGateway;
        this.emailService = emailService;
    }

    /**
     * Processes an order by charging payment and sending confirmation email
     * @param customerEmail customer's email address
     * @param amount order amount
     * @return status message indicating success or failure
     */
    public String processOrder(String customerEmail, double amount) {
        // Validate input
        if (amount <= 0) {
            return "Invalid amount";
        }

        if (customerEmail == null || customerEmail.trim().isEmpty()) {
            return "Invalid email";
        }

        // Try to process payment
        boolean paymentSuccess = paymentGateway.processPayment(amount);

        if (paymentSuccess) {
            // Payment successful - send confirmation email
            emailService.sendEmail(customerEmail, "Order confirmed! Total: $" + amount);
            return "Order processed successfully";
        } else {
            // Payment failed
            return "Payment failed";
        }
    }

    /**
     * Processes a premium order with special handling
     * @param customerEmail customer's email address
     * @param amount order amount
     * @return status message
     */
    public String processPremiumOrder(String customerEmail, double amount) {
        if (amount < 100) {
            return "Premium orders must be at least $100";
        }

        boolean paymentSuccess = paymentGateway.processPayment(amount);

        if (paymentSuccess) {
            // Send premium confirmation with subject
            emailService.sendEmailWithSubject(customerEmail,
                    "Premium Order Confirmation",
                    "Your premium order of $" + amount + " has been processed!");
            return "Premium order processed successfully";
        } else {
            return "Payment failed";
        }
    }

    /**
     * Cancels an order and processes refund
     * @param orderId the order to cancel
     * @param customerEmail customer's email address
     * @param amount amount to refund
     * @return status message
     */
    public String cancelOrder(String orderId, String customerEmail, double amount) {
        // Validate order ID
        if (orderId == null || orderId.trim().isEmpty()) {
            return "Invalid order ID";
        }

        // Validate email
        if (customerEmail == null || customerEmail.trim().isEmpty()) {
            return "Invalid email";
        }

        // Validate amount
        if (amount <= 0) {
            return "Invalid amount";
        }

        // Try to process refund
        boolean refundSuccess = paymentGateway.refundPayment(orderId, amount);

        if (refundSuccess) {
            // Refund successful - send cancellation email
            emailService.sendEmail(customerEmail, "Order " + orderId + " has been cancelled. Refund of $" + amount + " processed.");
            return "Order cancelled successfully";
        } else {
            // Refund failed
            return "Refund failed";
        }
    }
}
