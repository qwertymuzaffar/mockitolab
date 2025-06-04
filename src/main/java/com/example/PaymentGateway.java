package com.example;

/**
 * PaymentGateway represents an external payment processing service.
 * In real applications, this might call a credit card processor or PayPal API.
 * We'll mock this in our tests to avoid real transactions.
 */
public interface PaymentGateway {
    /**
     * Processes a payment for the given amount
     * @param amount the amount to charge
     * @return true if payment successful, false otherwise
     */
    boolean processPayment(double amount);

    /**
     * Refunds a payment
     * @param transactionId the transaction to refund
     * @param amount the amount to refund
     * @return true if refund successful, false otherwise
     */
    boolean refundPayment(String transactionId, double amount);
}