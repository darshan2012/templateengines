package com.templates;

public class templates {
    public static final String TEMPLATE_STRING = """
            <!DOCTYPE html>
            <html lang="en">
            <head>
                <meta charset="UTF-8">
                <meta name="viewport" content="width=device-width, initial-scale=1.0">
                <title>Welcome, {{ user }}</title>
            </head>
            <body>
                <h1>Welcome, {{ user }}</h1>
                <img src="cid:image1" alt="Background Image" width="500" height="600">
                <p>Value: {{ value }}</p>
                <p>Half the Value: {{ value / 2 }}</p>
                <p>Value squared: {{ value * value }}</p>
                {% if value > 10 %}
                    <p>The value is greater than 10.</p>
                {% elseif value == 10 %}
                    <p>The value is exactly 10.</p>
                {% else %}
                    <p>The value is less than 10.</p>
                {% endif %}
                <ul>
                    {% for season in seasons %}
                        <li>{{ season }}</li>
                    {% endfor %}
                </ul>
                <table border="1" cellpadding="5" cellspacing="0">
                    <thead><tr><th>Fruit</th><th>Price (in USD)</th></tr></thead>
                    <tbody>
                    {% for fruit in fruits %}
                        <tr><td>{{ fruit.name }}</td><td>{{ fruit.price }}</td></tr>
                    {% endfor %}
                    </tbody>
                </table>
            </body>
            </html>
            """;
}
