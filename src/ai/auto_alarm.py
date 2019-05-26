#-*- coding:utf-8-*-
import subprocess
import pymysql.cursors

while(1):
	res = subprocess.check_output(['./connect_docker.sh'],universal_newlines=True,shell=True)

	res = res.split('*')

	id = str(res[1])
	timestamp = str(res[3])
	visitor = str(res[5])
	print("id : {}, timestamp : {}, visitor : {}".format(id,timestamp,visitor))

	print("알림 전송")
	command = "python3 sendNotification.py " + visitor + " " + id
	subprocess.call(command, shell=True)
	print("알림 전송 완료")
	print("------------------------------------------------")
	print("방문기록 변경")
	timestamp = "2019-05-24 03:59:49.702502"

	conn = pymysql.connect(host="localhost", user="admin", password="Kookmin1!", db="db")
	try:
		belong = 'NULL'
		with conn.cursor() as cursor:
			sql='SELECT * FROM Acquaintance WHERE id=%s AND name=%s'
			cursor.execute(sql, (id, visitor))
			if(cursor.rowcount == 1):
				result = cursor.fetchall()
				belong = result[0][3]
			elif(cursor.rowcount == 0):
				belong = "외부인"
		with conn.cursor() as cursor:
			sql='UPDATE History SET name=%s, belong=%s WHERE rDate=%s'
			cursor.execute(sql, (visitor, belong, timestamp))
		conn.commit()
		print("변경된 row수 : {}".format(cursor.rowcount))
	finally:
		conn.close()

	print("방문기록 변경완료")