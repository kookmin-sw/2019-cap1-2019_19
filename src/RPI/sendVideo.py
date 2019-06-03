# -*- coding: utf-8 -*-

import RPi.GPIO as GPIO
import time
import datetime
import requests
import cv2
import pymysql
import os

f = open("id.txt", 'r')
line = f.readline()
id = line
f.close()

def DB():
    # 권한 문제로 연결 실패
    conn = pymysql.connect(host='52.78.219.61',user='monitor',password='Kookmin1!', db='db', charset='utf8');

    try:
        cursor = conn.cursor()
        sql = "insert into SEUNGAE(id, name, rDate, belong, video) values (%s, %s, %s, %s, %s)"
        cursor.execute(sql, (id ,'NULL',date2, 'NULL', 'http://52.78.219.61/video/'+date+'.mp4'))
        conn.commit()
        print(" DB 추가 완료" )
    finally:
        conn.close()


# PIR 센서 설정
GPIO.setmode(GPIO.BCM)
pirPin = 18
GPIO.setup(pirPin, GPIO.IN, GPIO.PUD_UP)

capture_duration = 45

# 파일 이름을 위한 date
date = datetime.datetime.now().strftime("%y%m%d_%H%M%S")
date2 = datetime.datetime.now()

# 스트리밍 서버에서 영상 읽기
camera = cv2.VideoCapture("http://localhost:8080/stream/video.mjpeg")

# 영상 저장 ( outputfile / fourcc / frame / size )
out = cv2.VideoWriter(date+'.mp4', 0X00000021, 30, (640,480))

# 카메라가 열렸는지 확인
if(camera.isOpened() == False):
    print("Error opening video stream or file")


# 카메라가 제대로 열렸으면
while(camera.isOpened()):
    # PIR 센서가 사람 인식
    if GPIO.input(pirPin) == GPIO.HIGH:
        print("사람 감지")
        start_time = time.time()
        while(int(time.time() - start_time) < capture_duration) :
            #영상을 한 프레임씩 읽어오기
            #(영상을 제대로 읽으면 ret=true, 잘못 읽으면 ret=false)
            ret, frame = camera.read()

            #영상을 제대로 읽었을 때
            if ret:
                out.write(frame)

            else:
                break
        out.release()
        time.sleep(5)
        # video: h.264, audio: aac 변환
        os.system("ffmpeg -y -i "+date+".mp4 -acodec aac -vcodec libx264 "+date+".mp4")

        # 영상 웹서버에 전송
        print("웹서버에 전송 시작")
        # 전송할 웹 서버 주소
        upload_url = 'http://52.78.219.61/video_upload.php'
        # 전송할 파일 설정
        file_ = {'myfile': (date+'.mp4', open(date+'.mp4', 'rb'))}
        # 파일 전송
        r = requests.post(upload_url, files=file_)
        print(r.text)

        DB()

        break

        time.sleep(30)

camera.release()
cv2.destroyAllWindows()
