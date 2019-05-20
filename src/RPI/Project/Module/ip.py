from subprocess import check_output

def getMyIP():
        ip = check_output(['hostname', '-I']).decode('utf8')
        ip = ip.replace('\n','')
        ip = ip.replace(' ','')
        return ip
