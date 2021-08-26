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


    $PhoneNum = $_POST['enroll_phonenum'];
    $Name = $_POST['enroll_name'];
    $Email = $_POST['enroll_email'];
    $Sex = $_POST['enroll_sex'];
    $ContentsTitle = $_POST['enroll_contents_title'];
    $Contents = $_POST['enroll_contents'];
    $Area = $_POST['enroll_area'];
    $Time_t = $_POST['enroll_time'];

    if (($PhoneNum == null) || ($Name == null) || ($Email == null) || ($Sex == null) || ($ContentsTitle == null) || ($Contents == null) || ($Area == null) || ($Time_t == null)) {
        echo "fail";
    } else {
        $query = "INSERT INTO notice_board VALUES ('$PhoneNum', '$Name', '$Email', '$Sex', '$ContentsTitle', '$Contents', '$Area', '$Time_t')";
    
        $res = mysqli_query($conn, $query);

        echo "complete";
    }

    mysqli_close($conn);
?>