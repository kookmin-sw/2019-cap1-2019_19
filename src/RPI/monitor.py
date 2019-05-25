#-*- coding:utf-8 -*-

from tkinter import *
from functools import partial
from PIL import Image, ImageTk
from time import sleep
import cv2
import pymysql
import threading
import socket
import os

########
# INFO #
########
user_id = ""
user_pw = ""
camIP = ""
camPrivateIP = ""

##########
# SOCKET #
##########
s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)

#########
# STATE #
#########
noti_state = "ON" # 알림 여부
stream_state = "OFF" # 영상 스트리밍 상태
call_state = "OFF" # 음성 통화 상태

#########
# LOGIN #
#########

# 로그인 함수
def loginCheck(login, id, pw):
	global user_id, user_pw, camIP, camPrivateIP, s

	# Entry에서 값 가져오기
	user_id = id.get()
	user_pw = pw.get()

	# DB연결
	conn = pymysql.connect(host="####", port=0000, user="####", passwd="####", db="####", charset="utf8")
	curs = conn.cursor()

	# SELECT문 실행
	sql = "select ip, i_ip from Login where id = %s and pw = %s"
	curs.execute(sql,(user_id, user_pw))

	# 결과
	rows = curs.fetchall()

	if len(rows) == 1:
		camIP = rows[0][0]
		camPrivateIP = rows[0][1]
		# 인터폰에 연결
		HOST = camPrivateIP
		PORT = 0000
		try:
			s.connect((HOST,PORT))
			thread = threading.Thread(target=notification).start()
			print("연결 성공")
		except:
			print("연결 실패")
		login.destroy()
	else:
		# 로그인 실패 표시
		text = "ID또는 PW가 틀렸습니다."
		Lable(login, text=text, fg="red", bg="white", font=("Arial", 20, "bold")).place(x=0, y=25, width=640, height=25)

# Login 창- Keyboard
# button 눌렸을 때 처리
def press(entry, value, upper):
	if value == "BACK":
		entry.delete(len(entry.get())-1, END)
	elif value == "CLEAR":
		entry.delete(0, END)
	else:
		if upper == 1:
			value = value.upper()
		entry.insert(END, value)

# Keyboard 생성
def drawKeyboard(window, entry, buttons, upper):
	baseX = 0
	baseY = 230
	w = 64
	h = 50
	count = 0

	for button in buttons:
		command = lambda x=button: press(entry, x, upper)
		if upper == 1:
			button = button.upper()
		if button == "CLEAR":
			Button(window,text= button,width=12, bg="#FFE08C", fg="black",
				activebackground = "#ffffff", activeforeground="#FFE08C", relief='raised', font=("Arial",12,"bold"),
				bd=1,command=command).place(x = baseX+count*w, y = baseY, width = w*3, height = h)
		elif button == "BACK":
			Button(window,text= button,width=12, bg="#FFE08C", fg="black",
				activebackground = "#ffffff", activeforeground="#FFE08C", relief='raised', font=("Arial",12,"bold"),
				bd=1,command=command).place(x = baseX+count*w, y = baseY, width = w, height = h)
		elif button == "SHIFT":
			Button(window,text= button,width=4, bg="#FFE08C", fg="black",
				activebackground = "#ffffff", activeforeground="#FFE08C", relief='raised', font=("Arial",12,"bold"),
				bd=1,command=partial(drawKeyboard,window, entry, buttons, (upper+1)%2)).place(x = baseX+count*w, y = baseY, width = w, height = h)
		else:
			Button(window,text= button,width=4, bg="#FFB2F5", fg="black",
				activebackground = "#ffffff", activeforeground="#FFB2F5", relief='raised', font=("Arial",12,"bold"),
				bd=1,command=command).place(x = baseX+count*w, y = baseY, width = w, height = h)

		count += 1
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

