<html>
    <body>
        <h2>Edit User</h2>
        <p>${error}</p>
        <p>${message}</p>
        <form action="/edit-user" method="post">
            Username:<br/>
            <input type="text" name="new-username"/>
            <br/>
            Firstname:<br/>
            <input type="text" name="new-firstname"/>
            <br/>
            Lastname:<br/>
            <input type="text" name="new-lastname"/>
            <br/>
            Password:<br/>
            <input type="password" name="new-password">
            <br><br>
            <input type="submit" value="Submit">
        </form>
        <form action="/" method="get">
            <input type="submit" value="Back">
        </form>
    </body>
</html>