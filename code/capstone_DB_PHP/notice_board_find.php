<?php
    require_once "DBDetails.php";
    $conn = mysqli_connect(HOST, USER, PASS);

    if(mysqli_connect_errno()){
        echo "접속 실패";
    }
    mysqli_set_charset($conn, "utf8");
    mysqli_select_db($conn, DB);
 
    // 세션 시작
    session_start();


    $AreaText = $_POST['area_text'];
    $TimeText = $_POST['time_text'];
    $SexText = $_POST['sex_text'];
    
    //$query = "SELECT * FROM notice_board  WHERE area = '$AreaText' and time_t = '$TimeText' and sex = '$SexText'";
    $query = "SELECT * FROM noticeboard WHERE phone_num=1234;"

    $res = mysqli_query($conn, $query);

    $rows = array();
    $result = array();

    while($row = mysqli_fetch_array($res)){
        $rows["no"] = $row[0]
        $rows["phone_num"] = $row[1];
        $rows["name"] = $row[2];
        $rows["email"] = $row[3];
        $rows["sex"] = $row[4];
        $rows["title"] = $row[5];
        $rows["content"] = $row[6];
        $rows["area"] = $row[7];
        $rows["time_t"] = $row[8];

        array_push($result, $rows);
    }

    echo json_encode(array("results"=>$result), JSON_UNESCAPED_UNICODE);
    
    mysqli_close($conn);
?>