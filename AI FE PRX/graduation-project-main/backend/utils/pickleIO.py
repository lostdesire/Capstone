import os
import torch
import pickle
from typing import *
from tqdm import tqdm

import sys
sys.path.append(os.path.dirname(os.path.abspath(os.path.dirname(__file__))))
from models import Encoders
from utils import dataloader, params

# raspi_prefix = 'http://ark10806.iptime.org/Projs/graduation-project/16-22_animalMeme/images/'
# raspi_prefix = 'http://ark10806.iptime.org/Projs/graduation-project/202104-202204_cchal/images/'
raspi_prefix = 'http://ark10806.iptime.org/Projs/graduation-project/en_ko/images/'
def saveMultimodalFeature(data_root: str, feature_fname: str):
    print(f'Data Root: {data_root}')
    clip = Encoders.MultiModalClip()
    dataset = dataloader.ImageTextPair(data_root)
    output = []
    failed = 0
    for image, label in tqdm(dataset, desc='Extr features'):
        try:
            output.append({
                'fname':    label['fname'],
                'text':     label['text'],
                'feature':  clip.get_multimodal_feature(image, label['text']),
                'image_url': raspi_prefix + label['fname']
            })
        except Exception as e:
            failed += 1
    print(f'loss rate: {failed / len(dataset) * 100: .2f}%')
    del clip
    savePkl(output, feature_fname)

# def saveImageFeature(data_root: str, feature_fname: str):
#     clip = Encoders.MultimodalClip()
#     dataset = dataloader.ImageSinglePair(data_root)
#     output = []
#     failed = 0
#     for image, label in tqdm(dataset, desc='Extr features'):
#         try:
#             output.append({
#                 'fname':    label['fname'],
#                 'text':     label['text'],
#                 'feature':  clip.get_image_features(image, label['text']),
#                 'image_url': raspi_prefix + label['fname']
#             })
#         except Exception as e:
#             failed += 1
#     print(f'loss rate: {failed / len(dataset) * 100: .2f}%')
#     del clip
#     savePkl(output, feature_fname)

def savePkl(data, pkl_fname: str):
    pkl_path = os.path.join(params.res_path, pkl_fname)
    print(f'saving {pkl_path}...', end='  ')
    with open(pkl_path, 'wb') as f:
        pickle.dump(data, f)
    print(f'complete! {len(data)}')
    

def loadPkl(pkl_fname: str)-> List[torch.Tensor]:
    pkl_path = os.path.join(params.res_path, pkl_fname)
    print(f'loading {pkl_path}..', end='  ')
    with open(pkl_path, 'rb') as f:
        data = pickle.load(f)
    print(f'[complete: {len(data)}]')
    return data




if __name__ == '__main__':
    data_root = params.data_root
    feature_fname = params.feature_fname

    saveMultimodalFeature(data_root, feature_fname)