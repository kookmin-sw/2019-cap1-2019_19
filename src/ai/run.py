import subprocess
import sendNotification as s
import connectDB as c

while(1):
	print("시작")
	res = subprocess.check_output(['./connect_docker.sh'],universal_newlines=True,shell=True)
	print(res)

	res = res.split('*')
	id = str(res[1])
	timestamp = str(res[3])
	visitor = str(res[5])
	print("id : {}, timestamp : {}, visitor : {}".format(id, timestamp, visitor))
	
	if(visitor == "stranger"):
		visitor = "외부인"
		
	s.send(id, visitor)
	belong = c.getBelong(id, visitor)
	c.updateHistory(id, timestamp, visitor, belong)

	print("완료")

