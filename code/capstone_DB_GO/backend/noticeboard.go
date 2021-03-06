package backend

import (
	"fmt"
	"net/http"
	"strconv"
	"strings"

	"github.com/labstack/echo"
)

func Echo_Noticeboardcheck(c echo.Context) error {
	resphonenum := c.FormValue("chkphonenum")
	query := "SELECT * FROM noticeboard_setting WHERE phone_num = " + "'" + resphonenum + "'" + ";"
	result := CheckQuery(db, query)
	return c.HTML(http.StatusOK, fmt.Sprint(string(result)))
}

func Echo_Noticeboardcontentcheck(c echo.Context) error {
	resphonenum := c.FormValue("cudb_phonenum")
	query := "SELECT * FROM info WHERE phone_num = " + "'" + resphonenum + "'" + ";"
	result := ContentCheckQuery(db, query)
	return c.HTML(http.StatusOK, fmt.Sprint(string(result)))
}

func Echo_Noticeboardcontents(c echo.Context) error {
	rescontent := c.FormValue("u_contents_title")
	query := "SELECT * FROM noticeboard WHERE title = " + "'" + rescontent + "'" + ";"
	result := FindsQuery(db, query)
	return c.HTML(http.StatusOK, fmt.Sprint(string(result)))
}

func Echo_Noticeboarddelete(c echo.Context) error {
	resno := c.FormValue("num")
	query := "DELETE FROM noticeboard WHERE no =" + "'" + resno + "'" + ";"
	DeleteQuery(db, query)
	return c.HTML(http.StatusOK, fmt.Sprint("delete"))
}

func Echo_Noticeboardfind(c echo.Context) error {
	var query string

	resphonenum := c.FormValue("phone_num")
	ressex := c.FormValue("sex_text")
	resarea := c.FormValue("area_text")
	restime := c.FormValue("time_text")

	if (restime == "") || (ressex == "") {
		query = "SELECT * FROM noticeboard WHERE phone_num = " + "'" + resphonenum + "'" + "ORDER BY no DESC;"
	} else {
		query = "SELECT * FROM noticeboard WHERE area = " + "'" + resarea + "'" + "AND sex = " + "'" + ressex + "'" + "ORDER BY no DESC;"
	}
	jsondata := FindsQuery(db, query)
	return c.HTML(http.StatusOK, fmt.Sprint(string(jsondata)))
}

func Echo_Noticeboardinsert(c echo.Context) error {
	resphonenum := c.FormValue("enroll_phonenum")
	resname := c.FormValue("enroll_name")
	resemail := c.FormValue("enroll_email")
	ressex := c.FormValue("enroll_sex")
	restitle := c.FormValue("enroll_contents_title")
	resmain := c.FormValue("enroll_contents")
	resarea := c.FormValue("enroll_area")
	restime := c.FormValue("enroll_time")
	respartycount := c.FormValue("enroll_partycount")
	//bimage, _ := base64.StdEncoding.DecodeString(resimage)
	//bimage := MysqlRealEscapeString(resimage)

	if resphonenum == "" || resname == "" || resemail == "" || ressex == "" || restitle == "" || resmain == "" || resarea == "" || restime == "" {
		return c.HTML(http.StatusOK, fmt.Sprint("fail"))
	} else {
		insertstring := "INSERT INTO noticeboard(phone_num, name, email, sex, title, content, area, time_t, partycount, partypeople) VALUES (" + "'" + resphonenum + "'" + "," + "'" + resname + "'" + "," + "'" + resemail + "'" + "," + "'" + ressex + "'" + "," + "'" + restitle + "'" + "," + "'" + resmain + "'" + "," + "'" + resarea + "'" + "," + "'" + restime + "'" + "," + "'" + respartycount + "'" + "," + "'" + "people : " + "'" + ");"
		InsertQuery(db, insertstring)
		selectmequery := "SELECT no FROM noticeboard WHERE phone_num=" + "'" + resphonenum + "'" + ";"
		resno := SelectQuery(db, selectmequery, "area")
		insertmequery := "INSERT INTO party (no, partypeople) VALUE (" + "'" + resno + "'" + "," + "'" + resphonenum + "'" + ");"
		InsertQuery(db, insertmequery)
		return c.HTML(http.StatusOK, fmt.Sprint(resno))
		//http.Redirect(w, r, "/send_alarm/", http.StatusFound)
	}
}

