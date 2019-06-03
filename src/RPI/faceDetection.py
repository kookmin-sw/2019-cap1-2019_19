#-*- coding: utf-8 -*-
import numpy as np
import cv2

def detection(count):
	face_cascade = cv2.CascadeClassifier('haarcascade_frontalface_default.xml')

	img = cv2.imread('tempImage/image_'+str(count)+'.jpg')
	gray = cv2.cvtColor(img, cv2.COLOR_BGR2GRAY)
	faces = face_cascade.detectMultiScale(gray, 1.3,5)
	if len(faces) > 0:
		return 1
	return 0