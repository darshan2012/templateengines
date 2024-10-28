package com.templates.pebble;

import com.templates.EmailSender;
import io.vertx.core.Future;
import io.vertx.core.Vertx;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.templ.pebble.PebbleTemplateEngine;

public class PebbleTemplateExample
{

    public static final Vertx vertx = Vertx.vertx();
    public static final PebbleTemplateEngine engine = PebbleTemplateEngine.create(vertx);

    public static void main(String[] args)
    {
        vertx.deployVerticle(new EmailSender())
                .compose(result ->
                {
                    // Create a JsonObject for the data
                    JsonObject data = new JsonObject();

                    // Add a string (user) to the JsonObject
                    data.put("user", "John Doe");
                    data.put("value", 75); // This will be used in arithmetic and conditional logic

                    // Add an array of strings (seasons) to the JsonObject
                    JsonArray seasons = new JsonArray();
                    seasons.add("Spring");
                    seasons.add("Summer");
                    seasons.add("Autumn");
                    seasons.add("Winter");
                    data.put("seasons", seasons);

                    // Add an array of objects (fruits) to the JsonObject
                    JsonArray fruits = new JsonArray();

                    // Each fruit is a JsonObject with name and price
                    fruits.add(new JsonObject().put("name", "Apple").put("price", 1.50));
                    fruits.add(new JsonObject().put("name", "Banana").put("price", 0.75));
                    fruits.add(new JsonObject().put("name", "Cherry").put("price", 3.00));

                    // Add fruits array to data
                    data.put("fruits", fruits);

                    // Render the template using Pebble with the JsonObject data
                    engine.render(data, "src/main/resources/template.peb", res ->
                    {
                        if (res.succeeded())
                        {
                            System.out.println(res.result());

                            // Send email with rendered HTML template (Optional)
                            vertx.eventBus()
                                    .send("email.send", new JsonObject()
                                            .put("content", res.result().toString())
                                            .put("subject", "Welcome Email")
                                            .put("to", "darshanoffice98@gmail.com"));

                        }
                        else
                        {
                            System.out.println("Failed to render template: " + res.cause().getMessage());
                        }
                    });
                    return Future.succeededFuture();
                });
    }
}