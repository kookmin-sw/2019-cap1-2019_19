#-*- coding: utf-8 -*-
from tkinter import *
from PIL import Image, ImageTk
import cv2
import pymysql
from subprocess import check_output

def myIP():
	# local ip
	ip = check_output(['hostname', '-I']).decode('utf8')
	ip = ip.replace('\n','')
	ip = ip.replace(' ','')
	return ip

def getIP():
	# DB에서 스트리밍 서버 IP 가져오기
	conn = pymysql.connect(host='52.78.219.61', port=3306, user='monitor', passwd="Kookmin1!", db='db', charset='utf8')
	myip = myIP()

	curs = conn.cursor()

	sql = "select * from Login where m_ip=%s"
	curs.execute(sql,myip)

	camip = "0.0.0.0"
	rows = curs.fetchall()
	if len(rows) != 1:
		print("등록된 기기가 없습니다")
	else:
		camip = rows[0][2]
	
	conn.close()
	return camip

win = Tk()
win.title("Smart Interphone")
win.geometry('800x480+0+0')
win.resizable(0,0)

video = Frame(win, width = 640, height = 480)
video.grid()
screen = Label(video)
screen.grid()
ip = getIP()
camera = cv2.VideoCapture('http://'+ip+':8080/stream/video.mjpeg')
video.pack(side="left")
menu = Frame(win, width = 160, height = 480)
menu.pack()

def stream():
	_, frame = camera.read()
	cv2image = cv2.cvtColor(frame, cv2.COLOR_BGR2RGBA)
	img = Image.fromarray(cv2image)
	imgtk = ImageTk.PhotoImage(image=img)
	screen.imgtk = imgtk
	screen.configure(image=imgtk)
	screen.after(1, stream)

# 0: Off, 1: On
global mic_state
mic_state = 0

def mic_on():
	global mic_state
	if mic_state == 1:
		print("already mic_on")
		return
	mic_state = 1

	# 마이크 키는 기능 작성
	
	print("mic_on")
	print("mic_state: ", mic_state)

def mic_off():
	global mic_state
	if mic_state == 0:
		print("already mic_off")
		return
	mic_state = 0

	#마이크 끄는 기능 작성

	print("mic_off")
	print("mic_state: ", mic_state)

stream()

mic_on = Button(menu, text="MIC ON", width = 160, height=15, bg = "green", command= mic_on)
mic_on.pack(expand=1)

mic_off = Button(menu, text = "MIC OFF", width = 160, height=15, bg="yellow", command= mic_off)

mic_off.pack(expand=1)

win.mainloop()