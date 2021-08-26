package backend

import (
	"fmt"
	"net/http"
)

func NoticeboardContentCheck(w http.ResponseWriter, r *http.Request) {
	r.ParseForm()

	if r.Method == "POST" {
		resphonenum := r.FormValue("cudb_phonenum")

		query := "SELECT * FROM info WHERE phone_num = " + "'" + resphonenum + "'" + ";"

		result := ContentCheckQuery(db, query)

		fmt.Fprintf(w, string(result))
	}
}
