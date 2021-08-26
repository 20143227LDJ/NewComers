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


    $UserID = $_POST['u_id'];
    $UserPW = $_POST['u_pass'];

    $query = "SELECT IF(strcmp(pass,'$UserPW'),0,1) FROM info WHERE phone_num = '$UserID'";
    
    $res = mysqli_query($connect, $query);

    $row = mysqli_fetch_array($res);

    if($row[0]){ // $row[0] 1일때
        echo "true";
    }
    else{
        echo "false";
    }
    mysqli_close($connect);

?>