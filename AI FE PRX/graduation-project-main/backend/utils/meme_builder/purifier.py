"""
1. Feature Extraction -> Pickle
2. VectorScan(Features) -> Meme
3. build dataset
"""
import os
from typing import *
from PIL import Image
from tqdm import tqdm

import sys
sys.path.append(os.path.dirname(os.path.abspath(os.path.dirname(os.path.abspath(os.path.dirname(__file__))))))
from models import Encoders
from utils import pickleIO, params

def getMeme(feature_fname):
    clip = Encoders.MultiModalClip()
    pkl_data = pickleIO.loadPkl(feature_fname)
    output = []
    cat_emb = clip.get_text_feature(params.categories)
    for data in tqdm(pkl_data, desc=f'fltr {params.categories[0]}'):
        pred, prob = clip.inference(data['feature'][:,:512], cat_emb)
        if pred == 0: # Meme category
            #! url 추가해야함 (raspi)
            output.append({
                'fname':    data['fname'],
                'text':     data['text'],
                'feature':  data['feature'],
                'prob':     prob[0]
            })
    
    pickleIO.savePkl(output, params.meme_feature_path)
    print(f'len(meme): {len(output)}')

def visMeme():
    meme = pickleIO.loadPkl(params.meme_feature_path)
    for data in tqdm(meme, desc=f'vis {params.catname[0]}'):
        img = Image.open(os.path.join(params.data_root, 'images', data['fname'])).resize((256,256))
        meme_path = os.path.join(params.res_path, 'meme')
        if not os.path.isdir(meme_path):
            os.makedirs(meme_path)
        img.save(os.path.join(meme_path, f'{data["prob"]*100:.2f}.jpeg' ))


if __name__ == '__main__':
    getMeme(params.feature_fname)
    visMeme()