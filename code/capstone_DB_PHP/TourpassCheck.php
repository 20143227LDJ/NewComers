<?php
    require_once "DBDetails.php";
    header('content-type: text/html; charset=utf-8'); 
    // 데이터베이스 접속 문자열. (db위치, 유저 이름, 비밀번호)
    $connect = mysqli_connect(HOST, USER, PASS, DB) or die( "SQL server에 연결할 수 없습니다.");
 
    mysqli_query("SET NAMES UTF8");
    // 데이터베이스 선택
    mysqli_select_db($connect, DB);
  
    // 세션 시작
    session_start();

    $PhoneText = $_POST['phone_text'];

    $query = "SELECT * FROM info WHERE phone_num = '$PhoneText'";

    $res = mysqli_query($connect, $query);

    $rows = array();
    $result = array();

    while($row = mysqli_fetch_array($res)){
        $rows["tourpass"] = $row[6];

        array_push($result, $rows);
    }
    echo json_encode(array("results"=>$result), JSON_UNESCAPED_UNICODE);

    mysqli_close($connect);
?>