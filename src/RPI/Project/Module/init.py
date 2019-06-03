# -*- coding: utf-8 -*- 
import pymysql

HOST = '####'
PORT = 0000
USER = '####'
PASSWD = '####'
DB = '####'
CHARSET = 'utf8'

# id.txt에서 ID 가져오기
def getMyId():
	f = open('../Project/Info/id.txt','r')
	id = f.read()
	id = id.replace('\n','')
	f.close()
	return id

def connectDB():
	conn = pymysql.connect(host=HOST, port=PORT, user=USER, passwd=PASSWD, db=DB, charset=CHARSET)
	curs = conn.cursor()
	return conn, curs

def closeDB(conn):
	conn.close()

# DB에서 모니터 IP가져오기
def getMonitorIP(conn, curs, id):
	sql = "select m_ip from Login where id=%s"
	curs.execute(sql,id)
	rows = curs.fetchall()

	mIP = rows[0][0]
	return mIP

# DB에 저장된 내부 ip 업데이트
def updateDB(conn, curs, id, ip):
	sql = "update Login set i_ip = %s where id = %s"
	curs.execute(sql,(ip,id))
	conn.commit()

# init
def init(ip):
	id = getMyId()
	conn, curs = connectDB()
	m_ip = getMonitorIP(conn, curs, id)
	updateDB(conn, curs, id,ip)
	closeDB(conn)
	return id, m_ip
