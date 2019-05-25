# -*- coding: utf-8 -*- 
import os
import threading
import socket
from time import sleep

##########
# SOCKET #
##########
HOST = ""
PORT = 0000
monitor_conn = ""

#########
# STATE #
#########
stream_state = "ON"
call_state = "OFF"

############
# FUNCTION #
############

# 음성 녹음
def record(i, device):
	os.system("rec -c 1 -r 44100 Audio/"+device+"/input_"+i+".mp3 trim 0 10")

# 음성 재생
def play(i, device):
	os.system("play Audio/"+device+"/output_"+i+".mp3")

# 알림 전송
def notification():
	global call_state, monitor_conn

	msg = "bell"
	if call_state == "OFF":
		try:
			monitor_conn.send(msg.encode("utf-8"))
			print("전송 성공")
		except:
			print("전송 실패")
	print("벨 누름")

# 음성 수신
def receive(conn,device):
	global stream_state, call_state
	count = 0
	while True:
		i = str(count%3+1)
		path = "Audio/"+device+"/output_"+i+".mp3"

		len_data = conn.recv(1023)
		len_data = len_data.decode("utf-8")
		if len_data == "close":
			print("streaming window close")
			stream_state = "OFF"
			sleep(1)
			break
		len_data = int(len_data)
		sleep(1)

		size = 0
		result = ""
		while True:
			data = conn.recv(65536)
			if data == "BYE":
				call_state = "OFF"
				print("receive end")
				break
			result += data
			size += len(data)
			if len_data == size:
				print("receive complete")
				break
		if call_state == "OFF":
			break
		audio = open(path, 'wb')
		audio.write(result)
		audio.close()
		play(i, device)
		count += 1

# 음성 송신
def send(conn,device):
	global stream_state, call_state

	count = 0
	while True:
		if stream_state == "OFF":
			break

		i = str(count%3+1)
		print("record start")
		record(i, device)
		print("record complete")

		print("send start")
		path = "Audio/"+device+"/input_"+i+".mp3"
		audio = open(path, "rb")
		data = audio.read()
		len_data = str(len(data))
		if stream_state == "ON":
			try:
				conn.send(len_data.encode("utf-8"))
				sleep(2)
				conn.sendall(data)
				print("send complete")
			except:
				print("error")
				break
		audio.close()
		count += 1
	call_state = "OFF"

# 소켓 연결
def connect(m_ip):
	global monitor_conn

	device = "App"

	s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
	s.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, 1)
	s.bind((HOST, PORT))
	s.listen(2)
	print("listening...")

	while True:
		conn, addr = s.accept()
		print(addr,m_ip)
		if addr[0] == str(m_ip):
			monitor_conn = conn
			device = "Monitor"
			print("모니터 접속")
		else:
			print("앱 접속")
		t = threading.Thread(target=communication, args=(conn,device,)).start()

# 음성 통신
def communication(conn,device):
	global stream_state, call_state

	while True:
		if call_state == "OFF":
			data = conn.recv(1023)
			data = data.decode("utf-8")
			print("connect|call|close")
			print(data)
			if data == "connect":
				conn.send(data.encode("utf-8"))
				sleep(1)
				stream_state = "ON"
				st = threading.Thread(target=send,args=(conn,device,)).start()
			elif data == "call":
				call_state = "ON"
				rt = threading.Thread(target=receive,args=(conn,device,)).start()
			elif data == "close":
				print("streaming window close")
				stream_state = "OFF"
				if device == "App":
					conn.close()
					break
