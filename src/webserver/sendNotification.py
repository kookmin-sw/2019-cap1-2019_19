# -*- coding: utf-8 -*- 

import pyfcm
import pymysql
import json
import requests

# AI 결과값: 이름 or Stranger

# ID 이용해서 Token 가져오기
def getToken():
	conn = pymysql.connect(host='####', user='###', password='####', db='##', charset='utf8')
	curs = conn.cursor()

	id = '##'
	sql = "select token from Login where id=%s"
	curs.execute(sql,id)

	token = ""
	rows = curs.fetchall()
	if len(rows) == 1:
		token = rows[0][0]
		print(token)
	else:
		print("등록된 토큰이 없습니다")

	return token

# 알림 전송하기
def send():
	push_service = pyfcm.FCMNotification(api_key = "####")
	message_title = "server2app"
	message_body = "test"
	registration_id = getToken()
	result = push_service.notify_single_device(registration_id=registration_id, message_title = message_title, message_body = message_body)
	print(result)

send()
