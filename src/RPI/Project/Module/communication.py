import os
import threading
from time import sleep

state = 'on'
call_state = 'off'

# 음성 녹음
def record(i):
	os.system("rec -c 1 -r 44100 Audio/input_"+i+".mp3 trim 0 5")

# 음성 재생
def play(i):
	os.system("play Audio/output_"+i+".mp3")

# 벨 눌렸을 때 모니터에 알림 전송
def notification(conn):
	global call_state

	msg = "bell"
	if call_state == "off":
		try:
			conn.send(msg.encode("utf-8"))
			print("전송 성공")
		except:
			print("전송 실패")
	print("벨 누름")

# 통화 수신
def receive(conn):
	global state, call_state
	count = 0
	while True:
		i = str(count%3+1)
		path = "../Project/Audio/output_"+i+".wav"
		if state == "programoff":
			break
		len_data = conn.recv(1023)
		len_data = len_data.decode("utf-8")
		if len_data == "close":
			print("call-programoff")
			state = "programoff"
			sleep(1)
			break
		len_data = int(len_data)
		sleep(1)

		size = 0
		result = ""
		while True:
			data = conn.recv(65536)
			if data == "bye from monitor":
				call_state = "off"
				print("receive end")
				break
			result += data
			size += len(data)
			if len_data == size:
				print("receive complete")
				break
		if call_state == "off":
			break
		audio = open(path, 'wb')
		audio.write(result)
		audio.close()
		play(i)
		count += 1

# 통화 송신
def send(conn):
	global state, call_state
	e_m = "bye from rpi"

	count = 0
	while True:
		if state != "on":
			break
		i = str(count%3+1)
		print("record start")
		record(i)
		print("record complete")
		print("send start")
		path = "../Project/Audio/input_"+i+".wav"
		audio = open(path, "rb")
		data = audio.read()
		len_data = str(len(data))
		if state == "on":
			try:
				conn.send(len_data.encode("utf-8"))
				sleep(1)
				conn.sendall(data)
				print("send complete")
			except:
				print("error")
				break
		audio.close()
		count += 1
	call_state = "off"

# 음성 스트리밍
def communication(conn):
	global state, call_state
	count = 0
	while True:
		if call_state == "on" and state == "programoff":
			print("program off")
			break
		if call_state == "off":
			data = conn.recv(65536)
			data = data.decode("utf-8")
			print(data)
			if data == "connect":
				state = "on"
				st = threading.Thread(target=send,args=(conn,)).start()
			elif data == "call":
				call_state = "on"
				rt = threading.Thread(target=receive,args=(conn,)).start()
			elif data == "close":
				print("end")
				state = "programoff"
				sleep(1)
				break
			count += 1
