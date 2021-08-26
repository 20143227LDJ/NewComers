package backend

import (
	"fmt"
	"net/http"

	"github.com/labstack/echo"
)

func Echo_Areasetting(c echo.Context) error {
	var query string

	resphonenum := c.FormValue("phone_text")
	resarea := c.FormValue("area_text")
	restime := c.FormValue("time_text")
	ressex := c.FormValue("sex_text")

	selectquery := "SELECT phone_num FROM noticeboard_setting WHERE phone_num = " + "'" + resphonenum + "'" + ";"
	queryphonenum := SelectQuery(db, selectquery, "phone_num")

	select2query := "SELECT area FROM noticeboard_setting WHERE phone_num = " + "'" + resphonenum + "'" + ";"
	queryarea := SelectQuery(db, select2query, "area")

	if queryarea == "부산" {
		queryarea = "busan"
	} else if queryarea == "서울" {
		queryarea = "seoul"
	} else if queryarea == "대구" {
		queryarea = "daegu"
	}
	//

	if resphonenum == "" || resarea == "" || restime == "" || ressex == "" {
		return c.HTML(http.StatusOK, fmt.Sprint("필수 입력사항입니다."))
	}
	if queryphonenum == resphonenum {
		query = "UPDATE noticeboard_setting SET area = " + "'" + resarea + "'" + ", time_t = " + "'" + restime + "'" + ", sex = " + "'" + ressex + "'" + "WHERE phone_num=" + "'" + resphonenum + "'" + ";"
		UpdateQuery(db, query)
		return c.String(0, queryarea)
	} else {
		query = "INSERT INTO noticeboard_setting VALUES (" + "'" + resphonenum + "'" + "," + "'" + resarea + "'" + "," + "'" + restime + "'" + "," + "'" + ressex + "'" + "," + "'" + "'" + ");"
		InsertQuery(db, query)
		return c.String(0, queryarea)
	}
	//영어로 바꾸어 데이터베이스에 저장
	if resarea == "부산" {
		resarea = "busan"
	} else if resarea == "서울" {
		resarea = "seoul"
	} else if resarea == "대구" {
		resarea = "daegu"
	}
	updatequery := "UPDATE info SET topic = " + "'" + resarea + "'" + "WHERE phone_num=" + "'" + resphonenum + "'" + ";"
	UpdateQuery(db, updatequery)
	return c.HTML(http.StatusOK, fmt.Sprint(queryarea))
}

func Echo_Areainfo(c echo.Context) error {
	query := "SELECT area.title, area.content, area.image, COALESCE(AVG(evaluation.preference), 1) FROM area LEFT JOIN evaluation ON area.title = evaluation.area_detail GROUP BY area.title ORDER BY area.no"
	result := AreaQuery(db, query)
	return c.HTML(http.StatusOK, fmt.Sprint(string(result)))
	//return c.HTML(http.StatusOK, fmt.Sprint(string(result)))
}

func Echo_Areaaverage(c echo.Context) error {
	query := "SELECT title, content, image, AVG(preference) FROM area"
	result := AreaQuery(db, query)
	return c.HTML(http.StatusOK, fmt.Sprint(string(result)))
}

/*
 사용자의 지역 설정
 파이어베이스 푸시 메시지를 보내기 위한 토픽 관리도 포함
*/
func AreaSetting(w http.ResponseWriter, r *http.Request) {
	r.ParseForm()

	var query string

	//go lang json 방식(marshal) 참고
	if r.Method == "POST" {
		resphonenum := r.FormValue("phone_text")
		resarea := r.FormValue("area_text")
		restime := r.FormValue("time_text")
		ressex := r.FormValue("sex_text")

		selectquery := "SELECT phone_num FROM noticeboard_setting WHERE phone_num = " + "'" + resphonenum + "'" + ";"
		queryphonenum := SelectQuery(db, selectquery, "phone_num")

		//이전 지역(토픽)을 찾기 위한 변수
		select2query := "SELECT area FROM noticeboard_setting WHERE phone_num = " + "'" + resphonenum + "'" + ";"
		queryarea := SelectQuery(db, select2query, "area")

		if queryarea == "부산" {
			queryarea = "busan"
		} else if queryarea == "서울" {
			queryarea = "seoul"
		} else if queryarea == "대구" {
			queryarea = "daegu"
		}
		//

		if resphonenum == "" || resarea == "" || restime == "" || ressex == "" {
			fmt.Fprintf(w, "필수 입력사항입니다.")
		} else {
			if queryphonenum == resphonenum {
				fmt.Fprintf(w, queryarea) //이전 지역(토픽) 출력
				query = "UPDATE noticeboard_setting SET area = " + "'" + resarea + "'" + ", time_t = " + "'" + restime + "'" + ", sex = " + "'" + ressex + "'" + "WHERE phone_num=" + "'" + resphonenum + "'" + ";"
				UpdateQuery(db, query)

			} else {
				fmt.Fprintf(w, queryarea) //이전 지역(토픽) 출력
				query = "INSERT INTO noticeboard_setting VALUES (" + "'" + resphonenum + "'" + "," + "'" + resarea + "'" + "," + "'" + restime + "'" + "," + "'" + ressex + "'" + "," + "'" + "'" + ");"
				InsertQuery(db, query)
			}
			//영어로 바꾸어 데이터베이스에 저장
			if resarea == "부산" {
				resarea = "busan"
			} else if resarea == "서울" {
				resarea = "seoul"
			} else if resarea == "대구" {
				resarea = "daegu"
			}
			updatequery := "UPDATE info SET topic = " + "'" + resarea + "'" + "WHERE phone_num=" + "'" + resphonenum + "'" + ";"
			UpdateQuery(db, updatequery)
		}

	} else {
		//테스트용 쿼리. 주소에 직접 접속하여 POST가 아니더라도 값을 확인하기 위해
		query = "SELECT * FROM noticeboard WHERE phone_num=1234;"

		jsondata := FindsQuery(db, query)

		fmt.Fprintf(w, string(jsondata))
	}
}

/*
 구글 맵 지역 정보 출력
 커스텀된 이미지 및 평균 선호도 출력
*/
func AreaInfo(w http.ResponseWriter, r *http.Request) {
	r.ParseForm()

	//go lang json 방식(marshal) 참고
	if r.Method == "POST" {
		query := "SELECT area.title, area.content, area.image, COALESCE(AVG(evaluation.preference), 1) FROM area LEFT JOIN evaluation ON area.title = evaluation.area_detail GROUP BY area.title ORDER BY area.no"
		//query := "SELECT title, content, image FROM area"

		result := AreaQuery(db, query)
		fmt.Fprintf(w, string(result))
	} else {
		//테스트용 쿼리. 주소에 직접 접속하여 POST가 아니더라도 값을 확인하기 위해
	}
}

/*
 왜 만들었는지 기억 안남. 확인 필요.
*/
func AreaAverage(w http.ResponseWriter, r *http.Request) {
	r.ParseForm()

	//go lang json 방식(marshal) 참고
	if r.Method == "POST" {
		query := "SELECT title, content, image, AVG(preference) FROM area"

		result := AreaQuery(db, query)
		fmt.Fprintf(w, string(result))
	} else {
		//테스트용 쿼리. 주소에 직접 접속하여 POST가 아니더라도 값을 확인하기 위해
	}

}