# Login 창
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
	login = Frame(Login, width=640, height=480, bg="white")
	login.pack(side="left")

	# ID, PW 입력Entry
	Label(login, text="ID", bg="white").place(x=0, y=75, width=50, height=25)
	Label(login, text="PW", bg="white").place(x=0, y=125, width=50, height=25)
	id = Entry(login)
	pw = Entry(login)
	id.place(x=50, y=75, width=540, height=25)
	pw.place(x=50, y=125, width=540, height=25)

	# onScreenKeyboard
	buttons=[
	'~','!','@','#','$','%','^','&','*','BACK',
	'1','2','3','4','5','6','7','8','9','0',
	'q','w','e','r','t','y','u','i','o','p',
	'a','s','d','f','g','h','j','k','l','SHIFT',
	'z','x','c','v','b','n','m','CLEAR']

	drawKeyboard(login, id, buttons, 0)
	id.bind("<Button-1>",lambda e: drawKeyboard(login, id, buttons, 0))
	pw.bind("<Button-1>",lambda e: drawKeyboard(login, pw, buttons, 0))

	# Login 창 메뉴Frame
	menu = Frame(Login, width=160, height=480)
	menu.pack()

	# Login Button
	bt_login = Button(menu, text="Login", bg="green", fg="white", activebackground="white", activeforeground="green", font=("Arial",15,"bold"), command=partial(loginCheck, Login, id, pw)).place(x=0, y=0, width=160, height=480)

#############
# STREAMING #
#############

# 통화 시작
def call():
	global call_state, s

	if call_state == "OFF":
		call_state = "ON"
		message = "call"
		s.send(message.encode("utf-8"))
		call_thread = threading.Thread(target=send).start()

# 통화 종료
def hangup():
	global call_state

	if call_state == "ON":
		call_state = "OFF"
		print("hang up")

# 음성 송신
def send():
	global call_state, s

	e_m = "BYE"
	count = 0
	while True:
		i = str(count%3+1)
		path = "Audio/input_"+i+".mp3"

		print("record start")
		record(i)
		print("record complete")

		audio = open(path, "rb")
		data = audio.read()
		len_data = str(len(data))

		print("send start")
		if call_state == "OFF":
			try:
				len_e_m = str(len(e_m))
				s.send(len_e_m.encode("utf-8"))
				sleep(1)
				s.send(e_m.encode("utf-8"))
				print("call end")
				break
			except:
				break
		try:
			s.send(len_data.encode("utf-8"))
			sleep(1)
			s.sendall(data)
			print("send complete")
		except:
			print("send error")
		audio.close()
		count += 1

# 음성 수신
def receive():
	global noti_state, stream_state, s

	count = 0
	fail = 0
	while True:
		i = str(count%3+1)
		path = "Audio/output_"+i+".mp3"
		if stream_state == "OFF":
			print("stream_state: CLOSE")
			break
		try:
			sleep(1)
			print("#####receive#####")
			if stream_state == "OFF":
				print("stream_state == 'OFF'")
				break
			print(stream_state)
			len_data = s.recv(1023)
			len_data = len_data.decode("utf-8")
			len_data = int(len_data)
		except:
			print("len_data error")
			fail = 1
			break
		size = 0
		result = b""
		while True:
			try:
				data = s.recv(65536)
				result += data
			except:

				print("data error")
				continue
			size += len(data)
			if len_data == size:
				print("receive complete")
				break
		audio = open(path, "wb")
		audio.write(result)
		audio.close()
		play(i)
		count += 1
	print("RECEIVE END")
# 음성 녹음
def record(i):
	os.system("rec -c 1 -r 44100 Audio/input_"+i+".mp3 trim 0 10")

# 음성 재생
def play(i):
	os.system("play Audio/output_"+i+".mp3")

