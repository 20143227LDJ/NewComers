package backend

import (
	"fmt"
	"net/http"
)

func NoticeboardFind(w http.ResponseWriter, r *http.Request) {
	r.ParseForm()

	var query string

	//go lang json 방식(marshal) 참고
	if r.Method == "POST" {
		resphonenum := r.FormValue("phone_num")
		ressex := r.FormValue("sex_text")
		resarea := r.FormValue("area_text")
		restime := r.FormValue("time_text")

		/*
			게시글을 삭제할 때는 전화번호 값만 가져와서 검색하고
			게시글을 볼 때는 지역, 시간, 성별을 확인함
		*/
		if restime == "" || ressex == "" {
			query = "SELECT * FROM noticeboard WHERE phone_num = " + "'" + resphonenum + "'" + "ORDER BY no DESC;"
		} else {
			query = "SELECT * FROM noticeboard WHERE area = " + "'" + resarea + "'" + "AND sex = " + "'" + ressex + "'" + "ORDER BY no DESC;"
		}

		jsondata := FindsQuery(db, query)
		fmt.Fprintf(w, string(jsondata))
	} else {
		//테스트용 쿼리. 주소에 직접 접속하여 POST가 아니더라도 값을 확인하기 위해
		query = "SELECT * FROM noticeboard WHERE phone_num=1234;"

		jsondata := FindsQuery(db, query)

		fmt.Fprintf(w, string(jsondata))

	}

}
