package backend

import (
	"fmt"
	"net/http"
)

func TourpassCheck(w http.ResponseWriter, r *http.Request) {
	r.ParseForm()

	if r.Method == "POST" {
		resphonenum := r.FormValue("phone_text")

		query := "SELECT tourpass FROM info WHERE phone_num = " + "'" + resphonenum + "'" + ";"

		result := TourpassQuery(db, query)

		fmt.Fprintf(w, string(result))
	}
}
