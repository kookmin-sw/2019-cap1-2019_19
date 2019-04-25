from tkinter import *
from PIL import Image

win = Tk()
win.title("Smart Interphone")
win.geometry('800x480+0+0')
win.resizable(0,0)

video = Frame(win, width = 640, height = 480, bg = "black")
video.pack(side="left")

menu = Frame(win, width = 160, height = 480)
menu.pack()

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

mic_on = Button(menu, text="MIC ON", width = 160, height=15, bg = "green", command= mic_on)
mic_on.pack(expand=1)

mic_off = Button(menu, text = "MIC OFF", width = 160, height=15, bg="yellow", command= mic_off)

mic_off.pack(expand=1)

win.mainloop()