package backend

import (
	"database/sql"
	"encoding/json"
	"fmt"
	"log"
)

var db = dbInfo{"root", "1463", "localhost:3306", "mysql", "makers"}

type dbInfo struct {
	user     string
	pwd      string
	url      string
	engine   string
	database string
}

type Noticeboard struct {
	No          string `json:"no"`
	Phone_num   string `json:"phone_num"`
	Name        string `json:"name"`
	Email       string `json:"email"`
	Sex         string `json:"sex"`
	Title       string `json:"title"`
	Content     string `json:"content"`
	Area        string `json:"area"`
	Time_t      string `json:"time_t"`
	Partycount  string `json:"partycount"`
	PartyPeople string `json:"partypeople"`
	Partyno     string `json:"partyno"`
}

type Noticeboards struct {
	Result []Noticeboard `json:"results"`
}

type Noticeboard_check struct {
	Phone_num string `json:"phone_num"`
	Area      string `json:"area"`
	Time_t    string `json:"time_t"`
	Sex       string `json:"sex"`
	Authority string `json:"authority"`
}

type Noticeboard_checks struct {
	Result []Noticeboard_check `json:"results"`
}

type Tourpass_struct struct {
	Tourpass string `json:"tourpass"`
}

type Tourpass_structs struct {
	Result []Tourpass_struct `json:"results"`
}

type ContentCheck struct {
	Name      string `json:"name"`
	Phone_num string `json:"phone_num"`
	Pass      string `json:"pass"`
	Address   string `json:"address"`
	Email     string `json:"email"`
	Sex       string `json:"sex"`
	Tourpass  string `json:"tourpass"`
	Topic     string `json:"topic"`
}

type ContentChecks struct {
	Result []ContentCheck `json:"results"`
}

type Area struct {
	Title      string `json:"title"`
	Content    string `json:"content"`
	Image      string `json:"image"`
	Preference string `json:"preference"`
}

type Areas struct {
	Result []Area `json:"results"`
}

func SelectQuery(db dbInfo, query string, choose string) string {
	var area, phone_num, login, signup, tourpass, pass, result, party, partycount, partypeople string

	dataSource := db.user + ":" + db.pwd + "@tcp(" + db.url + ")/" + db.database
	conn, err := sql.Open(db.engine, dataSource)
	if err != nil {
		log.Fatal(err)
	}
	rows, err := conn.Query(query)

	defer rows.Close()

	for rows.Next() {
		switch choose {
		case "area":
			err := rows.Scan(&area)
			if err != nil {
				log.Fatal(err)
			}
			result = area
		case "phone_num":
			err := rows.Scan(&phone_num)
			if err != nil {
				log.Fatal(err)
			}
			result = phone_num
		case "login":
			err := rows.Scan(&login)
			if err != nil {
				log.Fatal(err)
			}
			result = login
		case "signup":
			err := rows.Scan(&signup)
			if err != nil {
				log.Fatal(err)
			}
			result = signup
		case "tourpass":
			err := rows.Scan(&tourpass)
			if err != nil {
				log.Fatal(err)
			}
			result = tourpass
		case "findpw":
			err := rows.Scan(&pass)
			if err != nil {
				log.Fatal(err)
			}
			result = pass
		case "party":
			err := rows.Scan(&party)
			if err != nil {
				log.Fatal(err)
			}
			result = party
		case "partycount":
			err := rows.Scan(&partycount)
			if err != nil {
				log.Fatal(err)
			}
			result = partycount
		case "partypeople":
			err := rows.Scan(&partypeople)
			if err != nil {
				log.Fatal(err)
			}
			result = partypeople
		}
	}
	return result
}

func CountSelectQuery(db dbInfo, query string, choose string) int {
	var partypeoplecount, result int

	dataSource := db.user + ":" + db.pwd + "@tcp(" + db.url + ")/" + db.database
	conn, err := sql.Open(db.engine, dataSource)
	if err != nil {
		log.Fatal(err)
	}
	rows, err := conn.Query(query)

	defer rows.Close()

	for rows.Next() {
		switch choose {
		case "partypeoplecount":
			err := rows.Scan(&partypeoplecount)
			if err != nil {
				log.Fatal(err)
			}
			result = partypeoplecount
		}
	}
	return result
}

