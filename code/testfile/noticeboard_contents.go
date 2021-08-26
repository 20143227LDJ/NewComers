package backend

import (
	"fmt"
	"net/http"
)

func NoticeboardContents(w http.ResponseWriter, r *http.Request) {
	r.ParseForm()

	//go lang json 방식(marshal) 참고
	if r.Method == "POST" {
		rescontent := r.FormValue("u_contents_title")

		query := "SELECT * FROM noticeboard WHERE title = " + "'" + rescontent + "'" + ";"

		result := FindsQuery(db, query)

		fmt.Fprintf(w, string(result))
	} else {
		//테스트용 쿼리. 주소에 직접 접속하여 POST가 아니더라도 값을 확인하기 위해
		rescontent := "test title"

		query := "SELECT * FROM noticeboard WHERE title = " + "'" + rescontent + "'" + ";"

		result := FindsQuery(db, query)

		fmt.Fprintf(w, string(result))
	}
}
