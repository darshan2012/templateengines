package com.templates.pebble;

import com.mitchellbosecke.pebble.PebbleEngine;
import com.mitchellbosecke.pebble.template.PebbleTemplate;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import com.templates.EmailSender;

import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
//public class PebbleTemplateStringExample
public class PebbleTemplateStringExample {

    public static final Vertx vertx = Vertx.vertx();
    public static final PebbleEngine engine = new PebbleEngine.Builder().build();

    public static void main(String[] args) {
        vertx.deployVerticle(new EmailSender())
                .compose(result -> {
                    try {
                        // Create a map to store template data
                        Map<String, Object> data = new HashMap<>();

                        // Add a string (user) to the Map
                        data.put("user", "John Doe");
                        data.put("value", 75); // This will be used in arithmetic and conditional logic

                        // Add a list of strings (seasons) to the Map
                        List<String> seasons = new ArrayList<>();
                        seasons.add("Spring");
                        seasons.add("Summer");
                        seasons.add("Autumn");
                        seasons.add("Winter");
                        data.put("seasons", seasons);

                        // Add a list of maps (fruits) to the Map
                        List<Map<String, Object>> fruits = new ArrayList<>();

                        // Each fruit is a Map with name and price
                        Map<String, Object> apple = new HashMap<>();
                        apple.put("name", "Apple");
                        apple.put("price", 1.50);
                        fruits.add(apple);

                        Map<String, Object> banana = new HashMap<>();
                        banana.put("name", "Banana");
                        banana.put("price", 0.75);
                        fruits.add(banana);

                        Map<String, Object> cherry = new HashMap<>();
                        cherry.put("name", "Cherry");
                        cherry.put("price", 3.00);
                        fruits.add(cherry);

                        // Add fruits list to data
                        data.put("fruits", fruits);

                        // Define the template as a string
                        var templateString = """
                                        <!DOCTYPE html>
                                        <html lang="en">
                                        <head>
                                            <meta charset="UTF-8">
                                            <meta name="viewport" content="width=device-width, initial-scale=1.0">
                                            <title>Welcome, {{ user }}</title>
                                        </head>
                                        <body>
                                            <h1>Welcome, {{ user }}</h1>
                                    
                                            <!-- Display an embedded image using cid -->
                                            <img src="cid:image" alt="Background Image" width="500" height="600">
                                    
                                            <!-- Display the value and perform arithmetic operations -->
                                            <p>Value: {{ value }}</p>
                                            <p>Half the Value: {{ value / 2 }}</p>
                                            <p>Remainder of {{value}} / 2 : {{value % 2}}</p>
                                            <p>Value squared: {{ value + 2 / 2 * 3 }}</p>
                                    
                                            <!-- Conditional statement to compare value -->
                                            {% if value > 10 %}
                                                <p>The value is greater than 10.</p>
                                            {% elseif value == 10 %}
                                                <p>The value is exactly 10.</p>
                                            {% else %}
                                                <p>The value is less than 10.</p>
                                            {% endif %}
                                    
                                            <!-- Loop through an array of seasons -->
                                            <p>Seasons of the year:</p>
                                            <ul>
                                                {% for season in seasons %}
                                                    <li>{{ season }}</li>
                                                {% endfor %}
                                            </ul>
                                    
                                            <!-- Loop through an array of fruits and display them in a table -->
                                            <p>Available fruits:</p>
                                            <table border="1" cellpadding="5" cellspacing="0">
                                                <thead>
                                                    <tr>
                                                        <th>Fruit</th>
                                                        <th>Price (in USD)</th>
                                                    </tr>
                                                </thead>
                                                <tbody>
                                                    {% for fruit in fruits %}
                                                        <tr>
                                                            <td>{{ fruit.name }}</td>
                                                            <td>${{ fruit.price }}</td>
                                                        </tr>
                                                    {% endfor %}
                                                </tbody>
                                            </table>
                                    
                                            <!-- Show image with table -->
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

                        // Compile the string template
                        var template = engine.getLiteralTemplate(templateString);

                        // Render the template into a string
                        var writer = new StringWriter();
                        template.evaluate(writer, data);

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
