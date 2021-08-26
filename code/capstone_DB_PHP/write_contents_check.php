<?php
    require_once "dbDetails.php";
    $conn = mysqli_connect(HOST, USER, PASS);

    if(mysqli_connect_errno()){
        echo "접속 실패";
    }
    mysqli_set_charset($conn, "utf8");
    mysqli_select_db($conn, DB);
 
    // 세션 시작
    session_start();


    $PhoneNum = $_POST['cudb_phonenum'];
    
    $query = "SELECT * FROM info  WHERE phone_num = '$PhoneNum'";
    
    $res = mysqli_query($conn, $query);

    $rows = array();
    $result = array();

    while($row = mysqli_fetch_array($res)){
        $rows["phone_num"] = $row[0];
        $rows["name"] = $row[2];
        $rows["email"] = $row[4];
        $rows["sex"] = $row[5];
        array_push($result, $rows);
    }

    echo json_encode(array("results"=>$result), JSON_UNESCAPED_UNICODE);
    
    mysqli_close($conn);
?>