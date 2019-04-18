# -*- coding: utf-8 -*-

import socket
import sys
import time
import base64
import subprocess

HOST = '' #all available interfaces
PORT = 2222

#1. open Socket
s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
print ('Socket created')

#2. bind to a address and port
try:
    s.bind((HOST, PORT))
except socket.error as msg:
    print ('Bind Failed. Error code: ' + str(msg[0]) + ' Message: ' + msg[1])
    sys.exit()

print ('Socket bind complete')

#3. Listen for incoming connections
s.listen(10)
print ('Socket now listening')


#keep talking with the client
while 1:
    #4. Accept connection
    conn, addr = s.accept()
    print ('Connected with ' + addr[0] + ':' + str(addr[1]))
    
    
    for i in range(1, 4):
        #5. Read/Send
        img = ""
        len_data = conn.recv(1024).decode()
        len_data = int(len_data)
        size = 0
        
        
        #        print("type size", type(size))
        print("Data Reading")
        while 1:
            
            data = conn.recv(65536)
            str_data = data.decode()
            img += data.decode()
            #f.write(base64.b64decode(data))
            size += len(str_data)
            #            print(size, type(size), len_data, type(len_data))
            if size == len_data:
                print("Finished Data Reading")
                break
    #print(img)
    #        print(i)

    with open('/root/openface/temp/test_'+str(i)+'.jpeg', 'wb') as f:
        f.write(base64.b64decode(img))
        time.sleep(0.1)
for j in range(1,4):
    subprocess.call(['/root/openface/demos/classifier_test.py infer /root/openface/embedding/us/classifier.pkl /root/openface/temp/test_'+str(j)+'.jpeg'], shell=True)
#    break
conn.close()
s.close()
