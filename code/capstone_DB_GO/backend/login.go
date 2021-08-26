package backend

import (
	"fmt"
	"net/http"

	"github.com/labstack/echo"
)

func Echo_Login(c echo.Context) error {
	resid := c.FormValue("u_id")
	respw := c.FormValue("u_pass")

	query := "SELECT IF(STRCMP(pass," + "'" + respw + "'" + "),0,1) FROM info WHERE phone_num = " + "'" + resid + "'" + ";"

	result := SelectQuery(db, query, "login")
	if result == "1" {
		return c.HTML(http.StatusOK, fmt.Sprint("true"))
	} else {
		return c.HTML(http.StatusOK, fmt.Sprint("false"))
	}
}

func Echo_Signup(c echo.Context) error {
	resname := c.FormValue("u_name")
	resphonenum := c.FormValue("u_id")
	respass := c.FormValue("u_pass")
	resadd := c.FormValue("u_address")
	resemail := c.FormValue("u_email")
	ressex := c.FormValue("u_sex")

	query := "SELECT phone_num FROM info WHERE phone_num =" + "'" + resphonenum + "'" + ";"

	result := SelectQuery(db, query, "signup")

	if result != "" {
		return c.HTML(http.StatusOK, fmt.Sprint("insert_fail"))
	} else {
		insertquery := "INSERT INTO info VALUES (" + "'" + resname + "'" + "," + "'" + resphonenum + "'" + "," + "'" + respass + "'" + "," + "'" + resadd + "'" + "," + "'" + resemail + "'" + "," + "'" + ressex + "'" + ", 'FALSE', 'NOT USE');"
		InsertQuery(db, insertquery)

		return c.HTML(http.StatusOK, fmt.Sprint("insert_complete"))
	}
}

func Echo_FindPW(c echo.Context) error {
	resname := c.FormValue("u_name")
	resnum := c.FormValue("u_num")

	if c.Request().Method == "POST" {
		query := "SELECT pass FROM info WHERE name = " + "'" + resname + "'" + " AND phone_num = " + "'" + resnum + "'" + ";"
		result := SelectQuery(db, query, "findpw")
		//fmt.Println(string(result))
		return c.HTML(http.StatusOK, fmt.Sprint(string(result)))
	} else {
		return c.HTML(http.StatusOK, fmt.Sprint(c, "비밀번호를 찾지 못했습니다."))
	}
}

/*
 로그인 기능
*/
func Login(w http.ResponseWriter, r *http.Request) {
	r.ParseForm()

	if r.Method == "POST" {
		resid := r.FormValue("u_id")
		respw := r.FormValue("u_pass")

		query := "SELECT IF(STRCMP(pass," + "'" + respw + "'" + "),0,1) FROM info WHERE phone_num = " + "'" + resid + "'" + ";"

		result := SelectQuery(db, query, "login")
		if result == "1" {
			fmt.Fprintf(w, "true")
		} else {
			fmt.Fprintf(w, "false")
		}
	} else {
		fmt.Fprintf(w, "잘못된 접근입니다.")
	}
}

/*
 회원가입 기능
*/
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

func FindPW(w http.ResponseWriter, r *http.Request) {
	r.ParseForm()

	resname := r.FormValue("u_name")
	resnum := r.FormValue("u_num")

	//go lang json 방식(marshal) 참고
	if r.Method == "POST" {
		query := "SELECT pass FROM info WHERE name = " + "'" + resname + "'" + " AND phone_num = " + "'" + resnum + "'" + ";"
		//query := "SELECT title, content, image FROM area"

		result := SelectQuery(db, query, "findpw")
		fmt.Fprintf(w, string(result))
	} else {

	}
}
