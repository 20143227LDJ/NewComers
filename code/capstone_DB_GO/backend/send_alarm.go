package backend

import (
	"context"
	"fmt"
	"log"
	"net/http"

	firebase "firebase.google.com/go"
	"firebase.google.com/go/messaging"
	"github.com/labstack/echo"
	"google.golang.org/api/option"
)

func Echo_Sendpushalarm(c echo.Context) error {
	if c.Request().Method == "POST" {
		var areaselectquery = "SELECT area FROM noticeboard ORDER BY no DESC LIMIT 1"
		areaselect := SelectQuery(db, areaselectquery, "area")
		opt := option.WithCredentialsFile("newcomers-521cb-firebase-adminsdk-ggg4x-09b1c5355c.json")
		app, _ := firebase.NewApp(context.Background(), nil, opt)
		ctx := context.Background()
		client, _ := app.Messaging(ctx)
		sendToTopic(ctx, client, areaselect, "0")
		return c.HTML(http.StatusOK, fmt.Sprint("sending ~ "))
	} else {
		return c.HTML(http.StatusOK, fmt.Sprint("sending fail :( "))
	}
}

func Echo_Partysendpushalarm(c echo.Context, resno string) {
	opt := option.WithCredentialsFile("newcomers-521cb-firebase-adminsdk-ggg4x-09b1c5355c.json")
	app, _ := firebase.NewApp(context.Background(), nil, opt)
	ctx := context.Background()
	client, _ := app.Messaging(ctx)
	sendToTopic(ctx, client, "None", resno)
}

/*
 안드로이드 푸시 알람 메인 함수
*/
func SendPushAlarm(w http.ResponseWriter, r *http.Request) {
	//block the GET type.. and send push alarm to topic
	if r.Method == "POST" {
		//var topicselectquery = "SELECT topic FROM info;"
		var areaselectquery = "SELECT area FROM noticeboard ORDER BY no DESC LIMIT 1"
		areaselect := SelectQuery(db, areaselectquery, "area")
		opt := option.WithCredentialsFile("newcomers-521cb-firebase-adminsdk-ggg4x-09b1c5355c.json")
		app, _ := firebase.NewApp(context.Background(), nil, opt)
		ctx := context.Background()
		client, _ := app.Messaging(ctx)
		sendToTopic(ctx, client, areaselect, "0")
	}
	//http.Redirect(w, r, "/", http.StatusFound)
}

func initializeAppDefault() *firebase.App {
	// [START initialize_app_default_golang]
	app, err := firebase.NewApp(context.Background(), nil)
	if err != nil {
		log.Fatalf("error initializing app: %v\n", err)
	}
	// [END initialize_app_default_golang]

	return app
}

/*
 토픽에 맞게 푸시 알람을 실질적으로 전송하는 부분
*/
func sendToTopic(ctx context.Context, client *messaging.Client, selectedarea string, resno string) {
	// [START send_to_topic_golang]
	// The topic name can be optionally prefixed with "/topics/".
	//topic := selectedtopic
	//topic := "newcomers_fcm"

	var topic string
	var title string
	var body string

	if resno == "0" {
		var areatitle string = " 지역 새 게시글이 등록되었습니다."
		var areabody string = " 지역 게시판을 확인하세요."
		if selectedarea == "부산" {
			topic = "busan"
			title = "부산" + areatitle
			body = "부산" + areabody
		} else if selectedarea == "서울" {
			topic = "seoul"
			title = "서울" + areatitle
			body = "서울" + areabody
		} else if selectedarea == "대구" {
			topic = "daegu"
			title = "대구" + areatitle
			body = "대구" + areabody
		}
	} else if selectedarea == "None" {
		var resnotitle string = " 번 게시글 파티원이 모두 모였습니다."
		var resnobody string = " 번 게시글 파티원을 확인하세요."
		topic = resno
		title = resno + resnotitle
		body = resno + resnobody
	} else {

	}

	// See documentation on defining a message payload.
	messages := []*messaging.Message{
		{
			Notification: &messaging.Notification{
				Title: title,
				Body:  body,
			},
			Data: map[string]string{
				"title": title,
				"body":  body,
			},
			Topic: topic,
		},
	}

	// Send a message to the devices subscribed to the provided topic.
	br, err := client.SendAll(context.Background(), messages)
	if err != nil {
		log.Fatalln(err)
	}
	// Response is a message ID string.
	fmt.Println("Successfully send message:", br.SuccessCount)
	// [END send_to_topic_golang]
}

/*
 프로젝트 ID에 대한 토큰 초기화
*/
func initializeAppWithRefreshToken() *firebase.App {
	// [START initialize_app_refresh_token_golang]
	opt := option.WithCredentialsFile("path/to/refreshToken.json")
	config := &firebase.Config{ProjectID: "running-plus-9c5ab"}
	app, err := firebase.NewApp(context.Background(), config, opt)
	if err != nil {
		log.Fatalf("error initializing app: %v\n", err)
	}
	// [END initialize_app_refresh_token_golang]

	return app
}
