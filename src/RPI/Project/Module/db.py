# -*- coding: utf-8 -*-
import pymysql

HOST = '52.78.219.61'
PORT = 3306
USER = 'monitor'
PASSWD = 'Kookmin1!'
DB = 'db'
CHARSET = 'utf8'

def connect():
	conn = pymysql.connect(host=HOST, port=PORT, user=USER, passwd=PASSWD, db=DB, charset=CHARSET)
	curs = conn.cursor()
	return conn, curs

def select(values):
	conn, curs = connect()
	return

def insert():
	return

def update():
	return

def close(conn):
	conn.close()