# 스트리밍 창
def StreamingWindow(window):
	global camIP, camPrivateIP, noti_state, stream_state, s

	def stream():
		_, frame = camera.read()
		cv2image = cv2.cvtColor(frame, cv2.COLOR_BGR2RGBA)
		img = Image.fromarray(cv2image)
		imgtk = ImageTk.PhotoImage(image=img)
		screen.imgtk = imgtk
		screen.configure(image=imgtk)
		screen.after(2, stream)

	# 창 생성 및 설정
	streaming = Toplevel(window)
	streaming.title("Streaming")
	streaming.geometry("800x480+0+0")
	#streaming.attributes("-fullscreen", True)
	streaming.resizable(False, False)

	# Screen, Camera 설정
	video = Frame(streaming, width=640, height=480)
	video.place(x=0, y=0)
	video.grid()
	screen = Label(video)
	screen.grid()
	try:
		camera = cv2.VideoCapture("http://"+camIP+":8080/stream/video.mjpeg")
	except:
		try:
			camera = cv2.VideoCapture("http://"+camPrivateIP+":8080/stream/video.mjpeg")
		except:
			print("스트리밍 실패")
			streaming.destroy()
	streaming_thread = threading.Thread(target=stream).start()

	# 인터폰에 스트리밍 접속 알림
	message = "connect"
	s.send(message.encode("utf-8"))
	stream_state = "ON"
	sleep(1)
	receive_thread = threading.Thread(target=receive).start()

	# button
	buttonList = Frame(streaming, width=160, height=480)
	buttonList.place(x=640, y=0)

	bt_call = Button(buttonList, text="Call", command=call).place(x=0, y=0, width=160, height=160)
	bt_hangup = Button(buttonList, text="Hang Up", command=hangup).place(x=0, y=160, width=160, height=160)
	bt_close = Button(buttonList, text="Close", command=partial(closeStreaming, streaming)).place(x=0, y=320, width=160, height=160)

	# esc 누를 시 종료(임시)
	streaming.bind("<Escape>", lambda e: streaming.destroy())

# 스트리밍창 종료
def closeStreaming(streaming):
	global noti_state, call_state, stream_state, s

	noti_state = "ON"
	call_state = "OFF"
	stream_state = "OFF"

	message = "close"
	s.send(message.encode("utf-8"))

	noti_thread = threading.Thread(target=notification).start()
	sleep(1)
	streaming.destroy()
	print("Streaming Window Close")

###########
# HISTORY #
###########

# 방문기록 목록 창
def HistoryWindow(window):
	print("History Window")

# 방문 기록영상 재생 창
def PlayVideoWindow(window, path):
	print("Play Video Window")

################
# NOTIFICATION #
################

# 알림 처리
def notification():
	global noti_state, s

	print("notification start")

	sleep(2)
	while True:
		if noti_state == "ON":
			try:
				data = s.recv(1023)
				data = data.decode("utf-8")
			except:
				print("recevie error")
			if data == "bell":
				notificationWindow()
				print("bell")
			else:
				noti_state = "OFF"
				print(data)
				break
		else:
			print("notification end")
			break

# 알림 창
def notificationWindow():
	messagebox.showwarning("Notice","방문자가 있습니다.")

########
# MAIN #
########

# Main 창 생성 및 설정
main = Tk()
main.title("Main")
main.geometry("800x480+0+0")
#main.attributes("-fullscreen", True)
main.resizable(False, False)
main.lower()

# esc 누를 시 종료(임시)
main.bind("<Escape>", lambda e: main.destroy())

# Main 창 Frame
mainframe = Frame(main, width=800, height=480)
mainframe.pack()

bt_streaming = Button(mainframe, text="Streaming", bg="#FFE08C", font=("Arial", 20, "bold"), activebackground="#FFE08C", command=partial(StreamingWindow, main)).place(x=0, y=0, width=800, height=240)
bt_history = Button(mainframe, text="History", bg="#E4F7BA", font=("Arial", 20, "bold"), activebackground="#E4F7BA", command=partial(HistoryWindow, main)).place(x=0, y=240, width=800, height=240)

# Login 창 생성
LoginWindow(main)

main.mainloop()
