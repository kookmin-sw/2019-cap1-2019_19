#-*- coding: utf-8 -*-
import socket
import base64
import sys
import io
import os
import RPi.GPIO as GPIO
import faceDetection as fd
import streamingOnOff as soo
import time
import cv2
from PIL import Image
from array import array
from picamera import PiCamera

HOST = ''
PORT = 0
pirPin = 0

# 초기 설정
def init():
	# 전송 받을 주소 및 포트
	global HOST
	global PORT
	
	HOST = '###.###.###.###'
	PORT = ####

	# PIR 센서 설정
	global pirPin
	GPIO.setmode(GPIO.BCM)
	pirPin = 18
	GPIO.setup(pirPin, GPIO.IN, GPIO.PUD_UP)
	print("PIR 센서 설정 완료")

def saveImg():
	# 스트리밍 서버에서 영상가져오기
	camera = cv2.VideoCapture("http://localhost:8080/stream/video.mjpeg")

	# 사진 저장후 얼굴 인식하여 인식된 사진만 3장 저장
	for i in range(1, 4):
		while True:
			# 사진 읽어와 저장
			ret, frame = camera.read()
			cv2.imwrite('tempImage/image_%s.jpg'%i, frame)

			# 얼굴이 검출되면 루프 탈출
			if fd.detection(i) > 0:
				break
		print(str(i)+"번째 사진 저장")
	del(camera)

def sendImg():
	# socket 생성 및 연결
	global HOST
	global PORT
	s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
	s.connect((HOST, PORT))
	print("소켓 생성 및 연결")

	# 사진 전송
	for j in range(1, 4):
		print(str(j)+"번째 사진 전송")
		# 사진 읽어 오기 및 인코딩
		img = open('tempImage/image_'+str(j)+'.jpg', 'rb')
		b = base64.b64encode(img.read())

		# 사진 스트링 길이 전송
		len_b= str(len(b))
		s.send(len_b.encode('utf-8'))
		time.sleep(0.5)

		# 사진 스트링 전송
		s.sendall(b)
		time.sleep(1)

		img.close()
	s.close()

def siMain():
	global pirPin
	init()
	while True:
		if GPIO.input(pirPin) == GPIO.HIGH:
			saveImg()
			sendImg()
