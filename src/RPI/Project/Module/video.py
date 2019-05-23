# -*- coding: utf-8 -*-
import cv2
import pymysql
import time
import datetime
import requests
import os

row_data = 0
idx = -1

def DB(id, date, date_stamp):
    global row_data
    global idx

    conn = pymysql.connect(host='52.78.219.61', user='monitor', passwd="Kookmin1!", db='db', cha$
    cursor = conn.cursor()
    try:
        sql = "SELECT * FROM History WHERE id = %s AND rDate = %s"
        cursor.execute(sql, (id, date_stamp))
        result = cursor.fetchall()
        for row_data in result:
            idx = row_data[0]
        print('DB 조회 완료')
        sql2 = "UPDATE History SET video = %s  WHERE rIdx = %s"
        cursor.execute(sql2, ('/recordVideo/'+id+'/'+date+'.mp4', idx))
        conn.commit()
        print('DB 수정 완료')
        conn.commit()
    finally:
        conn.close()


def video(id, date, date_stamp):
    camera = cv2.VideoCapture('http://localhost:8080/stream/video.mjpeg')
    out = cv2.VideoWriter(date+'_before.mp4', 0X00000021, 30, (640,480))

    if(camera.isOpened() == False):
        print('Error opening video stream or file')

    start_time = time.time()
    while(time.time()-start_time < 20):
        ret, frame = camera.read()
        if ret:
            out.write(frame)
        else:
            break
    out.release()

    os.system('ffmpeg -y -i '+date+'_before.mp4 -acodec aac -vcodec libx264 '+date+'.mp4')

    DB(id, date, date_stamp)
    uploadVideo(date)
    print("video upload")
    camera.release()

    #os.system('rm '+date+'.mp4')
    #os.system('rm '+date+'_before.mp4')

def uploadVideo(date):
    upload_url = 'http://52.78.219.61/video_upload.php'
    file_ = {'myfile': (date+'.mp4', open(date+'.mp4', 'rb'))}
    r = requests.post(upload_url, files=file_)
    print(r.text)
