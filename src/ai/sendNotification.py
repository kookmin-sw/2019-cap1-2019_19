# -*- coding: utf-8 -*- 

import pyfcm
import pymysql
import sys

# AI 결과값: 이름 or Stranger

# ID 이용해서 Token 가져오기
def getToken(id):
	print("토큰 가져오기 시작")

	token = "empty"

	conn = pymysql.connect(host='localhost', user='######', password='######', db='db', charset='utf8')
	curs = conn.cursor()
	sql = "select token from Login where id=%s"
	curs.execute(sql,id)
	rows = curs.fetchall()

	if len(rows) == 1:
		token = rows[0][0]
		print("token : {}".format(token))
	else:
		print("등록된 토큰이 없습니다")

	print("토큰 가져오기 완료")
	return token

# 알림 전송하기
def send(id, visitor):
	print("알림 전송 시작")
	push_service = pyfcm.FCMNotification(api_key = "AAAAY8kbeM8:APA91bGq5bf_ALyJu6ADYnxna_Ggh-KRTzZUS-NY6c6CxuTxe7G-3zYnLnE_LFyO9kpAvrZPx8SuY7XM847_38uRX5JICC9B7Kr8pP-o_u0TbKB0JN4i7u5JMe9CbEwMJr5lmjaKrCTI")
	#message_title = "방문자 알림"
	message_body = visitor + "님이 방문했습니다"
	registration_id = getToken(id)

	data = {"title" : "BANG BANG 방문자 알림",
			"body" : message_body,}

	if registration_id != "empty":
		alarm = getStatus(id, visitor)
		if(alarm):
			result = push_service.notify_single_device(registration_id=registration_id, data_message=data)
			#result = push_service.notify_single_device(registration_id=registration_id, message_title = message_title, message_body = message_body, data_message = data)
			print("result : {}".format(result))
		else:
			print("알림 수신 여부를 꺼놨습니다.")
	else:
		print("등록된 기기가 없습니다.")

	print("알림 전송 완료")

#visitor 알림 여부 받아오기 
def getStatus(id, visitor):
	print("알림 여부 결정 시작")

	rows = []
	if(visitor == "user"):
		alarm = 0
		print("사용자, 알림 여부 : 0")
	elif(visitor == "외부인"):
		alarm = 1
		print("외부인, 알림 여부 : 1")
	else:	#visitor == acquaitance 
		conn = pymysql.connect(host='localhost', user='######', password='######', db='db', charset='utf8')
		curs = conn.cursor()
		sql = "select * from Acquaintance where id=%s and name=%s"
		curs.execute(sql, (id, visitor))
		rows = curs.fetchall()

		alarm = 1
		#지인에 등록되어있지 않을 경우 (ex. default)
		if len(rows) == 0:
			alarm = 1
			print("외부인, 알림 여부 : 1")
		#지인일 경우 사용자의 알림 수신 여부에 따라 발송 
		elif len(rows) == 1:
			alarm = rows[0][4]
			print("지인, 알림 여부 : {}".format(alarm))

	print("알림 여부 결정 완료")
	return alarm
