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
#os.system("rec -c 1 -r 44100 Audio/"+device+"/input_"+i+".mp3 trim 0 10")
    print("{}번째 음성 녹음 시작".format(i))
    sleep(10)
    print("{}번째 음성 녹음 끝".format(i))

# 음성 재생
def play(i, device):
#os.system("play Audio/"+device+"/output_"+i+".mp3")
    print("{}번째 음성 재생 시작".format(i))
    sleep(10)
    print("{}번째 음성 재생 끝".format(i))

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

# app 음성 수신
def receive_app(conn,device):
    global stream_state, call_state
    count = 0
    
    # 앱은 무한루프 필요없음
    i = str(count%3+1)
    path = "output_"+i+".mp3"
    
    len_data = conn.recv(1023)
    len_data = len_data.decode("utf-8")
    
    temp = len_data.split("\n")
    len_data = temp[0]
    
    print("전송받을 파일의 크기는 "+len_data)
    
    if len_data == "close":
        print("streaming window close")
        stream_state = "OFF"
        sleep(1)
        return
    
    len_data = int(len_data)
    sleep(1)

    print("receive 준비 완료")

    size = 0
    result = b"" # byte로 써야함
    while True:
        data = conn.recv(65536)
        if data == "BYE":
            call_state = "OFF"
            print("receive end")
            break
        
        print("파일 받는 중 {}".format(len(data)))
        result += data
        size += len(data)

        if size >= len_data:
            print("receive complete")
            break

    audio = open(path, 'wb')
    audio.write(result)
    audio.close()
    play(i, device)
    count += 1
    sleep(2)
    call_state = "OFF"

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

# app 음성 송신
def send_app(conn,device):
    global stream_state, call_state
    print("this is send")
    
    count = 0
    while True:
        
        if stream_state == "OFF":
            break
    
        i = str(count%3+1)
        print("record start")
        record(i, device)
        print("record complete")
        
        if call_state == "OFF":
            print("send start")
            path = "input_"+i+".mp3"
            audio = open(path, "rb")
            data = audio.read()
            len_data = str(len(data))
            print("전송할 파일의 크기는 "+len_data)
            
            if stream_state == "ON" and call_state == "OFF":
                try:
                    print("send len")
                    conn.send(len_data.encode("utf-8"))
                    sleep(2)
                    print("send data")
                    conn.sendall(data)
                    sleep(1)
                    print("send complete")
                except:
                    print("error")
                    break
            else:
                print("지금은 call state = ON상태")
            
            
            audio.close()
            count += 1

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
                    
            elif data == "connect\n":
                sleep(1)
                stream_state = "ON"
                st = threading.Thread(target=send_app,args=(conn,device,)).start()
            elif data == "call":
                call_state = "ON"
                rt = threading.Thread(target=receive,args=(conn,device,)).start()
            elif data == "call\n":
                call_state = "ON"
                sleep(1)
                print("receive 쓰레드 시작")
                rt = threading.Thread(target=receive_app,args=(conn,device,)).start()
            elif data == "close" or data == "close\n":
                print("streaming window close")
                stream_state = "OFF"
                if device == "App":
                    conn.close()
                    break