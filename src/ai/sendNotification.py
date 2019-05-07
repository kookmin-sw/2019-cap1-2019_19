# -*- coding: utf-8 -*- 

import pyfcm
import pymysql
import json
import requests

# AI 결과값: 이름 or Stranger

# ID 이용해서 Token 가져오기
def getToken(id):
	conn = pymysql.connect(host='####', user='###', password='####', db='##', charset='utf8')
	curs = conn.cursor()

	sql = "select token from Login where id=%s"
	curs.execute(sql,id)

	token = "empty"
	rows = curs.fetchall()
	if len(rows) == 1:
		token = rows[0][0]
		print(token)
	else:
		print("등록된 토큰이 없습니다")

	return token

# 알림 전송하기
def send(id, visitor):
	push_service = pyfcm.FCMNotification(api_key = "####")
	message_title = "방문자 알림"
	message_body = visitor + "님이 방문했습니다"
	registration_id = getToken(id)
	if registration_id != "empty":
		result = push_service.notify_single_device(registration_id=registration_id, message_title = message_title, message_body = message_body)
		print(result)
	else:
		print("등록된 기기가 없습니다.")
send('id','stranger')