func Echo_Partification(c echo.Context) error {
	if c.Request().Method == "POST" {
		resno := c.FormValue("no")
		resphone := c.FormValue("phone_num")
		resuserphone := c.FormValue("user_phone")

		selquery := "SELECT phone_num FROM noticeboard WHERE no=" + "'" + resno + "'" + ";"
		selphone := SelectQuery(db, selquery, "phone_num")

		if resuserphone == selphone {
			return c.HTML(http.StatusOK, fmt.Sprint("????????? ???????????? ????????? ??? ????????????."))
		} else if resphone == "" {
			return c.HTML(http.StatusOK, fmt.Sprint("????????? ????????? ????????????.."))
		} else if resuserphone == "" {
			return c.HTML(http.StatusOK, fmt.Sprint("???????????? ????????? ????????????."))
		} else {
			partyquery := "SELECT partycount FROM noticeboard WHERE no=" + "'" + resno + "'" + ";"
			partycount := SelectQuery(db, partyquery, "party")
			partycount = partycount[0:1]
			ipartycount, _ := strconv.Atoi(partycount)

			partypeoplequery1 := "SELECT count(partypeople) FROM party WHERE no=" + "'" + resno + "'" + ";"
			partypeoplecount := CountSelectQuery(db, partypeoplequery1, "partypeople")
			if ipartycount <= partypeoplecount {
				return c.HTML(http.StatusOK, fmt.Sprint("?????? ????????? ????????????.."))
			} else {
				partypeoplequery2 := "SELECT partypeople FROM party WHERE no=" + "'" + resno + "'" + ";"
				partypeople := SelectQuery(db, partypeoplequery2, "partypeople")

				if strings.Contains(partypeople, resuserphone) {
					return c.HTML(http.StatusOK, fmt.Sprint("?????? ???????????? ????????????."))
				} else if ipartycount <= 0 {
					return c.HTML(http.StatusOK, fmt.Sprint("?????? ????????? ????????????.."))
				} else {
					//spartycount := strconv.Itoa(ipartycount - 1)
					//partyinsertquery := "UPDATE noticeboard SET partycount=" + "'" + spartycount + "'" + "WHERE no=" + "'" + resno + "'" + ";"
					//UpdateQuery(db, partyinsertquery)

					partypeoplequery3 := "INSERT INTO party (no, partypeople) VALUE (" + "'" + resno + "'" + "," + "'" + resuserphone + "'" + ");"
					InsertQuery(db, partypeoplequery3)

					partycountcheckquery := "SELECT partycount FROM noticeboard WHERE no=" + "'" + resno + "'" + ";"
					partycountcheck := SelectQuery(db, partycountcheckquery, "partycount")
					partycountcheck = partycountcheck[0:1]
					if partycount == partycountcheck {
						Echo_Partysendpushalarm(c, resno)
					}

					return c.HTML(http.StatusOK, fmt.Sprint("????????? ?????? ???????????????."))
				}
			}
		}
	} else {
		return c.HTML(http.StatusOK, fmt.Sprint("???????????? ???????????? ???????????????."))
	}
}

/*
 ????????? ????????? ????????? ??????
*/
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

func NoticeboardContentCheck(w http.ResponseWriter, r *http.Request) {
	r.ParseForm()

	if r.Method == "POST" {
		resphonenum := r.FormValue("cudb_phonenum")

		query := "SELECT * FROM info WHERE phone_num = " + "'" + resphonenum + "'" + ";"

		result := ContentCheckQuery(db, query)

		fmt.Fprintf(w, string(result))
	}
}

/*
 ????????? ????????? ??????
*/
func NoticeboardContents(w http.ResponseWriter, r *http.Request) {
	r.ParseForm()

	//go lang json ??????(marshal) ??????
	if r.Method == "POST" {
		rescontent := r.FormValue("u_contents_title")

		query := "SELECT * FROM noticeboard WHERE title = " + "'" + rescontent + "'" + ";"

		result := FindsQuery(db, query)

		fmt.Fprintf(w, string(result))
	} else {
		//???????????? ??????. ????????? ?????? ???????????? POST??? ??????????????? ?????? ???????????? ??????
		rescontent := "test title"

		query := "SELECT * FROM noticeboard WHERE title = " + "'" + rescontent + "'" + ";"

		result := FindsQuery(db, query)

		fmt.Fprintf(w, string(result))
	}
}

/*
 ????????? ?????? ??????
*/
func NoticeboardDelete(w http.ResponseWriter, r *http.Request) {
	r.ParseForm()

	if r.Method == "POST" {
		resno := r.FormValue("num")

		query := "DELETE FROM noticeboard WHERE no =" + "'" + resno + "'" + ";"

		DeleteQuery(db, query)
		fmt.Fprintf(w, "delete")
	}
}

