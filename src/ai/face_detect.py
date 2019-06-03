


# -*- coding: utf-8 -*-

import dlib
import cv2
import os
import numpy as np
import sys
from datetime import datetime

folder_name = sys.argv[1] + '/'
now = datetime.now()
when = '%s_%s_%s_' % (now.year, now.month, now.day)
np.set_printoptions(precision=2)

#재생할 파일
VIDEO_FILE_PATH = '2sec_test.mp4'
detector = dlib.get_frontal_face_detector()

# 동영상 파일 open
cap = cv2.VideoCapture(VIDEO_FILE_PATH)

# 잘 열렸는지 확인
if cap.isOpened() == False:
    print ('Can\'t open the video (%d)' % (VIDEO_FILE_PATH))
    exit()

i = 0
j = 0

##########################################
#  추후에 속도가 너무 느리면 적절히 프레임 조젏할 것 #
##########################################
while(True):
    #파일로 부터 이미지 얻기
    ret, frame = cap.read()
    
    # 동영상이 끝나면 종료
    if frame is None:
        break;
    
    height, weight = frame.shape[:2]
    # 이미지 회전값 설정 - 현재 동영상파일은 옆으로 회전되어있음 (안드로이드 영상에 따라 조정할 것)
    rotate = cv2.getRotationMatrix2D(((weight-1)/2.0, (weight-1)/2.0), #회전 중심
                                     270, # 회전각도(양수 반시계방향, 음수 시계방향)
                                     1) # 이미지 배율

    # 이미지 회전
    frame = cv2.warpAffine(frame, rotate, (weight,weight))
    
    rgbImg = cv2.cvtColor(frame, cv2.COLOR_BGR2RGB)

    res = detector(rgbImg, 1)

    if len(res) > 0:
        #params=[cv2.IMWRITE_PNG_COMPRESSION,0] => 압축률 높아질수록 영상의 질은 높아지지만 속도가 늦어짐 (추후에 조정)
        #cv2.imwrite('img/test_%d.png' % i,frame, params=[cv2.IMWRITE_PNG_COMPRESSION,0])
        filename = folder_name + when + 'data' + str(i) + '.png'
        cv2.imwrite(filename,frame)
        #cv2.imwrite('img/test_%d.png' % i,frame)
        i += 1
        print("res = {}".format(res))


