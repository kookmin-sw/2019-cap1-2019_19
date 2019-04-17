import os

def streamingOn():
	os.system("sudo service uv4l_raspicam start")

def streamingOff():
	os.system("sudo pkill uv4l")