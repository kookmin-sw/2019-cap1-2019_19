# -*- coding: utf-8 -*-

import RPi.GPIO as GPIO
import time
import datetime
import requests
import cv2

date = " "
capture_duration = 0
pirPin = 0

def init():
    global pirPin
    global capture_duration
    global date
    # PIR 센서 설정
    GPIO.setmode(GPIO.BCM)
    pirPin = 18
    GPIO.setup(pirPin, GPIO.IN, GPIO.PUD_UP)

    capture_duration = 41

    # 파일 이름을 위한 date
    date = datetime.datetime.now().strftime("%y%m%d_%H%M%S")


def saveVideo():
    global date

    # 스트리밍 서버에서 영상 읽기
    camera = cv2.VideoCapture("http://localhost:8080/stream/video.mjpeg")
    fourcc = cv2.VideoWriter_fourcc(*'X264')
    out = cv2.VideoWriter(date+'.mp4', fourcc, 20, (640,480))

    # 카메라가 열렸는지 확인
    if(camera.isOpened() == False):
        print("Error opening video stream or file")


    start_time = time.time()
    # 카메라가 제대로 열렸으면
    while(camera.isOpened()):
        while(int(time.time() - start_time) < capture_duration) :
            #영상을 한 프레임씩 읽어오기
            #(영상을 제대로 읽으면 ret=true, 잘못 읽으면 ret=false)
            ret, frame = camera.read()

            #영상을 제대로 읽었을 때
            if ret:
                # 이미지 반전 0:상하, 1:좌
                frame = cv2.flip(frame, 0)
                out.write(frame)
                cv2.imshow('frame', frame)

            else:
                break

        time.sleep(3)

    camera.release()
    out.release()
    cv2.destroyAllWindows()

def sendVideo():
    global date

    # 영상 웹서버에 전송
    print("웹서버에 전송 시작")
    # 전송할 웹 서버 주소
    upload_url = 'http://52.78.219.61/video_upload.php'
    # 전송할 파일 설정
    file_ = {'myfile': (date+'.mp4', open(date+'.mp4', 'rb'))}
    # 파일 전송
    r = requests.post(upload_url, files=file_)
    print(r.text)

    time.sleep(30)


def svMain():
    global pirPin
    init()

    while True:
        # PIR 센서가 사람 인식
        if GPIO.input(pirPin) == GPIO.HIGH:
            print("사람 감지")
            time.sleep(1)

            saveVideo()
            sendVideo()
