package com.pm.track.common;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.*;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    /**
     * ✅ Plain text email for password reset
     */
    @Async
    public void sendPasswordResetEmail(String toEmail, String token) {
        String resetUrl = "http://your-frontend-url.com/reset-password?token=" + token;
        String subject = "Password Reset Request";
        String body = "Hi,\n\n"
                + "You requested to reset your password. Please click the link below:\n"
                + resetUrl + "\n\n"
                + "This link will expire in 15 minutes.\n\n"
                + "If you did not request this, please ignore this email.\n\n"
                + "Thanks,\nTrack Team";

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject(subject);
        message.setText(body);

        mailSender.send(message);
    }

    /**
     * ✅ HTML email only for new users (e.g. after registration)
     */
    @Async
    public void sendWelcomeEmail(String toEmail, String name) {
        String subject = "🎉 Welcome to Track, " + name + "!";
        String content = buildWelcomeHtml(name);
        sendHtmlEmail(toEmail, subject, content);
    }

    /**
     * 🔁 Future: For old returning users at login (event, discounts, etc.)
     */
    @Async
    public void sendReturningUserOfferEmail(String toEmail, String name) {
        String subject = "👋 Welcome back, " + name + "! Here’s what’s new...";
        String content = buildReturningUserOfferHtml(name);
        sendHtmlEmail(toEmail, subject, content);
    }

    /**
     * 🧱 Reusable method to send HTML mails
     */
    private void sendHtmlEmail(String to, String subject, String htmlContent) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlContent, true); // HTML = true

            mailSender.send(message);
        } catch (MessagingException e) {
            throw new RuntimeException("Failed to send HTML email", e);
        }
    }

    /**
     * 🖌️ Email Template: New User Welcome
     */
    private String buildWelcomeHtml(String name) {
            return "<!DOCTYPE html>" +
                    "<html>" +
                    "<head>" +
                    "<meta name='viewport' content='width=device-width, initial-scale=1.0'>" +
                    "<link href='https://fonts.googleapis.com/css2?family=Orbitron:wght@400;700&family=Roboto:wght@300;400;500&display=swap' rel='stylesheet'>" +
                    "</head>" +
                    "<body style='font-family: Roboto, sans-serif; background-color: #0a0a1a; margin: 0; padding: 0;'>" +

                    // Animated gradient border container
                    "<div style='max-width: 600px; margin: 20px auto; background: #111126; border-radius: 12px; padding: 3px; background: linear-gradient(135deg, #00c6ff, #0072ff, #6a11cb); animation: gradientBorder 8s ease infinite; background-size: 400% 400%;'>" +

                    // Main content area
                    "<div style='background: #111126; border-radius: 10px; padding: 30px;'>" +

                    // Header with animated logo
                    "<div style='text-align: center; margin-bottom: 25px;'>" +
                    "<h1 style='font-family: Orbitron, sans-serif; color: #00f3ff; font-size: 28px; margin: 0; letter-spacing: 1px;'>TRACK<span style='color: #ff2d75;'>AI</span></h1>" +
                    "<div style='height: 2px; background: linear-gradient(90deg, transparent, #00f3ff, transparent); margin: 10px 0;'></div>" +
                    "</div>" +

                    // Personalized greeting
                    "<h2 style='font-family: Orbitron, sans-serif; color: #ffffff; font-size: 22px; margin-bottom: 20px;'>HELLO <span style='color: #00f3ff; text-transform: uppercase;'>" + name + "</span>,</h2>" +

                    // Main message with tech icon
                    "<div style='display: flex; align-items: center; margin-bottom: 25px;'>" +
                    "<div style='flex: 0 0 60px; text-align: center;'>" +
                    "<div style='width: 50px; height: 50px; background: rgba(0, 243, 255, 0.1); border-radius: 50%; display: flex; align-items: center; justify-content: center; border: 1px solid #00f3ff;'>" +
                    "<span style='font-size: 24px; color: #00f3ff;'>👋</span>" +
                    "</div>" +
                    "</div>" +
                    "<p style='flex: 1; color: #d1d1ff; font-size: 16px; line-height: 1.6; margin: 0;'>Welcome to the future of shopping! Your AI-powered commerce experience begins now. Explore personalized recommendations, AR previews, and instant checkout.</p>" +
                    "</div>" +

                    // Interactive CTA button
                    "<div style='text-align: center; margin: 30px 0;'>" +
                    "<a href='#' style='display: inline-block; padding: 12px 30px; background: linear-gradient(90deg, #6a11cb 0%, #2575fc 100%); color: white; text-decoration: none; border-radius: 30px; font-weight: 500; font-size: 16px; transition: all 0.3s; box-shadow: 0 4px 15px rgba(106, 17, 203, 0.4);'>" +
                    "DASHBOARD PORTAL" +
                    "</a>" +
                    "</div>" +

                    // Features grid
                    "<div style='display: grid; grid-template-columns: repeat(2, 1fr); gap: 15px; margin-bottom: 25px;'>" +
                    "<div style='background: rgba(0, 243, 255, 0.05); border: 1px solid rgba(0, 243, 255, 0.2); border-radius: 8px; padding: 15px; text-align: center;'>" +
                    "<div style='font-size: 24px; color: #00f3ff; margin-bottom: 8px;'>🤖</div>" +
                    "<h3 style='color: white; font-size: 14px; margin: 0;'>AI RECOMMENDATIONS</h3>" +
                    "</div>" +
                    "<div style='background: rgba(0, 243, 255, 0.05); border: 1px solid rgba(0, 243, 255, 0.2); border-radius: 8px; padding: 15px; text-align: center;'>" +
                    "<div style='font-size: 24px; color: #00f3ff; margin-bottom: 8px;'>🕶️</div>" +
                    "<h3 style='color: white; font-size: 14px; margin: 0;'>AR PREVIEWS</h3>" +
                    "</div>" +
                    "<div style='background: rgba(0, 243, 255, 0.05); border: 1px solid rgba(0, 243, 255, 0.2); border-radius: 8px; padding: 15px; text-align: center;'>" +
                    "<div style='font-size: 24px; color: #00f3ff; margin-bottom: 8px;'>⚡</div>" +
                    "<h3 style='color: white; font-size: 14px; margin: 0;'>INSTANT CHECKOUT</h3>" +
                    "</div>" +
                    "<div style='background: rgba(0, 243, 255, 0.05); border: 1px solid rgba(0, 243, 255, 0.2); border-radius: 8px; padding: 15px; text-align: center;'>" +
                    "<div style='font-size: 24px; color: #00f3ff; margin-bottom: 8px;'>🔒</div>" +
                    "<h3 style='color: white; font-size: 14px; margin: 0;'>CRYPTO PAYMENTS</h3>" +
                    "</div>" +
                    "</div>" +

                    // Footer with social links
                    "<div style='border-top: 1px solid rgba(255, 255, 255, 0.1); padding-top: 20px; text-align: center;'>" +
                    "<p style='color: #8a8ab5; font-size: 12px; margin-bottom: 15px;'>CONNECT WITH OUR DIGITAL ECOSYSTEM</p>" +
                    "<div style='display: flex; justify-content: center; gap: 15px; margin-bottom: 15px;'>" +
                    "<a href='#' style='color: #00f3ff; text-decoration: none; font-size: 20px;'>𝕏</a>" +
                    "<a href='#' style='color: #00f3ff; text-decoration: none; font-size: 20px;'>⎈</a>" +
                    "<a href='#' style='color: #00f3ff; text-decoration: none; font-size: 20px;'>◈</a>" +
                    "<a href='#' style='color: #00f3ff; text-decoration: none; font-size: 20px;'>◇</a>" +
                    "</div>" +
                    "<p style='color: #5a5a8a; font-size: 11px; line-height: 1.5;'>© 2023 TRACKAI. All systems operational. This is an automated message, please do not reply directly to this email.</p>" +
                    "</div>" +

                    "</div>" + // End main content
                    "</div>" + // End gradient border

                    // CSS animations
                    "<style>" +
                    "@keyframes gradientBorder {" +
                    "0% { background-position: 0% 50%; }" +
                    "50% { background-position: 100% 50%; }" +
                    "100% { background-position: 0% 50%; }" +
                    "}" +
                    "</style>" +

                    "</body>" +
                    "</html>";

    }

    /**
     * 🎯 Future Template: Discount or Event for returning user
     */
    private String buildReturningUserOfferHtml(String name) {
        return "<html>" +
                "<body style='font-family: Arial, sans-serif; background-color: #f0f8ff; padding: 20px;'>" +
                "<div style='max-width: 600px; margin: auto; background: #fff; border-radius: 10px; padding: 25px; box-shadow: 0 0 8px rgba(0,0,0,0.1);'>" +
                "<h2 style='color: #222;'>Welcome back, <span style='color: #28a745;'>" + name + "</span> 👋</h2>" +
                "<p style='font-size: 15px; color: #444;'>We've got some exciting news and offers for you! 🎁</p>" +
                "<ul style='color: #555;'>" +
                "<li>🔥 Exclusive discount on your next order</li>" +
                "<li>🗓️ Invite-only access to our upcoming event</li>" +
                "</ul>" +
                "<p style='font-size: 13px; color: #888;'>Stay tuned. We’re just getting started.</p>" +
                "</div>" +
                "</body>" +
                "</html>";
    }
}
