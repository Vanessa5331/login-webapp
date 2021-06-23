<html>
    <body>
        <h2>Add User</h2>
        <p>${error}</p>
        <p>${message}</p>
        <form action="/add-user" method="post">
            Username:<br/>
            <input type="text" name="username"/>
            <br/>
            Password:<br/>
            <input type="password" name="password">
            <br><br>
            <input type="submit" value="Submit">
        </form>
        <form action="/" method="get">
            <input type="submit" value="Back">
        </form>
    </body>
</html>