import os
import re
import time
import pandas as pd
from tqdm import tqdm
from glob import glob
from PIL import Image
import requests as req
from io import BytesIO
from random import random
from Logger import Logger

import sys
sys.path.append(os.path.dirname(os.path.abspath(os.path.dirname(os.path.abspath(os.path.dirname(__file__))))))
import params


def preprocess(fnames:list, data_root:str, dataset_name:str):
    def get_hashtags(text: str)->list:
        tags = []
        tokens = text.split()
        for token in tokens:
            if token[0] == '#':
                tags.append(token.strip()[1:])
        return tags

    def get_urls(text: str)->list:
        urls = []
        tokens = text.split("'")
        for token in tokens:
            if 'http' in token:
                urls.append(token)
        return urls
    
    def get_puretext(text: str)->str:
        try:
            text = re.sub('\n', ' ', text)
            text = re.search('.+[ㄱ-ㅎ|ㅏ-ㅣ|가-힣|\w](?<!\d)', text).group().split()
            if text[-1] =='View':
                text.pop()
            text = ' '.join(text)
            text = text.replace(' The following media includes potentially sensitive content. Change settings', '')
            text = text.replace(' Show this thread', '')
            return text
        except:
            print(f'[Error]: {text}')
            return None

    tweets = []
    for file in fnames:
        df = pd.read_csv(file)
        user_csv = df['UserScreenName'].to_list()
        date_csv = df['Timestamp'].to_list()
        _url_csv = df['Image link'].to_list()
        _tag_csv = df['Embedded_text'].to_list()
        _text_csv = df['Embedded_text'].to_list()
        posturl_csv = df['Tweet URL'].to_list()

        url_csv = [get_urls(text) for text in _url_csv]
        tag_csv = [get_hashtags(text) for text in _tag_csv]
        text_csv = []
        for text in _text_csv:
            text = get_puretext(text)
            if text:
                text_csv.append(text)
        # text_csv = [get_puretext(text) for text in _text_csv]

        for user, date, urls, tags, text, posturl in zip(user_csv, date_csv, url_csv, tag_csv, text_csv, posturl_csv):
            tweets.append( (user, date, urls, tags, text, posturl) )

    dst = os.path.join(data_root, dataset_name)
    if not os.path.isdir(dst):
        os.makedirs(dst)
    if not os.path.isdir(os.path.join(dst, 'images')):
        os.makedirs(os.path.join(dst, 'images'))

    label = []
    idx = 0
    for user, date, urls, tags, text, posturl in tqdm(tweets):
        for url in urls:
            try:
                img = req.get(url).content
                img = Image.open(BytesIO(img)).convert('RGB')
                if not img or sum(img.size) < 128*2:
                    continue
                fname = os.path.join(dst, 'images', f'{idx}.jpg')
                img.save(fname)
                label.append( (f'./{idx}.jpg', user, date, tags, text, posturl, url) )
                time.sleep(random()*1.2)
                idx += 1
            except Exception as e:
                print(e)

    pd.DataFrame(label).to_csv(
        os.path.join(dst, 'label.csv'), 
        header = ['filename', 'user', 'date', 'tags', 'text', 'posturl', 'imgurl'],
        index = False
    )

if __name__ == '__main__':
    logger = Logger()
    fnames = glob(os.path.join(params.root_dir, 'dataset/scweet/meme_kor/output/filter*'))
    print(fnames)
    data_root = os.path.join(params.root_dir, 'dataset')
    dataset_name = '202104-202204_cchal'
    preprocess(fnames, data_root, dataset_name)
    print(logger.get_elapsed())