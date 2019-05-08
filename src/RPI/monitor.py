# -*- coding:utf-8 -*-
from tkinter import *
from functools import partial
from PIL import Image, ImageTk
import cv2
import pymysql

user_id = ""
user_pw = ""
camIP = ""

# 창 닫기
def close(window):
	window.destroy()

# 로그인
def loginCheck(login, id, pw):
	global user_id
	global user_pw
	global camIP

	user_id = id.get()
	user_pw = pw.get()


	conn = pymysql.connect(host='52.78.219.61', port=3306, user='monitor', passwd="Kookmin1!", db='db', charset='utf8')
	curs = conn.cursor()

	sql = "select ip from Login where id = %s and pw = %s"
	curs.execute(sql, (user_id, user_pw))

	rows = curs.fetchall()
	# 등록된 경우 camIP 저장 후 로그인 창 닫기
	if len(rows) == 1:
		camIP = rows[0][0]
		close(Login)

# 스트리밍 창
def Streaming(window):
	global camIP

	# 스트리밍
	def stream():
        	_,frame = camera.read()
        	cv2image = cv2.cvtColor(frame, cv2.COLOR_BGR2RGBA)
        	img = Image.fromarray(cv2image)
        	imgtk = ImageTk.PhotoImage(image=img)
        	screen.imgtk = imgtk
        	screen.configure(image=imgtk)
        	screen.after(1, stream)

	streaming=Toplevel(window)
	streaming.title("streaming")
	streaming.geometry("800x480+0+0")
	#streaming.attributes('-fullscreen', True)
	streaming.resizable(False,False)

	# screen, camera 설정
	video = Frame(streaming, width = 640, height = 480)
	video.grid()
	screen = Label(video)
	screen.grid()
	camera = cv2.VideoCapture('http://'+camIP+':8080/stream/video.mjpeg')
	video.pack(side="left")

	buttonList = Frame(streaming, width = 160, height = 480)
	buttonList.pack()

	# bt_call: 전화 걸기, bt_hangup: 전화 끊기, bt_streaming_close: 창닫기
	bt_call = Button(buttonList, text="call", width = 160, height = 11)
	bt_call.pack(expand=1)
	bt_hangup = Button(buttonList, text="hang up", width=160, height = 11)
	bt_hangup.pack(expand=1)
	bt_streaming_close = Button(buttonList, text="close", width=160, height = 11, command=partial(close,streaming))
	bt_streaming_close.pack(expand=1)

	stream()

# 방문기록 창
def History(window):
	window.destroy()

# main 창 생성 및 설정
# size: 800x480, size 변경 불가, fullscreen
main=Tk()
main.title("main")
main.geometry("800x480+0+0")
#main.attributes('-fullscreen', True)
main.resizable(False, False)
main.lower()

# Login 창 생성 및 설정
# size: 800x480, size 변경 불가, fullscreen
Login = Toplevel(main)
Login.title("Login")
Login.geometry("800x480+0+0")
#Login.attributes('-fullscreen', True)
Login.resizable(False, False)
Login.lift()

# main 창 Frame
mainframe = Frame(main, width = 800, height = 480)
mainframe.pack()

# main 창 버튼: 스트리밍, 방문기록, 종료
bt_streaming = Button(mainframe, text="Streaming", width = 800, height = 11, command=partial(Streaming, main))
bt_streaming.pack(expand=1)
bt_history = Button(mainframe, text="History", width = 800, height = 11, command=partial(History, main))
bt_history.pack(expan=1)
bt_close = Button(mainframe, text="close", width = 800, height=11,bg="red", command= partial(close,main))
bt_close.pack(expand=1)

# Login Frame
login = Frame(Login, width = 640, height = 480, bg="black")
login.pack(side="left")

# ID, PW 입력Entry
Label(login, text="ID").grid(row=0)
Label(login, text="PW").grid(row=1)
id = Entry(login)
pw = Entry(login)
id.grid(row=0, column=1)
pw.grid(row=1, column=1)

# Login 창 메뉴Frame
menu = Frame(Login, width = 160, height = 480)
menu.pack()

# Login 창 버튼: 로그인, 종료
bt_login = Button(menu, text="Login", width = 160, height=17, bg="green", command=partial(loginCheck, Login, id, pw))
bt_login.pack(expand=1)
bt_login_close = Button(menu, text="Close", width = 160, height=16, bg="red", command=partial(close, main))
bt_login_close.pack(expand=1)

main.mainloop()