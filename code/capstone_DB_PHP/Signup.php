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


    $UserNA = $_POST['u_name'];
    $UserID = $_POST['u_id'];
    $UserPW = $_POST['u_pass'];
    $UserAD = $_POST['u_address'];
    $UserEM = $_POST['u_email'];
    $UserSEX = $_POST['u_sex'];

    $query = "SELECT * FROM info WHERE phone_num = '$UserID'";
    
    $res = mysqli_query($connect, $query);

    $row = mysqli_fetch_array($res);

    if(($row[0] == $UserID) || ($UserID == null) || ($UserID == "")){
        echo "insert_fail";
    }
    else{
        echo "insert_complete";
        $insert_query = "INSERT INTO info VALUES ('$UserID', '$UserPW', '$UserNA', '$UserAD', '$UserEM', '$UserSEX', 'FALSE')";
        $insert_res = mysqli_query($connect, $insert_query);
    }
    mysqli_close($connect);
?>