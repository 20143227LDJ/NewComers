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


    $PhoneNum = $_POST['chkphonenum'];
    $PhoneNum = '01049373143';

    $query = "SELECT * FROM notice_board_setting WHERE phone_num = '$PhoneNum'";
    
    $res = mysqli_query($conn, $query);

    $rows = array();
    $result = array();

    //$setting_rows = array();
    //$setting_result = array();

    while($row = mysqli_fetch_array($res)){
        $rows["phone_num"] = $row[0];
        $rows["area"] = $row[1];
        $rows["time_t"] = $row[2];
        $rows["sex"] = $row[3];
        $rows["authority"] = $row[4];

        array_push($result, $rows);
    }

    
    echo json_encode(array("results"=>$result), JSON_UNESCAPED_UNICODE);
    
    //echo json_encode(array("results"=>$result), JSON_UNESCAPED_UNICODE);
    //mysql_close($conn);

    /*
    mysqli_data_seek($result, 0);

    $setting_query = "SELECT * FROM notice_board  WHERE area = '$rows[area]' and time_t = '$rows[time_t]' and sex = '$rows[sex]'";
    $setting_res = mysqli_query($conn, $setting_query);
    */

    /*
    if (($rows["area"] == null) || ($rows["time_t"] == null) || ($rows["sex"] == null)){
        echo "check fail";
    } else {
        echo json_encode(array("results"=>$result), JSON_UNESCAPED_UNICODE);
    }
    */

    //기기에서 되는지 테스트 해봐야 함
    /*
    if (($rows["area"] == null) || ($rows["time_t"] == null) || ($rows["sex"] == null)){
        echo "check fail";
    } else {
        while($setting_row = mysqli_fetch_array($setting_res)){
            $setting_rows["phone_num"] = $setting_row[0];
            $setting_rows["name"] = $setting_row[1];
            $setting_rows["email"] = $setting_row[2];
            $setting_rows["sex"] = $setting_row[3];
            $setting_rows["contents"] = $setting_row[4];
            $setting_rows["area"] = $setting_row[5];
            $setting_rows["time_t"] = $setting_row[6];

            array_push($setting_result, $setting_rows);
        }
        echo json_encode(array("results"=>$setting_result), JSON_UNESCAPED_UNICODE);
    }
    */
    mysqli_close($conn);
?>