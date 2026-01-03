package com.smartsla.smart_sla_tracker.service;



import com.smartsla.smart_sla_tracker.entity.Ticket;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailNotificationService {

    private final JavaMailSender mailSender;

    public EmailNotificationService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public void sendSlaBreachEmail(Ticket ticket) {

        String toEmail = ticket.getCreatedBy().getEmail();

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(toEmail);
        message.setSubject("🚨 SLA Breach Alert – Ticket #" + ticket.getId());
        message.setText(
                "Hello " + ticket.getCreatedBy().getUsername() + ",\n\n" +
                        "Your ticket has breached the SLA.\n\n" +
                        "Ticket Details:\n" +
                        "Title: " + ticket.getTitle() + "\n" +
                        "Priority: " + ticket.getPriority() + "\n" +
                        "SLA Deadline: " + ticket.getSlaDeadline() + "\n\n" +
                        "Our team has been notified and will take action.\n\n" +
                        "Regards,\nSmart SLA Tracker Team"
        );

        mailSender.send(message);
    }
}

