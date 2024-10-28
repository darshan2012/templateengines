package com.templates.freemarker;

import com.templates.EmailSender;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateExceptionHandler;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FreeMarkerTemplateStringExample {

    public static final Vertx vertx = Vertx.vertx();
    public static final Configuration config = new Configuration(Configuration.VERSION_2_3_31);

    static {
        config.setDefaultEncoding("UTF-8");
        config.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
    }

    public static void main(String[] args) {
        vertx.deployVerticle(new EmailSender())
                .compose(result -> {
                    try {
                        // Create a map to store template data
                        Map<String, Object> data = new HashMap<>();
                        data.put("user", "John Doe");
                        data.put("value", 75);

                        List<String> seasons = List.of("Spring", "Summer", "Autumn", "Winter");
                        data.put("seasons", seasons);

                        List<Map<String, Object>> fruits = new ArrayList<>();
                        fruits.add(Map.of("name", "Apple", "price", 1.50));
                        fruits.add(Map.of("name", "Banana", "price", 0.75));
                        fruits.add(Map.of("name", "Cherry", "price", 3.00));
                        data.put("fruits", fruits);

                        // Define the template as a string

                        // Compile and render the template
                        String templateString = """
                                        <!DOCTYPE html>
                                        <html lang="en">
                                        <head>
                                            <meta charset="UTF-8">
                                            <meta name="viewport" content="width=device-width, initial-scale=1.0">
                                            <title>Welcome, ${user}</title>
                                        </head>
                                        <body>
                                            <h1>Welcome, ${user}</h1>
                                    
                                            <!-- Display an embedded image using cid -->
                                            <img src="cid:image" alt="Background Image" width="500" height="600">
                                    
                                            <p>Value: ${value}</p>
                                            <p>Half the Value: ${value / 2}</p>
                                            <p>Remainder of ${value} / 2 : ${value % 2}</p>
                                            <p>Value squared: ${value + 2 / 2 * 3}</p>
                                    
                                            <#if value < 10>
                                                <p>The value is less than 10.</p>
                                            <#elseif value == 10>
                                                <p>The value is exactly 10.</p>
                                            <#else>
                                                <p>The value is greater than 10.</p>
                                            </#if>
                                    
                                            <p>Seasons of the year:</p>
                                            <ul>
                                                <#list seasons as season>
                                                    <li>${season}</li>
                                                </#list>
                                            </ul>
                                    
                                            <p>Available fruits:</p>
                                            <table border="1" cellpadding="5" cellspacing="0">
                                                <thead>
                                                    <tr>
                                                        <th>Fruit</th>
                                                        <th>Price (in USD)</th>
                                                    </tr>
                                                </thead>
                                                <tbody>
                                                    <#list fruits as fruit>
                                                        <tr>
                                                            <td>${fruit.name}</td>
                                                            <td>${fruit.price}</td>
                                                        </tr>
                                                    </#list>
                                                </tbody>
                                            </table>
                                    
                                            <h2>Image with Table Example</h2>
                                            <table border="1" cellpadding="5" cellspacing="0">
                                                <thead>
                                                    <tr>
                                                        <th>Description</th>
                                                        <th>Image</th>
                                                    </tr>
                                                </thead>
                                                <tbody>
                                                    <tr>
                                                        <td>A nice view:</td>
                                                        <td><img src="cid:image" alt="View Image" width="200" height="200"></td>
                                                    </tr>
                                                </tbody>
                                            </table>
                                        </body>
                                        </html>
                                    """;
                        Template template = new Template("emailTemplate", new StringReader(templateString), config);
                        StringWriter writer = new StringWriter();
                        template.process(data, writer);

                        // Output rendered template to console
                        System.out.println(writer.toString());

                        // Send the email using the rendered content
                        vertx.eventBus().send("email.send", new JsonObject()
                                .put("content", writer.toString())
                                .put("subject", "Welcome Email")
                                .put("to", "darshanoffice98@gmail.com"));

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    return Future.succeededFuture();
                });
    }
}
