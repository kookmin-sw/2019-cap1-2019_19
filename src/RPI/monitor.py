# -*- coding:utf-8 -*-
from tkinter import *
from functools import partial
from PIL import Image, ImageTk
import cv2
import pymysql
import threading
from time import sleep
import socket

user_id = ""
user_pw = ""
camIP = ""
camPrivateIP = ""

# 창 닫기
def close(window):
	window.destroy()
# 로그인
def loginCheck(login, id, pw):
	global user_id, user_pw, camIP, camPrivateIP

	user_id = id.get()
	user_pw = pw.get()


	conn = pymysql.connect(host='####', port=0000, user='####', passwd="####", db='####', charset='utf8')
	curs = conn.cursor()

	sql = "select ip, i_ip from Login where id = %s and pw = %s"
	curs.execute(sql, (user_id, user_pw))

	rows = curs.fetchall()
	# 등록된 경우 camIP 저장 후 로그인 창 닫기
	if len(rows) == 1:
		camIP = rows[0][0]
		camPrivateIP = rows[0][1]
		close(login)
	else:
		t = 'ID또는 PW가 틀렸습니다.'
		Label(login,text=t, fg="red", bg="white", font=("Arial", 20, "bold")).place(x=0, y=25, width = 640, height=25)

# Key 처리
def select(entry,value,upper):
	if value == "BACK":
		entry.delete(len(entry.get())-1,END)
	elif value == "CLEAR":
		entry.delete(0,END)
	else:
		if upper == 1:
			value = value.upper()
		entry.insert(END, value)

# Keyboard 생성
def makeKeyboard(window, entry, buttons, upper):
	baseX = 0
	baseY = 230
	w = 64
	h = 50
	count = 0

	for button in buttons:
		command = lambda x=button: select(entry,x,upper)
		if upper == 1:
			button = button.upper()
		if button == "CLEAR":
			Button(window,text= button,width=12, bg="#FFE08C", fg="black",
				activebackground = "#ffffff", activeforeground="#FFE08C", relief='raised', font=("Arial",12,"bold"),
				bd=1,command=command).place(x = baseX+count*w, y = baseY, width = w*3, height = h)
		elif button == "SHIFT":
			Button(window,text= button,width=4, bg="#FFE08C", fg="black",
				activebackground = "#ffffff", activeforeground="#FFE08C", relief='raised', font=("Arial",12,"bold"),
				bd=1,command=partial(makeKeyboard,window, entry, buttons, (upper+1)%2)).place(x = baseX+count*w, y = baseY, width = w, height = h)
		elif button == "BACK":
			Button(window,text= button,width=12, bg="#FFE08C", fg="black",
				activebackground = "#ffffff", activeforeground="#FFE08C", relief='raised', font=("Arial",12,"bold"),
				bd=1,command=command).place(x = baseX+count*w, y = baseY, width = w, height = h)
		else:
			Button(window,text= button,width=4, bg="#FFB2F5", fg="black",
				activebackground = "#ffffff", activeforeground="#FFB2F5", relief='raised', font=("Arial",12,"bold"),
				bd=1,command=command).place(x = baseX+count*w, y = baseY, width = w, height = h)

		count +=1

		if count > 9 and baseY == 230:
			count = 0
			baseY += h
		if count > 9 and baseY == 280:
			count = 0
			baseY += h
		if count > 9 and baseY == 330:
			count = 0
			baseY += h
		if count > 9 and baseY == 380:
			count = 0
			baseY += h
# 로그인 창
def LoginWindow(window):
	# Login 창 생성 및 설정
	# size: 800x480, size 변경 불가, fullscreen
	Login = Toplevel(window)
	Login.title("Login")
	Login.geometry("800x480+0+0")
	Login.attributes('-fullscreen', True)
	Login.resizable(False, False)
	Login.lift()
	# esc 누를 시 종료(임시)
	Login.bind("<Escape>",lambda e: window.destroy())

	# Login Frame
	login = Frame(Login, width = 640, height = 480, bg="white")
	login.pack(side="left")

	# ID, PW 입력Entry
	Label(login, text="ID", bg="white").place(x=0, y=75, width = 50, height = 25)
	Label(login, text="PW", bg="white").place(x=0, y=125, width = 50, height = 25)
	id = Entry(login)
	pw = Entry(login)
	id.place(x=50, y = 75, width = 540, height = 25)
	pw.place(x=50, y = 125, width = 540, height = 25)

	# onScreenKeyboard
	buttons=[
	'~','!','@','#','$','%','^','&','*','BACK',
	'1','2','3','4','5','6','7','8','9','0',
	'q','w','e','r','t','y','u','i','o','p',
	'a','s','d','f','g','h','j','k','l','SHIFT',
	'z','x','c','v','b','n','m','CLEAR']

	makeKeyboard(login, id, buttons,0)
	id.bind("<Button-1>",lambda e: makeKeyboard(login, id, buttons,0))
	pw.bind("<Button-1>",lambda e: makeKeyboard(login, pw, buttons,0))

	# Login 창 메뉴Frame
	menu = Frame(Login, width = 160, height = 480)
	menu.pack()

	# Login 창 버튼: 로그인, 종료
	bt_login = Button(menu, text="Login", bg="green", fg="white", activebackground = "white", activeforeground="green", font=("Arial",15,"bold"), command=partial(loginCheck, Login, id, pw)).place(x=0, y=0, width = 160, height = 480)


