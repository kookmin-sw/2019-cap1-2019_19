# -*- coding: utf-8 -*-
from Module import ip
from Module import init
from Module import pir
from Module import image
from Mpdule import video
from Module import button
from Module import communication as c

from time import sleep
import threading
import socket

MONITOR = 'off'
HOST = ''
PORT = 0000

ip = ip.getMyIP()
id, m_ip = init.init(ip)
if m_ip != None:
	MONITOR = 'ON'
	monitor = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
	monitor.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, 1)
	monitor.bind((HOST,PORT))
	monitor.listen(1)
	print("listening...")
	conn, addr = monitor.accept()
	print(addr,"접속")
	ct = threading.Thread(target=c.communication, args=(conn,)).start()
pir.init()
button.init()

while True:
	# 벨 눌른 경우 -> 모니터에 알림
	if button.click():
		msg = "bell"
		conn.send(msg.encode('utf-8'))
		print("버튼 누름")

	if pir.visit() == 1:
		print("방문자 확인")
		stopPir = threading.Thread(target=pir.stop).start()
		imageThread = threading.Thread(target=image.image, args=(id,)).start()
		videoThread = threading.Thread(target=video.video, args=(id,)).start()

