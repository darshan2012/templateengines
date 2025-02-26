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

<!-- Display the value and perform arithmetic operations -->
<p>Value: ${value}</p>
<p>Half the Value: ${value / 2}</p>
<p>Value squared: ${value * value}</p>

<!-- Conditional statement to compare value -->
<#if value < 10>
    <p>The value is less than 10.</p>
<#elseif value == 10>
    <p>The value is exactly 10.</p>
<#else>
    <p>The value is greater than 10.</p>
</#if>

<!-- Loop through an array of seasons -->
<p>Seasons of the year:</p>
<ul>
    <#list seasons as season>
        <li>${season}</li>
    </#list>
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
    <#list fruits as fruit>
        <tr>
            <td>${fruit.name}</td>
            <td>${fruit.price}</td>
        </tr>
    </#list>
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