# 통화 상태
state = "calloff"
# 오디오 송신
def send(s):
	global state

	s_m = "hi from monitor"
	e_m = "bye from moniotr"
	print("send 시작")
	while True:
		if state == "calloff" or state == "programoff":
			s.send(e_m.encode('utf-8'))
			sleep(1)
			break
		s.send(s_m.encode('utf-8'))
		sleep(1)
	print("send 종료")

# 오디오 수신
def receive(s):
	global state
	while True:
		if state == "programoff":
			break
		data = s.recv(65536)
		data = data.decode("utf-8")
		print(data)
		if data == "bye from rpi":
			print("receive end")
			break

# 통화 시작
def call(s):
	global state
	if state == "calloff":
		state = "callon"
		SendMessage(s,"call")
		p1 = threading.Thread(target=send, args=(s,))
		p1.start()

# 통화 종료
def hangup():
	global state
	if state == "callon":
		state = "calloff"
		print("hangup")

# message 전송
def SendMessage(s,message):
	s.send(message.encode('utf-8'))

# 스트리밍 창 종료
def closeStreaming(s,streaming):
	global state
	state = "programoff"
	SendMessage(s,'close')
	sleep(2)
	s.close()
	streaming.destroy()
	print("close")

# 스트리밍 창
# 스트리밍 영상 못가져오는경우 예외처리 필요함
def StreamingWindow(window):
	global camIP, camPrivateIP, state
	state = "calloff"

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
	sleep(1)
	stream()

	# 외부 인터폰과 통신
	HOST = camPrivateIP
	PORT = 0000

	s = socket.socket(socket.AF_INET,socket.SOCK_STREAM)
	s.connect((HOST,PORT))
	SendMessage(s,"connect")
	p = threading.Thread(target=receive, args=(s,)).start()

	# button 생성
	buttonList = Frame(streaming, width = 160, height = 480)
	buttonList.pack()

	# bt_call: 전화 걸기, bt_hangup: 전화 끊기, bt_streaming_close: 창닫기
	bt_call = Button(buttonList, text="call", command = partial(call,s)).place(x=0, y=0, width = 160, height = 160)
	bt_hangup = Button(buttonList, text="hang up", command = hangup).place(x=0, y= 160, width = 160, height = 160)
	bt_streaming_close = Button(buttonList, text="close", command=partial(closeStreaming,s,streaming)).place(x=0, y=320, width = 160, height = 160)

# 방문기록 창
def HistoryWindow(window):
	window.destroy()

# main 창 생성 및 설정
# size: 800x480, size 변경 불가, fullscreen
main=Tk()
main.title("main")
main.geometry("800x480+0+0")
#main.attributes('-fullscreen', True)
main.resizable(False, False)
main.lower()
# esc 누를 시 종료(임시)
main.bind("<Escape>",lambda e: main.destroy())

# main 창 Frame
mainframe = Frame(main, width = 800, height = 480)
mainframe.pack()

# main 창 버튼: 스트리밍, 방문기록, 종료(제거할 예정)
bt_streaming = Button(mainframe, text="Streaming",bg="#FFE08C", font=("Arial",20,"bold"), activebackground = "#FFE08C", command=partial(StreamingWindow, main)).place(x=0, y=0, width = 800, height= 240)
bt_history = Button(mainframe, text="History", bg="#E4F7BA", font=("Arial",20,"bold"), activebackground = "#E4F7BA",command=partial(HistoryWindow, main)).place(x=0, y=240, width =800, height =240)
#bt_close = Button(mainframe, text="close",bg="red", activebackground = "red", font=("Arial",20,"bold"), command= partial(close,main)).place(x=0, y=320, width = 800, height = 160)

# 로그인 창 생성
LoginWindow(main)

main.mainloop()
