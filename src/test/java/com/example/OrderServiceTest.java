package com.example;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 * Test class for OrderService using Mockito mocks.
 * @ExtendWith(MockitoExtension.class) - Enables Mockito in JUnit 5
 * @Mock - Creates mock objects automatically
 * @InjectMocks - Automatically injects mocks into the test subject
 */
@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    // Mock objects - these are fake implementations that we control
    @Mock
    private PaymentGateway paymentGateway;

    @Mock
    private EmailService emailService;

    // The object we're testing - mocks will be automatically injected
    @InjectMocks
    private OrderService orderService;

    /**
     * Test 1: Basic successful order processing
     * This test verifies the happy path where everything works correctly
     */
    @Test
    void testSuccessfulOrder() {
        // ARRANGE - Set up test data and mock behavior
        String customerEmail = "john@example.com";
        double amount = 50.0;

        // Tell the payment gateway mock to return true when processPayment is called
        when(paymentGateway.processPayment(amount)).thenReturn(true);

        // ACT - Execute the method we're testing
        String result = orderService.processOrder(customerEmail, amount);

        // ASSERT - Verify the results
        assertEquals("Order processed successfully", result);

        // Verify that the email service was called with the correct parameters
        verify(emailService).sendEmail(customerEmail, "Order confirmed! Total: $" + amount);

        // Verify that payment gateway was called exactly once
        verify(paymentGateway, times(1)).processPayment(amount);
    }

    /**
     * Test 2: Failed payment handling
     * This test verifies that our service handles payment failures correctly
     */
    @Test
    void testFailedPayment() {
        // ARRANGE
        String customerEmail = "jane@example.com";
        double amount = 75.0;

        // Tell the payment gateway mock to return false (payment failed)
        when(paymentGateway.processPayment(amount)).thenReturn(false);

        // ACT
        String result = orderService.processOrder(customerEmail, amount);

        // ASSERT
        assertEquals("Payment failed", result);

        // Verify that NO email was sent since payment failed
        verify(emailService, never()).sendEmail(anyString(), anyString());

        // Verify payment was attempted
        verify(paymentGateway).processPayment(amount);
    }

    /**
     * Test 3: Input validation - invalid amount
     * This test verifies that our service validates input correctly
     */
    @Test
    void testInvalidAmount() {
        // ARRANGE
        String customerEmail = "test@example.com";
        double invalidAmount = -10.0;

        // ACT
        String result = orderService.processOrder(customerEmail, invalidAmount);

        // ASSERT
        assertEquals("Invalid amount", result);

        // Verify that neither payment nor email services were called
        verify(paymentGateway, never()).processPayment(anyDouble());
        verify(emailService, never()).sendEmail(anyString(), anyString());
    }

    /**
     * Test 4: Input validation - invalid email
     */
    @Test
    void testInvalidEmail() {
        // ARRANGE
        String invalidEmail = "";
        double amount = 50.0;

        // ACT
        String result = orderService.processOrder(invalidEmail, amount);

        // ASSERT
        assertEquals("Invalid email", result);

        // Verify no external services were called
        verify(paymentGateway, never()).processPayment(anyDouble());
        verify(emailService, never()).sendEmail(anyString(), anyString());
    }
}
