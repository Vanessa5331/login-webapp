<html>
    <body>
    <h2>Remove User</h2>
        <p>Do you really want to remove ${removeUser}?</p>
        <form action="/remove-user" method="post">
            <input type="submit" name="remove" value="Yes">
            <input type="hidden" name="removeUser" value=${removeUser}>
        </form>
        <form action="/" method="get">
            <input type="submit" value="No">
        </form>
    </body>
</html>