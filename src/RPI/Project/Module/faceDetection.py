#-*- coding: utf-8 -*-
import numpy as np
import cv2
import dlib

# 사진에서 얼굴 인식한 후 인식된 사람의 수 반환
def detection(count):
	detector = dlib.get_frontal_face_detector()
	img = cv2.imread('../Project/Image/image_'+str(count)+'.jpg')
	rgbImg = cv2.cvtColor(img, cv2.COLOR_BGR2RGB)
	res = detector(rgbImg, 1)
	return len(res)
