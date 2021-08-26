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
    $AreaText = $_POST['area_text'];
    $TimeText = $_POST['time_text'];
    $SexText = $_POST['sex_text'];

    $query = "SELECT * FROM notice_board_setting WHERE phone_num = '$PhoneText'";

    $res = mysqli_query($connect, $query);

    $row = mysqli_fetch_array($res);

    if (($PhoneText == null) || ($AreaText == null) || ($TimeText == null) || ($SexText == null)){
        echo "필수 입력 사항입니다.";
        mysqli_close($connect);
    } else {
        if ($row[0] == $PhoneText){
            echo "update";
            $update_query = "UPDATE notice_board_setting SET area = '$AreaText', time_t = '$TimeText', sex = '$SexText' where phone_num = '$PhoneText'"; 
            $update_res = mysqli_query($connect, $update_query);
        } else {
            echo "insert";
            $insert_query = "INSERT INTO notice_board_setting VALUES ('$PhoneText', '$AreaText', '$TimeText', '$SexText', '')";
            $insert_res = mysqli_query($connect, $insert_query);
        }
    }

/*
    $PhoneText = $_POST['phone_text'];
    $AreaText = $_POST['area_text'];
    $TimeText = $_POST['time_text'];
    $SexText = $_POST['sex_text'];
    $PhoneText = '111';
    $AreaText = '111';
    $TimeText = '111';
    $SexText = '1';

    //$query = "SELECT IF(strcmp(phone_num,'$PhoneText'),0,1) FROM notice_board_setting";
    //$query = "INSERT INTO notice_board_setting VALUES ('$PhoneText', '$AreaText', '$TimeText', '$SexText')";
    $insert_query = "INSERT INTO notice_board_setting VALUES ('1111', '1111', '1111', '1111')";

    $insert_res = mysqli_query($connect, $insert_query);
    */
    //$res = mysqli_query($connect, $query);

    //$row = mysqli_fetch_array($res);
/*
    if(($PhoneText == null) || ($AreaText == null) || ($TimeText == null) || ($SexText == null)){
        mysqli_close($connect);
    }
    else{
        if ($row[0]) {
            echo "update";
            $update_query = "UPDATE notice_board_setting SET area = '$AreaText', time_t = '$TimeText', sex = '$SexText' where phone_num = '$PhoneText'"; 
            $update_res = mysqli_query($connect, $update_query);
        } else {
            echo "insert";
            $insert_query = "INSERT INTO notice_board_setting VALUES ('$PhoneText', '$AreaText', '$TimeText', '$SexText')";
            $insert_res = mysqli_query($connect, $insert_query);
        }
    }
*/
/*
    $res = mysqli_query($conn, $query);

    $rows = array();
    $result = array();

    while($row = mysqli_fetch_array($res)){
        $rows["phone_num"] = $row[0];
        $rows["area"] = $row[1];
        $rows["time_t"] = $row[2];
        $rows["sex"] = $row[3];
        
        array_push($result, $rows);

    }
    echo json_encode(array("results"=>$result), JSON_UNESCAPED_UNICODE);
    */

    mysqli_close($connect);
?>