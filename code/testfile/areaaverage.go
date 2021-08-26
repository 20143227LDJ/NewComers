package backend

import (
	"fmt"
	"net/http"
)

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
