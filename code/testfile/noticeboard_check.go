package backend

import (
	"fmt"
	"net/http"
)

func NoticeboardCheck(w http.ResponseWriter, r *http.Request) {
	r.ParseForm()

	if r.Method == "POST" {
		resphonenum := r.FormValue("chkphonenum")

		query := "SELECT * FROM noticeboard_setting WHERE phone_num = " + "'" + resphonenum + "'" + ";"

		result := CheckQuery(db, query)

		fmt.Fprintf(w, string(result))
	} else {
		resphonenum := "1234"
		query := "SELECT * FROM noticeboard_setting WHERE phone_num = " + "'" + resphonenum + "'" + ";"
		result := CheckQuery(db, query)
		fmt.Fprintf(w, string(result))
	}
}
