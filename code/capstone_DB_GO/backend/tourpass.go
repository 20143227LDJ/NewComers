package backend

import (
	"fmt"
	"net/http"

	"github.com/labstack/echo"
)

func Echo_Tourpasssetting(c echo.Context) error {
	resphonenum := c.FormValue("phone_text")
	restourpass := c.FormValue("tourpass_text")

	if resphonenum == "" {
		return c.HTML(http.StatusOK, fmt.Sprint("어떻게 들어오셨나요?"))
	} else {
		updatequery := "UPDATE info SET tourpass = " + "'" + restourpass + "'" + "WHERE phone_num = " + "'" + resphonenum + "'" + ";"
		UpdateQuery(db, updatequery)
		return c.HTML(http.StatusOK, fmt.Sprint("buy"))
	}
}

func Echo_Tourpasscheck(c echo.Context) error {
	resphonenum := c.FormValue("phone_text")
	query := "SELECT tourpass FROM info WHERE phone_num = " + "'" + resphonenum + "'" + ";"
	result := TourpassQuery(db, query)
	return c.HTML(http.StatusOK, fmt.Sprint(string(result)))
}

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

func TourpassCheck(w http.ResponseWriter, r *http.Request) {
	r.ParseForm()

	if r.Method == "POST" {
		resphonenum := r.FormValue("phone_text")

		query := "SELECT tourpass FROM info WHERE phone_num = " + "'" + resphonenum + "'" + ";"

		result := TourpassQuery(db, query)

		fmt.Fprintf(w, string(result))
	}
}
