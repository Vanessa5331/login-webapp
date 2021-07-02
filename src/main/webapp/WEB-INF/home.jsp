<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
    <style>
        table {
            width:40%;
            border: 1px solid #eee;
            border-collapse: collapse;
            margin: auto;
            text-align: center
        }
        tr:nth-child(even) {
            background-color: #eee;
        }
        tr:nth-child(odd) {
            background-color: #fff;
        }
    </style>
    <body style="text-align: center">
        <h2>Welcome, ${username}</h2>
        <form action="/" method="post">
            <input type="submit" name="logout" value="Logout">
        </form>
        <h3>Your Information</h3>
        <p>${userInfo}</p>
        <form action="/edit-user" method="get">
            <input type="submit" value="Edit User">
        </form>
        <p>${error}</p>
        <p>${message}</p>
        <h3>User List</h3>
        <table>
        ${userTable}
        </table><br/>
        <form action="/add-user" method="get">
            <input type="submit" value="Add User">
        </form>
    </body>
</html>
