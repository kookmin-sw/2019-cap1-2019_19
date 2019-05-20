# -*- coding: utf-8 -*- 
import socket
import base64
import cv2
from Module import faceDetection as fd
import time

HOST = '####'
PORT = 0000
PATH = '../Project/Image/'

def save():
	global PATH

	# 스트리밍 서버에서 영상 가져오기
	camera = cv2.VideoCapture("http://localhost:8080/stream/video.mjpeg")

	# 사진 저장후 얼굴 인식하여 인식된 사진 3장 저장
	t = time.time()
	result = 'sucess'
	for i in range(1, 4):
		print(str(i)+"번째 사진 저장 시작")
		while True:
			if time.time() - t > 15:
				result = 'fail'
				break
			ret, frame = camera.read()
			cv2.imwrite(PATH+'image_%s.jpg'%i, frame)
			num = fd.detection(i)
			print(num)
			if num > 0:
				break
		print(str(i)+"번째 사진 저장")
	del(camera)

	if result == 'fail':
		return 0
	else:
		return 1

def send(id, result):
	global HOST, PORT, PATH

	s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
	s.connect((HOST,PORT))

	print("전송 시작")
	# id 전송
	s.send(id.encode('utf-8'))

	if result == 1:
		# 사진 3장 전송
		for i in range(1, 4):
			img = open(PATH+'image_'+str(i)+'.jpg','rb')
			b = base64.b64encode(img.read())

			len_b = str(len(b))
			s.send(len_b.encode('utf-8'))
			time.sleep(1)

			s.sendall(b)
			time.sleep(1)

			img.close()
	else:
		# 사진 1장 전송
		img = open(PATH+'image_1.jpg','rb')
		b = base64.b64encode(img.read())

		lenb = str(len(b))
		s.send(len_b.encode('utf-8'))
		time.sleep(1)

		s.sendall(b)
		time.sleep(1)

		img.close()
	s.close()
	print("전송 끝")

def image(id):
	r = save()
	#send(id, r)

