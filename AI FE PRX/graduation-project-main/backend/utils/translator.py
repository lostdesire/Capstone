import os
import sys
import json
from typing import *
import urllib.request
from datetime import datetime

sys.path.append(os.path.dirname(os.path.abspath(os.path.dirname(__file__))))
from utils import params
from utils import papagoaccounts


class PapagoTranslator:
    def __init__(self, account_id: str):
        self.clnt_id = papagoaccounts.papago_accounts[account_id]['clnt_id']
        self.clnt_pw = papagoaccounts.papago_accounts[account_id]['clnt_pw']
        self.url = "https://openapi.naver.com/v1/papago/n2mt"
        self.prefix = "source=ko&target=en&text="
        self.monitor = Monitor(account_id)
    
    def _post(self, data):
        req = urllib.request.Request(self.url)
        req.add_header("X-Naver-Client-Id", self.clnt_id)
        req.add_header("X-Naver-Client-Secret", self.clnt_pw)
        res = urllib.request.urlopen(req, data=data.encode("utf-8"))
        rescode = res.getcode()
        return res.read().decode('utf-8') if rescode==200 else False
    
    def run(self, msg: str)-> str:
        if self.monitor.update(len(msg)):
            encText = urllib.parse.quote(msg)
            data = self.prefix + encText
            body = self._post(data)
            if body:
                result = json.loads(body)['message']['result']['translatedText']
                return result
        return ''


class Monitor:
    def __init__(self, account_id: str):
        self.monitor = os.path.join(params.papago_monitor_path, f'{account_id}.log')
        self.init_cnt()
        self.limit = 10000
    
    def init_cnt(self):
        print(self.monitor)
        if not os.path.isfile(self.monitor):
            self.cnt = 0
            self.dump()
            return
        else:
            with open(self.monitor, 'r') as f:
                prev_date, prev_cnt = f.read().split()
            if self.isToday(prev_date):
                self.cnt = int(prev_cnt)
            else:
                self.cnt = 0
    
    def getNow(self):
        return datetime.now().strftime("%Y%m%d,%H%M%S")

    def isToday(self, timestamp: str):
        return self.getNow().split(',')[0] == timestamp.split(',')[0]

    def dump(self):
        with open(self.monitor, 'w') as f:
            f.write(f'{self.getNow()} {str(self.cnt)}')

    def update(self, str_len: int) -> bool:
        if self.cnt + str_len < self.limit:
            self.cnt += str_len
            self.dump()
            return True
        else:
            return False




if __name__ == '__main__':
    account_id = 'sck12031203'
    translator = PapagoTranslator(account_id)
    print(translator.run('띵작'))
    monitor = Monitor(account_id)
    print(monitor.cnt)