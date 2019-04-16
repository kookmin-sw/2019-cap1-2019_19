# -*- coding: utf-8 -*-

import RPi.GPIO as GPIO
import time
from picamera import PiCamera
import datetime
import requests

# PIR 센서 설정
GPIO.setmode(GPIO.BCM)
pirPin = 18
GPIO.setup(pirPin, GPIO.IN, GPIO.PUD_UP)

# Camera 설정
camera = PiCamera()

# 파일 이름을 위한 date
date = datetime.datetime.now().strftime("%y%m%d_%H%M%S")

while True:
    # PIR 센서가 사람 인식
    if GPIO.input(pirPin) == GPIO.HIGH:
        print("사람 감지")
        camera.start_preview()
        # 영상 촬영 및 저장할 장소
        print("촬영 시작")
        camera.start_recording('/home/pi/Code/%s.h264' % date)
        # 사람이 감지되고 30초 동안 영상 촬영
        time.sleep(30)
        # 영상 촬영 종료
        camera.stop_recording()
        print("촬영 종료")
        camera.stop_preview()

        time.sleep(1)

        # 영상 웹서버에 전송
        print("웹서버에 전송 시작")
        # 전송할 웹 서버 주소
        upload_url = 'http://52.78.219.61/video_upload.php'
        # 전송할 파일 설정
        file_ = {'myfile': (date+'.h264', open(date+'.h264', 'rb'))}
        # 파일 전송
        r = requests.post(upload_url, files=file_)
        print (r.text)

camera.close()

