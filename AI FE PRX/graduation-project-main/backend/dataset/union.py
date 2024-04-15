import os
import pandas as pd
from tqdm import tqdm


def get_info(data_root: str):
    return pd.read_csv(os.path.join(data_root, 'label.csv'))

def change_label(data_root, dst_root, prefix):
    if not os.path.isdir( os.path.join(dst_root, 'images') ):
        os.makedirs( os.path.join(dst_root, 'images') )

    label = get_info(data_root)
    for i in tqdm(range(len(label['filename'])), desc=prefix):
        prev = label['filename'][i]
        label['filename'][i] = prefix + '_' + prev[2:]
        os.system(f"cp {data_root}/images/{prev} {dst_root}/images/{label['filename'][i]}")
    return label

def union(data_root1, data_root2, prefix1, prefix2, dst_root):
    label1 = change_label(data_root1, dst_root, prefix1)
    label2 = change_label(data_root2, dst_root, prefix2)
    df = pd.concat([label1, label2])
    df.to_csv(os.path.join(dst_root, 'label.csv'), index=False)


if __name__ == '__main__':
    root_dir = '/home/seungchan/Desktop/Projs/graduation-project/backend/dataset'
    data_root1 = os.path.join( root_dir, '16-22_animalMeme' )
    data_root2 = os.path.join( root_dir, '202104-202204_cchal')
    dst_root = os.path.join( root_dir, 'en_ko' )
    union(data_root1, data_root2, 'en', 'ko', dst_root)