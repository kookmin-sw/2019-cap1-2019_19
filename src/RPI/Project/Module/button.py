# -*- coding: utf-8 -*- 
import RPi.GPIO as GPIO
import os
import communication as c

btnPin = 25

def init():
	global btnPin

	GPIO.setmode(GPIO.BCM)
	GPIO.setup(btnPin, GPIO.IN, GPIO.PUD_UP)
	print("Button 설정 완료")

def click():
	global btnPin

	if GPIO.input(btnPin) == 1:
		return 1
	else:
		return 0

def play():
	os.system("play Audio/bell.wav")

def button():
	while True:
		if click() == 1:
			play()
			c.notification()


