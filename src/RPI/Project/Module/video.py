# -*- coding: utf-8 -*-
import cv2
import pymysql
import time
import datetime
import requests
import os

def insertDB(id, date, date_stamp):
	conn = pymysql.connect(host='####', port=0000, user='####', passwd="####", db='####', charset='utf8')
	cursor = conn.cursor()
	try:
		sql = 'insert into SEUNGAE(id, name, rDate, belong, video) values (%s, %s, %s, %s, %s)'
		cursor.execute(sql, (id, 'NULL', date_stamp, 'NULL', 'http://####/video/'+date+'.mp4'))
		conn.commit()
		print('DB 추가 완료')
	finally:
		conn.close()

def video():
	date_stamp = datetime.datetime.now()
	date = date_stamp.strftime('%y%m%d_%H%M%S')
	print(date)

	camera = cv2.VideoCapture('http://localhost:8080/stream/video.mjpeg')
	out = cv2.VideoWriter(date+'_before.mp4', 0X00000021, 30, (640,480))

	if(camera.isOpened() == False):
		print('Error opening video stream or file')

	start_time =time.time()
	while(time.time()-start_time < 20):
		ret, frame = camera.read()
		if ret:
			out.write(frame)
		else:
			break
	out.release()

	os.system('ffmpeg -y -i '+date+'_before.mp4 -acodec aac -vcodec libx264 '+date+'.mp4')

	insertDB(id, date, date_stamp)
	print("DB insert")
	uploadVideo(date)
	print("video upload")
	camera.release()

	os.system('rm '+date+'.mp4')
	os.system('rm '+date+'_before.mp4')

def uploadVideo(date):
	upload_url = 'http://####/video_upload.php'
	file_ = {'myfile': (date+'.mp4', open(date+'.mp4', 'rb'))}
	r = requests.post(upload_url, files=file_)
	print(r.text)

