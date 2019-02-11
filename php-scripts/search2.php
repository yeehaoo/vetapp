<?php
session_start();
?>
<html>
<body>
<table border=1>
<?php
$table = $_POST["table"];
$id = $_POST["id"];

if($table == "users") {
    $table_id = "user_id";
} else if($table == "pets") {
    $table_id = "pet_id";
} else if($table == "visits") {
    $table_id = "visit_id";
}

require_once __DIR__ . '/db_connect.php';
$db = new DB_CONNECT();
$db->connect();

$sqlCommand = "SELECT * FROM $table WHERE $table_id = $id";
$result = mysqli_query($db->myconn,"$sqlCommand");
$fields_num = mysqli_num_fields($result);

for($i=0; $i<$fields_num; $i++) { 
    $field = mysqli_fetch_field($result);
    $htmlDisplay= $htmlDisplay."<td><b>{$field->name}</b></td>";
}

if (mysqli_num_rows($result) > 0) {
    while ($row = mysqli_fetch_row($result)) {
        $htmlDisplay= $htmlDisplay."<tr>"; 
        foreach($row as $cell){
            $htmlDisplay =$htmlDisplay."<td>$cell</td>";
        }
        $htmlDisplay =$htmlDisplay."</tr>\n";
    }
    $htmlDisplay = $htmlDisplay."</table>";
    echo $htmlDisplay;
}
else {
    echo "No upcoming appointments.";
}
$db->close($db->myconn);
?>
<br />
<a href="home.php">Back To Home</a>
</body>
</html>