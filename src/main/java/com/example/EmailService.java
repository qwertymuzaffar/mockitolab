package com.example;

/**
 * EmailService represents an external email service.
 * In real applications, this might use SendGrid, AWS SES, or SMTP.
 * We'll mock this in our tests to avoid sending real emails.
 */
public interface EmailService {
    /**
     * Sends an email to the specified recipient
     * @param recipient the email address to send to
     * @param message the message content
     */
    void sendEmail(String recipient, String message);

    /**
     * Sends an email with a subject line
     * @param recipient the email address to send to
     * @param subject the email subject
     * @param message the message content
     */
    void sendEmailWithSubject(String recipient, String subject, String message);
}