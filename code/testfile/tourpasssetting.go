package backend

import (
	"fmt"
	"net/http"
)

func TourpassSetting(w http.ResponseWriter, r *http.Request) {
	r.ParseForm()

	if r.Method == "POST" {
		resphonenum := r.FormValue("phone_text")
		restourpass := r.FormValue("tourpass_text")

		if resphonenum == "" {
			fmt.Fprintf(w, "어떻게 들어오셨나요?")
		} else {
			fmt.Fprintf(w, "buy")
			updatequery := "UPDATE info SET tourpass = " + "'" + restourpass + "'" + "WHERE phone_num = " + "'" + resphonenum + "'" + ";"
			UpdateQuery(db, updatequery)
		}
	}
}
