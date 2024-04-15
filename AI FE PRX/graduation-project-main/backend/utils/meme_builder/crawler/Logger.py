import time
import os

class Logger:
    def __init__(self, dst:str=None, logfile:str='log.txt'):
        if dst and logfile:
            self.file_path = os.path.join(dst, logfile)
            if not os.path.isdir(dst):
                os.makedirs(dst)
        self.startTime = time.time()

    def write_header(self):
        header = time.strftime('[%d %H:%M:%S]\n', time.localtime())
        self.startTime = time.time()
        with open(self.file_path, 'a') as f:
            f.write(header)

    def write_log(self, strs):
        elapsed = self.get_elapsed()
        with open(self.file_path, 'a') as f:
            f.write(f'\telapsed: {elapsed} {strs}\n')
    
    def get_elapsed(self):
        elapsed = time.gmtime(time.time() - self.startTime)
        elapsed = time.strftime('%Hh %Mm %Ss: ', elapsed)
        return elapsed