# -*- coding:utf-8 -*-
import socket
from time import sleep
import threading

HOST = ''
PORT = ####

state = 'on'

def receive(conn):
	while True:
		if state == "programoff":
			break
		print(state)
		data = conn.recv(65536)
		data = data.decode()
		print(data)
		if data == "bye from monitor":
			print("receive end")
			break
		sleep(1)
def send(conn):
	global state
	s_m = "hi from rpi"
	e_m = "bye from rpi"

	while True:
		if state == "off" or state == "programoff":
			conn.send(e_m.encode('utf-8'))
			sleep(1)
			break
		conn.send(s_m.encode('utf-8'))
		sleep(1)

def main():
	global HOST, PORT, state

	s=socket.socket(socket.AF_INET, socket.SOCK_STREAM)
	s.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, 1)
	try:
		s.bind((HOST,PORT))
	except socket.error as msg:
		print("Bind Failed"+str(msg[1]))

	message = "ok"
	s.listen(1)
	print("listening...")
	conn, addr = s.accept()
	print(addr)
	while True:
		data = conn.recv(65536)
		data = data.decode('utf-8')
		print(data)
		if data == "connect":
			state = "on"
			p1 = threading.Thread(target=send,args=(conn,))
			p1.start()
		elif data == "call":
			p2 = threading.Thread(target=receive, args=(conn,))
			p2.start()
		elif data == "close":
			print("end")
			state = "programoff"
			sleep(2)
			break
	conn.close()
	s.close()
while True:
	main()
