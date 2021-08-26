package backend

import (
	"fmt"
	"net/http"
)

func NoticeboardDelete(w http.ResponseWriter, r *http.Request) {
	r.ParseForm()

	if r.Method == "POST" {
		resno := r.FormValue("num")

		query := "DELETE FROM noticeboard WHERE no =" + "'" + resno + "'" + ";"

		DeleteQuery(db, query)
		fmt.Fprintf(w, "delete")
	}
}
