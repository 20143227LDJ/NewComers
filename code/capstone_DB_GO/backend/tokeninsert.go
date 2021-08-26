package backend

import (
	"fmt"
	"net/http"

	"github.com/labstack/echo"
)

func Echo_Tokeninsert(c echo.Context) error {
	if c.Request().Method == "POST" {
		restoken := c.FormValue("Token")
		query := "INSERT INTO users(Token) VALUES(" + "'" + restoken + "'" + ") ON DUPLICATE KEY UPDATE Token=" + "'" + restoken + "'" + ";"
		InsertQuery(db, query)
		return c.HTML(http.StatusOK, fmt.Sprint("Token Insert"))
	} else {
		return c.HTML(http.StatusOK, fmt.Sprint("Token Insert Fail"))
	}
}

/*
 토큰 값을 삽입
 현재는 안 쓰이는 파일
 애플리케이션 전체에 대한 메시지 전송이 필요한 경우 활성화 하고
 send_alarm.go 파일을 수정하여 지정된 토큰에 대하여 푸시 알람을 활성화 하면 됨
*/
func TokenInsert(w http.ResponseWriter, r *http.Request) {
	r.ParseForm()

	if r.Method == "POST" {
		restoken := r.FormValue("Token")
		query := "INSERT INTO users(Token) VALUES(" + "'" + restoken + "'" + ") ON DUPLICATE KEY UPDATE Token=" + "'" + restoken + "'" + ";"

		InsertQuery(db, query)
	}
}
