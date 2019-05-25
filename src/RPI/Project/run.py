# -*- coding: utf-8 -*-
from Module import ip
from Module import init
from Module import pir
from Module import image
from Module import video
from Module import button
from Module import communication as c

from time import sleep
import datetime
import threading
import socket

#########
# STATE #
#########
MONITOR = 'OFF'

########
# MAIN #
########
ip, e_ip = ip.getMyIP()
id, m_ip = init.init(ip, e_ip)

if m_ip != None:
	MONITOR = "ON"

# 통신
c_t = threading.Thread(target=c.connect, args=(m_ip,)).start()

# PIR센서, Button 초기화
pir.init()
button.init()

while True:
	# 벨 눌른 경우 -> 모니터에 알림
	if button.click() and MONITOR == "ON":
		c.notification()
		button.play()

	if pir.visit() == 1:
		# 방문 시간
		date_stamp = datetime.datetime.now()
		date = date_stamp.strftime('%y%m%d_%H%M%S')

		print("방문자 확인")

		# PIR센서 무시
		stopPir = threading.Thread(target=pir.stop).start()
		# 사진
		imageThread = threading.Thread(target=image.image, args=(id, date_stamp,)).start()
		# 영상
		videoThread = threading.Thread(target=video.video, args=(id, date, date_stamp,)).start()
