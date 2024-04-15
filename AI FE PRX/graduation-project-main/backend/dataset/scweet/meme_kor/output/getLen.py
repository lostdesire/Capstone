import os
from typing import *
import pandas as pd
from PIL import Image

class ImageTextPair:
    def __init__(self, data_root: str):
        df = pd.read_csv(os.path.join(data_root, 'label.csv'))
        self.data_root = data_root
        _text = df['text'].to_list()
        _fname = df['filename'].to_list()
        self.label = [{'fname': fname, 'text': txt} for fname, txt in zip(_fname, _text)]
        del df, _text, _fname
    
    def __len__(self):
        return len(self.label)
    
    def __getitem__(self, idx: int) -> Tuple:
        image = Image.open(os.path.join(self.data_root, 'images', self.label[idx]['fname']))
        label = self.label[idx]
        return image, label


if __name__ == '__main__':
    data_root = '/home/seungchan/Desktop/Projs/graduation-project/dataset/202104-202204_cchal'
    dataset = ImageTextPair(data_root)
    output = 0
    for img, label in dataset:
        output += len(label['text'])
    print(output)