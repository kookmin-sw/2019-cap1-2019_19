# -*- coding: utf-8 -*- 
import RPi.GPIO as GPIO
from time import sleep

pirPin = 24
state = 'ON'

def init():
	global pirPin

	GPIO.setmode(GPIO.BCM)
	GPIO.setup(pirPin, GPIO.IN, GPIO.PUD_UP)
	print("PIR 센서 설정 완료")

def visit():
	global pirPin, state

	if state == 'ON' and GPIO.input(pirPin) == 1:
		return 1
	else:
		return 0

def stop():
	global state

	state = 'OFF'
	print(state)
	sleep(300)
	state = 'ON'
	print(state)
