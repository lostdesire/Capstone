import os
import sys
import numpy as np
from random import shuffle

sys.path.append(os.path.dirname(os.path.abspath(os.path.dirname(__file__))))
from utils import pickleIO, params

def id2rgba(cluster_id, max_id, is_center=False):
    if not cluster_id:
        return 'rgba(255,255,255,1.0)'

    nRGB = (cluster_id / max_id) * (256**3-1)
    R = int(nRGB // (256**2))
    G = int((nRGB - R*(256**2)) // 256)
    B = int(nRGB % 256)
    A = 0.2 if is_center else 1.0
    return f'rgba({R},{G},{B},{A})'



def vis_hover_img_test(cluster_fname: str, vis_ratio=0.2):
    """ provides sources for visualize on Plotly
    Args:
        pkl_path ([str]): path to pickle file that consist of (features, cluster, fname)
    
    cluster
        fname, feature, text, cluster_id, similarity

    Returns:
        [type]: [description]
    """
    cluster = pickleIO.loadPkl(cluster_fname)
    print(len(cluster))
    sp = int(len(cluster) * vis_ratio)
    shuffle(cluster)
    cluster = cluster[:sp]

    max_id = len(set([ c['cluster_id'] for c in cluster ]))

    features = np.array([ c['feature'][0] for c in cluster ])
    fnames = [ c['fname'] for c in cluster ]
    cluster_ids = [ c['cluster_id'] for c in cluster ]
    colors = [ id2rgba(cid, max_id) for cid in cluster_ids]

    return {
        'feature': features,
        'fname': fnames,
        'cluster_id': cluster_ids,
        'color': colors,
    }
