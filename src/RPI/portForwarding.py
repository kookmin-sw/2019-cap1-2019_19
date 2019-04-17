from subprocess import check_output
import os

# 내부 ip 주소
ip = check_output(['hostname', '-I']).replace('\n','')
ip = ip.replace(' ','')

# port: 8080, 스트리밍 서버
os.system("upnpc -a " + ip + " 8080 8080 tcp")