func InsertQuery(db dbInfo, query string) {
	dataSource := db.user + ":" + db.pwd + "@tcp(" + db.url + ")/" + db.database
	conn, err := sql.Open(db.engine, dataSource)
	if err != nil {
		log.Fatal(err)
	}

	result, err := conn.Exec(query)

	if err != nil {
		log.Fatal(err)
	}
	nRow, err := result.RowsAffected()
	fmt.Println("insert count : ", nRow)
	conn.Close()
}

func DeleteQuery(db dbInfo, query string) {
	dataSource := db.user + ":" + db.pwd + "@tcp(" + db.url + ")/" + db.database
	conn, err := sql.Open(db.engine, dataSource)
	if err != nil {
		log.Fatal(err)
	}
	result, err := conn.Exec(query)

	if err != nil {
		log.Fatal(err)
	}
	nRow, err := result.RowsAffected()
	fmt.Println("delete count : ", nRow)
	conn.Close()
}

func UpdateQuery(db dbInfo, query string) {
	dataSource := db.user + ":" + db.pwd + "@tcp(" + db.url + ")/" + db.database
	conn, err := sql.Open(db.engine, dataSource)
	if err != nil {
		log.Fatal(err)
	}
	result, err := conn.Exec(query)

	if err != nil {
		log.Fatal(err)
	}
	nRow, err := result.RowsAffected()
	fmt.Println("update count : ", nRow)
	conn.Close()
}

func FindsQuery(db dbInfo, query string) []byte {
	var no, phone_num, name, email, sex, title, content, area, time_t, partycount, partypeople string

	//Noticeboard ????????? ?????? ??????
	var n []Noticeboard

	dataSource := db.user + ":" + db.pwd + "@tcp(" + db.url + ")/" + db.database
	conn, err := sql.Open(db.engine, dataSource)
	if err != nil {
		log.Fatal(err)
	}
	rows, err := conn.Query(query)

	defer rows.Close()

	for rows.Next() {
		err := rows.Scan(&no, &phone_num, &name, &email, &sex, &title, &content, &area, &time_t, &partycount, &partypeople)

		if err != nil {
			log.Fatal(err)
		} else {
			var structdata Noticeboard
			var partyno string
			partyquery := "SELECT count(partypeople) FROM party WHERE no =" + "'" + no + "'" + ";"
			rows2, _ := conn.Query(partyquery)
			defer rows2.Close()
			for rows2.Next() {
				err2 := rows2.Scan(&partyno)

				if err2 != nil {
					log.Fatal(err2)
				} else {
					structdata = Noticeboard{no, phone_num, name, email, sex, title, content, area, time_t, partycount, partypeople, partyno}
				}
			}
			//structdata = Noticeboard{no, phone_num, name, email, sex, title, content, area, time_t, partycount, partypeople, partyno}
			n = append(n, structdata)
		}
	}

	//Noticeboards??? ????????? ??????
	result := Noticeboards{n}
	/*
		result ????????? Noticeboard ????????? ????????? []byte ????????? ????????? ?????????
		????????? ???????????? ?????? json?????? ???????????? []byte ???????????? ??????
	*/

	returnresult, _ := json.Marshal(result)

	//????????? ??? ????????? ??????
	return returnresult
}

func CheckQuery(db dbInfo, query string) []byte {
	var phone_num, area, time_t, sex, authority string

	//Noticeboard_check ????????? ?????? ??????
	var n []Noticeboard_check

	dataSource := db.user + ":" + db.pwd + "@tcp(" + db.url + ")/" + db.database
	conn, err := sql.Open(db.engine, dataSource)
	if err != nil {
		log.Fatal(err)
	}
	rows, err := conn.Query(query)

	defer rows.Close()

	for rows.Next() {
		err := rows.Scan(&phone_num, &area, &time_t, &sex, &authority)
		if err != nil {
			log.Fatal(err)
		} else {
			structdata := Noticeboard_check{phone_num, area, time_t, sex, authority}

			n = append(n, structdata)
		}
	}

	//Noticeboard_checks??? ????????? ??????
	result := Noticeboard_checks{n}
	/*
		result ????????? Noticeboard_check ????????? ????????? []byte ????????? ????????? ?????????
		????????? ???????????? ?????? json?????? ???????????? []byte ???????????? ??????
	*/
	returnresult, _ := json.Marshal(result)

	//????????? ??? ????????? ??????
	return returnresult
}

