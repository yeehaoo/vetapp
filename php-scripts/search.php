<?php
session_start();
?>
<html>
<body>

<form action="search2.php" method="POST">
Search for:<br>
<input type="radio" name="table" value="users"> User<br>
<input type="radio" name="table" value="pets"> Pet<br>
<input type="radio" name="table" value="visits"> Visit<br>
ID:
<input type="text" name="id" required>
<br />
<input type="submit" value="Search">
</form>
<br />
<a href="home.php">Back To Home</a>

</body>
</html>