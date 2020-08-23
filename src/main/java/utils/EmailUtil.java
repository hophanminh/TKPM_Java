package utils;

import Model.Class.Customer;
import javafx.stage.Stage;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Date;
import java.util.List;
import java.util.Properties;

public class EmailUtil {
    public int sendEmail(Stage thisStage, List<Customer> customerList, String title, String mgs) throws MessagingException {
        // Sender's email ID needs to be mentioned
        String from = "hpmntt@gmail.com";
        String password = "Phanminh195";

        // Get mail properties
        Properties properties = new Properties();
        properties.put("mail.smtp.host","smtp.gmail.com");
        properties.put("mail.smtp.socketFactory.port","465");
        properties.put("mail.smtp.socketFactory.class","javax.net.ssl.SSLSocketFactory");
        properties.put("mail.smtp.auth","true");
        properties.put("mail.smtp.port","465");

        // Get the default Session object.
        Session session = Session.getDefaultInstance(properties, new javax.mail.Authenticator(){
            protected PasswordAuthentication getPasswordAuthentication(){
                return new PasswordAuthentication(from, password);
            }
        });

        Transport transport = session.getTransport();
        transport.connect();

        int sum = 0;
        for(int i = 0 ; i < customerList.size(); i++){
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            InternetAddress[] address = {new InternetAddress(customerList.get(i).getEmailCustomer())};
            message.setRecipients(Message.RecipientType.TO, address);

            message.setSubject(title);
            message.setContent(mgs, "text/html");
            message.setSentDate(new Date());

            message.saveChanges();
            transport.sendMessage(message, address);
            System.out.println("Send Email successfully");
            sum++;
        }
        transport.close();
        return sum;
    }
}
