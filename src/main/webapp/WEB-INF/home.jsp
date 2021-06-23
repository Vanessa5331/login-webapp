<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
    <body>
        <h2>Welcome, ${username}</h2>
        <p>${error}</p>
        <p>${message}</p>
        ${userTable}
        <form action="/add-user" method="get">
            <input type="submit" value="Add User">
        </form>
        <form action="/" method="post">
            <input type="submit" name="logout" value="Logout">
        </form>
    </body>
</html>
