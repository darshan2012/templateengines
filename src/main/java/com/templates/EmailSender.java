package com.templates;

import io.github.cdimascio.dotenv.Dotenv;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.mail.*;

import java.nio.file.Files;
import java.nio.file.Paths;

public class EmailSender extends AbstractVerticle
{
    private static final Dotenv env = Dotenv.load();

    private final MailConfig mailConfig = new MailConfig()
            .setHostname("smtp.gmail.com")
            .setPort(587)
            .setStarttls(StartTLSOptions.REQUIRED)
            .setLogin(LoginOption.REQUIRED)
            .setUsername(env.get("EMAIL"))
            .setPassword(env.get("PASSWORD"));

    @Override
    public void start()
    {
        vertx.eventBus().<JsonObject>localConsumer("email.send", mail ->
        {
            try
            {
                var client = MailClient.create(vertx, mailConfig);

                // Log the content for debugging
                System.out.println("Email content: " + mail.body().getString("content"));

                var message = new MailMessage();
                message.setFrom(env.get("EMAIL"));
                message.setTo(mail.body().getString("to"));
                message.setSubject(mail.body().getString("subject", ""));

                // Set the HTML content for the email
                message.setHtml(mail.body().getString("content"));

                // Add the image attachment
                var imagePath = "/root/IdeaProjects/templates/src/main/resources/images.jpeg"; // Replace with the actual path to your image
                var imageBuffer = Buffer.buffer(Files.readAllBytes(Paths.get(imagePath)));

                var attachment = MailAttachment.create()
                        .setContentType("image/jpeg")  // Set content type based on image format
                        .setData(imageBuffer)          // Attach the image data
                        .setName("image.jpeg")          // Set the file name
//                        .setDisposition("image.jpeg")
                        .setContentId("image"); // Set it as a file attachment

                message.setInlineAttachment(attachment); // Add the attachment to the email

                // Log the message details for debugging
                System.out.println("Message: " + message);

                // Send the email
                client.sendMail(message).onComplete(result ->
                {
                    if (result.succeeded())
                    {
                        System.out.println("Email sent successfully.");
                    }
                    else
                    {
                        System.out.println("Failed to send email: " + result.cause().getMessage());
                    }
                });
            }
            catch (Exception exception)
            {
                exception.printStackTrace();
                System.out.println("Exception while sending email: " + exception.getMessage());
            }
        });
    }
}