/*
 ???????????? ??????
 ????????? ????????? ?????? ????????? ???????????? ???????????? ???????????? ?????? ???????????? ????????????
 ???????????? ??? ?????? ??????, ??????, ????????? ??????
*/
func NoticeboardFind(w http.ResponseWriter, r *http.Request) {
	r.ParseForm()

	var query string

	//go lang json ??????(marshal) ??????
	if r.Method == "POST" {
		resphonenum := r.FormValue("phone_num")
		ressex := r.FormValue("sex_text")
		resarea := r.FormValue("area_text")
		restime := r.FormValue("time_text")

		if (restime == "") || (ressex == "") {
			query = "SELECT * FROM noticeboard WHERE phone_num = " + "'" + resphonenum + "'" + "ORDER BY no DESC;"
		} else {
			query = "SELECT * FROM noticeboard WHERE area = " + "'" + resarea + "'" + "AND sex = " + "'" + ressex + "'" + "ORDER BY no DESC;"
		}

		jsondata := FindsQuery(db, query)
		fmt.Fprintf(w, string(jsondata))
	} else {
		//???????????? ??????. ????????? ?????? ???????????? POST??? ??????????????? ?????? ???????????? ??????
		query = "SELECT * FROM noticeboard WHERE phone_num=1234;"

		jsondata := FindsQuery(db, query)

		fmt.Fprintf(w, string(jsondata))
	}
}

/*
 ????????? ????????? ??????
*/
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
		respartycount := r.FormValue("enroll_partycount")
		/*
			resimg := r.FormValue("enroll_image")
			bimg, _ := base64.StdEncoding.DecodeString(resimg)
			resimg = string(bimg)
		*/

		if resphonenum == "" || resname == "" || resemail == "" || ressex == "" || restitle == "" || resmain == "" || resarea == "" || restime == "" {
			fmt.Fprintf(w, "fail")
		} else {
			insertstring = "INSERT INTO noticeboard(phone_num, name, email, sex, title, content, area, time_t, partycount, partypeople) VALUES (" + "'" + resphonenum + "'" + "," + "'" + resname + "'" + "," + "'" + resemail + "'" + "," + "'" + ressex + "'" + "," + "'" + restitle + "'" + "," + "'" + resmain + "'" + "," + "'" + resarea + "'" + "," + "'" + restime + "'" + "," + "'" + respartycount + "'" + "," + "'" + "people : " + "'" + ");"
			InsertQuery(db, insertstring)
			fmt.Fprintf(w, "complete")
			//http.Redirect(w, r, "/send_alarm/", http.StatusFound)
		}
	} else {
		fmt.Fprintf(w, "fail")
	}
}

func MysqlRealEscapeString(value string) string {
	replace := map[string]string{"\\": "\\\\", "'": `\'`, "\\0": "\\\\0", "\n": "\\n", "\r": "\\r", `"`: `\"`, "\x1a": "\\Z"}

	for b, a := range replace {
		value = strings.Replace(value, b, a, -1)
	}

	return value
}

func Partification(w http.ResponseWriter, r *http.Request) {
	r.ParseForm()
	if r.Method == "POST" {
		resno := r.FormValue("no")
		resphone := r.FormValue("phone_num")
		resuserphone := r.FormValue("user_phone")

		selquery := "SELECT phone_num FROM noticeboard WHERE no=" + "'" + resno + "'" + ";"
		selphone := SelectQuery(db, selquery, "phone_num")

		if resuserphone == selphone {
			fmt.Fprintf(w, "????????? ???????????? ????????? ??? ????????????.")
		} else if resphone == "" {
			fmt.Fprintf(w, "????????? ????????? ????????????.")
		} else if resphone == "" {
			fmt.Fprintf(w, "???????????? ????????? ????????????.")
		} else {
			partyquery := "SELECT partycount FROM noticeboard WHERE no=" + "'" + resno + "'" + ";"
			partycount := SelectQuery(db, partyquery, "party")
			partycount = partycount[0:1]
			ipartycount, _ := strconv.Atoi(partycount)

			partypeoplequery1 := "SELECT count(partypeople) FROM party WHERE no=" + "'" + resno + "'" + ";"
			partypeoplecount := CountSelectQuery(db, partypeoplequery1, "partypeople")
			if ipartycount <= partypeoplecount {
				fmt.Fprintf(w, "?????? ????????? ????????????..")
			} else {
				partypeoplequery2 := "SELECT partypeople FROM party WHERE no=" + "'" + resno + "'" + ";"
				partypeople := SelectQuery(db, partypeoplequery2, "partypeople")

				if strings.Contains(partypeople, resuserphone) {
					fmt.Fprintf(w, "?????? ???????????? ????????????.")
				} else if strings.Contains(partypeople, resphone) {
					fmt.Fprintf(w, "????????? ???????????? ????????? ??? ????????????.")
				} else if ipartycount <= 0 {
					fmt.Fprintf(w, "?????? ????????? ????????????..")
				} else {
					//spartycount := strconv.Itoa(ipartycount - 1)
					//partyinsertquery := "UPDATE noticeboard SET partycount=" + "'" + spartycount + "'" + "WHERE no=" + "'" + resno + "'" + ";"
					//UpdateQuery(db, partyinsertquery)

					partypeoplequery3 := "INSERT INTO party (no, partypeople) VALUE (" + "'" + resno + "'" + "," + "'" + resuserphone + "'" + ");"
					InsertQuery(db, partypeoplequery3)

					fmt.Fprintf(w, "????????? ?????? ???????????????.")
				}
			}
		}
	}
}