func TourpassQuery(db dbInfo, query string) []byte {
	var tourpass string

	//Noticeboard_check ????????? ?????? ??????
	var n []Tourpass_struct

	dataSource := db.user + ":" + db.pwd + "@tcp(" + db.url + ")/" + db.database
	conn, err := sql.Open(db.engine, dataSource)
	if err != nil {
		log.Fatal(err)
	}
	rows, err := conn.Query(query)

	defer rows.Close()

	for rows.Next() {
		err := rows.Scan(&tourpass)
		if err != nil {
			log.Fatal(err)
		} else {
			structdata := Tourpass_struct{tourpass}

			n = append(n, structdata)
		}
	}

	//Noticeboard_checks??? ????????? ??????
	result := Tourpass_structs{n}
	/*
		result ????????? Noticeboard_check ????????? ????????? []byte ????????? ????????? ?????????
		????????? ???????????? ?????? json?????? ???????????? []byte ???????????? ??????
	*/
	returnresult, _ := json.Marshal(result)

	//????????? ??? ????????? ??????
	return returnresult
}

func ContentCheckQuery(db dbInfo, query string) []byte {
	var name, phone_num, pass, address, email, sex, tourpass, topic string

	//Contentcheck ????????? ?????? ??????
	var n []ContentCheck

	dataSource := db.user + ":" + db.pwd + "@tcp(" + db.url + ")/" + db.database
	conn, err := sql.Open(db.engine, dataSource)
	if err != nil {
		log.Fatal(err)
	}
	rows, err := conn.Query(query)

	defer rows.Close()

	for rows.Next() {
		err := rows.Scan(&name, &phone_num, &pass, &address, &email, &sex, &tourpass, &topic)
		if err != nil {
			log.Fatal(err)
		} else {
			structdata := ContentCheck{name, phone_num, pass, address, email, sex, tourpass, topic}

			n = append(n, structdata)
		}
	}

	//ContentChecks??? ????????? ??????
	result := ContentChecks{n}
	/*
		result ????????? Contentcheck ????????? ????????? []byte ????????? ????????? ?????????
		????????? ???????????? ?????? json?????? ???????????? []byte ???????????? ??????
	*/
	returnresult, _ := json.Marshal(result)

	//????????? ??? ????????? ??????
	return returnresult
}

/*
	area ????????? ?????? ??? ?????? ?????? ??? ??????
	????????? ???????????? results??? ????????? ?????? ???
*/
func AreaQuery(db dbInfo, query string) []byte {
	var title, content, image, preference, slicepref string

	//Contentcheck ????????? ?????? ??????
	var n []Area

	dataSource := db.user + ":" + db.pwd + "@tcp(" + db.url + ")/" + db.database
	conn, err := sql.Open(db.engine, dataSource)
	if err != nil {
		log.Fatal(err)
	}
	rows, err := conn.Query(query)

	defer rows.Close()

	for rows.Next() {
		err := rows.Scan(&title, &content, &image, &preference)
		if err != nil {
			log.Fatal(err)
		} else {
			fmt.Println(preference[1:2])
			if preference[1:2] == "." {
				slicepref = preference[:1]
			} else if preference[2:3] == "." {
				slicepref = preference[:2]
			} else {
				slicepref = preference[:3]
			}
			structdata := Area{title, content, image, slicepref}

			n = append(n, structdata)
		}
	}

	//ContentChecks??? ????????? ??????
	result := Areas{n}
	/*
		result ????????? Contentcheck ????????? ????????? []byte ????????? ????????? ?????????
		????????? ???????????? ?????? json?????? ???????????? []byte ???????????? ??????
	*/
	returnresult, _ := json.Marshal(result)

	//????????? ??? ????????? ??????
	return returnresult
}
