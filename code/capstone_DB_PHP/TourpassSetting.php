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
    $TourpassText = $_POST['tourpass_text'];

    $query = "SELECT * FROM info WHERE phone_num = '$PhoneText'";

    $res = mysqli_query($connect, $query);

    $row = mysqli_fetch_array($res);

    if ($PhoneText == null){
        echo "어떻게 들어오셨나요?";
        mysqli_close($connect);
    } else {
        echo "buy";
        $update_query = "UPDATE info SET tourpass = '$TourpassText' WHERE phone_num = '$PhoneText'";
        $update_res = mysqli_query($connect, $update_query);
    }

    mysqli_close($connect);
?>