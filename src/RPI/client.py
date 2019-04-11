import socket
import base64
import sys
import io
import os
from PIL import Image
from array import array

HOST = '52.78.219.61'
PORT = 3078
#HOST = '3.18.202.129'
#PORT = 3333

s = socket.socket(socket.AF_INET, socket.SOCK_STREAM)
s.connect((HOST, PORT))

# image open and read
img = open('home.png',"rb")
b = base64.b64encode(img.read())
len_b= str(len(b))

# send len of string
s.send(len_b.encode('utf-8'))

# send image_str
s.sendall(b)

img.close()
s.close()