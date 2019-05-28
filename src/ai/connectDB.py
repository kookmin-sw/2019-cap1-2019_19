import pymysql.cursors

def getBelong(id, visitor):
	print("방문자 그룹 설정 시작 ")

	conn = pymysql.connect(host="localhost", user="######", password="######", db="db")
	belong = "외부인"
	try:
		if(visitor == "외부인"):
			belong = "외부인"
		elif(visitor == "user"):
			belong == "user"
		else:
			with conn.cursor() as cursor:
				sql = 'SELECT * FROM Acquaintance WHERE id = %s AND name = %s'
				cursor.execute(sql, (id, visitor))
				if(cursor.rowcount == 1):
					res = cursor.fetchall()
					belong = res[0][3]
				elif(cursor.rowcount == 0):
					belong = "외부인"
	finally:
		conn.close()
	return belong
	print("방문자 그룹 설정 완료")

def updateHistory(id, timestamp, visitor, belong):
	print("방문기록 변경 시작")

	conn = pymysql.connect(host="localhost", user="######", password="######", db="db")
	try:
		with conn.cursor() as cursor:
			sql = 'UPDATE History SET name = %s, belong = %s WHERE id = %s AND rDate = %s'
			cursor.execute(sql, (visitor, belong, id, timestamp))
		conn.commit()
		print("변경된 row 수 : {}".format(cursor.rowcount))
	finally:
		conn.close()

	print("방문기록 변경 완료")