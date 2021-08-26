package backend

import (
	"fmt"
	"net/http"
)

func SingUp(w http.ResponseWriter, r *http.Request) {
	r.ParseForm()

	if r.Method == "POST" {
		resname := r.FormValue("u_name")
		resphonenum := r.FormValue("u_id")
		respass := r.FormValue("u_pass")
		resadd := r.FormValue("u_address")
		resemail := r.FormValue("u_email")
		ressex := r.FormValue("u_sex")

		query := "SELECT phone_num FROM info WHERE phone_num =" + "'" + resphonenum + "'" + ";"

		result := SelectQuery(db, query, "signup")

		if result != "" {
			fmt.Fprintf(w, "insert_fail")
		} else {
			fmt.Fprintf(w, "insert_complete")
			insertquery := "INSERT INTO info VALUES (" + "'" + resname + "'" + "," + "'" + resphonenum + "'" + "," + "'" + respass + "'" + "," + "'" + resadd + "'" + "," + "'" + resemail + "'" + "," + "'" + ressex + "'" + ", 'FALSE', 'NOT USE');"

			InsertQuery(db, insertquery)
		}
	}
}
