package main

import (
	"Makers/capstone_DB_GO/backend"
	"html/template"
	"io"
	"net/http"

	_ "github.com/go-sql-driver/mysql"
	"github.com/labstack/echo"
	"github.com/labstack/echo/middleware"
)

type Template struct {
	templates *template.Template
}

func (t *Template) Render(w io.Writer, name string, data interface{}, c echo.Context) error {
	return t.templates.ExecuteTemplate(w, name, data)
}

func main() {
	fs := http.FileServer(http.Dir("./frontend/static"))
	http.Handle("/static/", http.StripPrefix("/static/", fs))

	t := &Template{
		templates: template.Must(template.ParseGlob("./frontend/*.html")),
	}

	e := echo.New()
	e.Use(middleware.Logger())
	e.Use(middleware.Recover())

	e.Static("/static/", "public")
	e.Renderer = t

	e.GET("/static/*", echo.WrapHandler(http.StripPrefix("/static/", fs)))
	e.POST("/static/*", echo.WrapHandler(http.StripPrefix("/static/", fs)))

	e.GET("/", backend.Echo_Index)

	e.POST("/login/", backend.Echo_Login)
	e.POST("/signup/", backend.Echo_Signup)

	e.POST("/area_setting/", backend.Echo_Areasetting)
	e.POST("/area_info/", backend.Echo_Areainfo)

	e.POST("/noticeboard_check/", backend.Echo_Noticeboardcheck)
	e.POST("/noticeboard_find/", backend.Echo_Noticeboardfind)
	e.POST("/noticeboard_insert/", backend.Echo_Noticeboardinsert)
	e.POST("/noticeboard_delete/", backend.Echo_Noticeboarddelete)
	e.POST("/noticeboard_contents/", backend.Echo_Noticeboardcontents)
	e.POST("/noticeboard_content_check/", backend.Echo_Noticeboardcontentcheck)

	e.POST("/tourpass_check/", backend.Echo_Tourpasscheck)
	e.POST("/tourpass_setting/", backend.Echo_Tourpasssetting)

	e.POST("/send_alarm/", backend.Echo_Sendpushalarm)
	e.POST("/token_insert/", backend.Echo_Tokeninsert)
	e.POST("/find_pw/", backend.Echo_FindPW)
	e.POST("/partification/", backend.Echo_Partification)

	err := e.Start(":9091")
	if err != nil {
		e.Logger.Fatal(err)
	}
	/*

		http.HandleFunc("/", backend.Index)
		http.HandleFunc("/login/", backend.Login)
		http.HandleFunc("/signup/", backend.SingUp)
		http.HandleFunc("/area_setting/", backend.AreaSetting)
		http.HandleFunc("/area_info/", backend.AreaInfo)
		http.HandleFunc("/noticeboard_check/", backend.NoticeboardCheck)
		http.HandleFunc("/noticeboard_find/", backend.NoticeboardFind)
		http.HandleFunc("/noticeboard_insert/", backend.NoticeboardInsert)
		http.HandleFunc("/noticeboard_delete/", backend.NoticeboardDelete)
		http.HandleFunc("/noticeboard_contents/", backend.NoticeboardContents)
		http.HandleFunc("/noticeboard_content_check/", backend.NoticeboardContentCheck)
		http.HandleFunc("/tourpass_check/", backend.TourpassCheck)
		http.HandleFunc("/tourpass_setting/", backend.TourpassSetting)
		http.HandleFunc("/send_alarm/", backend.SendPushAlarm)
		http.HandleFunc("/token_insert/", backend.TokenInsert)
		http.HandleFunc("/find_pw/", backend.FindPW)
		http.HandleFunc("/partification/", backend.Partification)

		log.Println("Listening on : 9091...")
		err := http.ListenAndServe(":9091", nil)
		if err != nil {
			log.Fatal("ListenAndServer : ", err)
		} else {
			fmt.Println("ListenAndServer Started! -> Port(9000)")
		}
	*/

}
