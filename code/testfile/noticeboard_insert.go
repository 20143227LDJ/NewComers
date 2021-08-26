package backend

import (
	"fmt"
	"net/http"
)

func NoticeboardInsert(w http.ResponseWriter, r *http.Request) {
	r.ParseForm()
	if r.Method == "POST" {
		var insertstring string
		resphonenum := r.FormValue("enroll_phonenum")
		resname := r.FormValue("enroll_name")
		resemail := r.FormValue("enroll_email")
		ressex := r.FormValue("enroll_sex")
		restitle := r.FormValue("enroll_contents_title")
		resmain := r.FormValue("enroll_contents")
		resarea := r.FormValue("enroll_area")
		restime := r.FormValue("enroll_time")

		if resphonenum == "" || resname == "" || resemail == "" || ressex == "" || restitle == "" || resmain == "" || resarea == "" || restime == "" {
			fmt.Fprintf(w, "fail")
		} else {
			insertstring = "INSERT INTO noticeboard(phone_num, name, email, sex, title, content, area, time_t) VALUES (" + "'" + resphonenum + "'" + "," + "'" + resname + "'" + "," + "'" + resemail + "'" + "," + "'" + ressex + "'" + "," + "'" + restitle + "'" + "," + "'" + resmain + "'" + "," + "'" + resarea + "'" + "," + "'" + restime + "'" + ");"
			InsertQuery(db, insertstring)
			fmt.Fprintf(w, "complete")
			//http.Redirect(w, r, "/send_alarm/", http.StatusFound)
		}
	} else {
		fmt.Fprintf(w, "fail")
	}
}
