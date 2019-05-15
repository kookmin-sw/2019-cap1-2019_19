# -*- coding:utf-8 -*-
import socket
from time import sleep
import threading
import os

HOST = ''
PORT = ####

state = 'on'
call_state = 'off'

def record(i):
	os.system("arecord --format=S16_LE --duration=5 --rate=16000 --file-type=wav input_"+i+".wav")

def play(i):
	os.system("aplay --format=S16_LE --rate=16000 output_"+i+".wav")

def receive(conn):
	global state
	count = 0
	while True:
		i = str(count%3+1)
		path = 'output_'+i+'.wav'
		if state == "programoff":
			break
		len_data = conn.recv(1023)
		len_data = len_data.decode('utf-8')
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
				call_state = 'off'
				print("receive end")
				break
			result += data
			size += len(data)
			if len_data == size:
				print("complete")
				break
		if data == "bye from monitor":
			break
		audio = open(path, 'wb')
		audio.write(result)
		audio.close()
		play(i)
		count += 1

def send(conn):
	global state, call_state
	e_m = "bye from rpi"

	count = 0
	while True:
		if state == "off" or state == "programoff":
			break
		i = str(count%3+1)
		print("record start")
		record(i)
		print("record complete")
		print("send start")
		path = "input_"+i+".wav"
		audio = open(path, "rb")
		data = audio.read()
		len_data = str(len(data))
		if state == "on":
			try:
				conn.send(len_data.encode('utf-8'))
				sleep(1)
				conn.sendall(data)
				print("send complete")
			except:
				print("error")
				break
		audio.close()
		sleep(1)
		count += 1
	call_state = "off"
def main():
	global HOST, PORT, state, call_state

	s=socket.socket(socket.AF_INET, socket.SOCK_STREAM)
	s.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, 1)

	try:
		s.bind((HOST,PORT))
	except socket.error as msg:
		print("Bind Failed "+str(msg[1]))
		return

	message = "ok"
	s.listen(1)
	print("listening...")
	conn, addr = s.accept()
	print(addr)
	count = 0
	while True:
		if call_state == "on" and state == "programoff":
			print("program off")
			break
		if call_state == 'off':
			data = conn.recv(65536)
			data = data.decode('utf-8')
			print(data)
			if data == "connect":
				state = "on"
				p1 = threading.Thread(target=send,args=(conn,))
				p1.start()
			elif data == "call":
				call_state = 'on'
				p2 = threading.Thread(target=receive, args=(conn,))
				p2.start()
			elif data == "close":
				print("end")
				state = "programoff"
				sleep(1)
				break
			count += 1
	conn.close()
	s.close()
while True:
	main()
	sleep(2)
