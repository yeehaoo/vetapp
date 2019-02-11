<?php
if (isset($_POST["pet_id"])) {
    $pet_id = $_POST["pet_id"];
    
    require_once __DIR__ . '/db_connect.php';
    $db = new DB_CONNECT();
    $db->connect();

    $sqlCommand = "SELECT * FROM pets WHERE pet_id = '$pet_id'";
    $result = mysqli_query($db->myconn,"$sqlCommand");

    if (mysqli_num_rows($result) > 0) {
        $response["pets"] = array();
        while ($row = mysqli_fetch_array($result)) {
            $pet = array();
            $pet["pet_id"] = $row["pet_id"];
            $pet["pet_name"] = $row["pet_name"];
            $pet["pet_userid"] = $row["pet_userid"];
            $pet["pet_type"] = $row["pet_type"];
            $pet["pet_dob"] = $row["pet_dob"];      
            $pet["pet_sex"] = $row["pet_sex"];
            $pet["visits"] = array();
            
            $sqlCommand2 = "SELECT * FROM visits WHERE visit_petid = '$pet_id'";
            $result2 = mysqli_query($db->myconn,"$sqlCommand2");
            while($row = mysqli_fetch_array($result2)) {
                $visit = array();
                $visit["visit_id"] = $row["visit_id"];
                $visit["visit_petid"] = $row["visit_petid"];
                $visit["visit_userid"] = $row["visit_userid"];
                $visit["visit_date"] = $row["visit_date"];
                $visit["visit_comments"] = $row["visit_comments"];
                $pet["visits"][] = $visit;
            }
            
            array_push($response["pets"], $pet);
        }
        $response["success"] = 1;
    } else {
    $response["success"] = 0;
    $response["message"] = "No pets found";
}
    echo json_encode($response);
    $db->close($db->myconn);
}
?>