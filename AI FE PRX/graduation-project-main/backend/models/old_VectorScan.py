import os
import sys
import torch
import numpy as np
from typing import *
from tqdm import tqdm
from random import shuffle
from numpy.linalg import norm
from collections import Counter, deque

sys.path.append(os.path.dirname(os.path.abspath(os.path.dirname(__file__))))
from utils import pickleIO, params

device = 'cuda' if torch.cuda.is_available else 'cpu'
print(device)

def normalize(vec: np.array):
    return vec / norm(vec, axis=-1, keepdims=True)


class Node:
    def __init__(self, fname: str, feature: np.array, text: str):
        self.fname = fname
        self.feature = feature
        self.text = text

class Cluster:
    def __init__(self, center_pt: np.array, elmts: list):
        self.group_id = None
        self.center_pt = center_pt
        self.elmts = elmts
        self.n_elmts = 1

    def merge(self, cluster):
        self.center_pt = self.center_pt * self.n_elmts + cluster.center_pt * cluster.n_elmts
        self.center_pt = normalize(self.center_pt)
        #! elmts를 set으로 구성한 경우 remove -> amortized O(1)
        #!         list로 구성한 경우 remove -> O(N)
        self.elmts.extend(cluster.elmts)
        self.n_elmts = self.n_elmts + cluster.n_elmts
    
    def discharge(self, eta: int):
        discharged : List[Node] = []
        # n_discharged = 0
        for child in self.elmts:
            if self.center_pt @ child.feature.T < eta:
                discharged.append(child)
                # n_discharged += 1
                self.n_elmts -= 1
                self.center_pt = self.center_pt * self.n_elmts - child.feature
                self.center_pt = normalize(self.center_pt)
        
        for child in discharged:
            self.elmts.remove(child)

        return [Cluster(child.feature, [child]) for child in discharged]


class VecScan:
    def __init__(self, pkl_data, epochs=6, eta=0.6, minPts=8):
        self.epochs = epochs
        self.eta = eta
        self.minPts = minPts
        self.pkl_data = pkl_data
        self.bigbang()
        
    
    def bigbang(self):
        """ Initialize each of features into "Cluster" instance whose len(element) is 1"""
        self.universe = deque()
        for data in self.pkl_data:
            feature = np.array(data['feature'])
            # feature: torch.Tensor = data['feature'].to(device)
            star = Node(fname=data['fname'], feature=feature, text=data['text'])
            self.universe.append(Cluster(center_pt = feature, elmts = [star]))
    
    def get_similarity(self, vec1: np.array, vec2: np.array):
        # res =  vec1 @ vec2.T
        # return res
        return vec1 @ vec2.T
    
    @torch.no_grad()
    def get_nearest(self, parent):
        parent = torch.tensor(parent).to(device)
        bunch = torch.tensor([c.center_pt[0] for c in self.universe]).to(device)
        sim, child_idx = torch.max(self.get_similarity(parent, bunch), -1)
        return sim, self.universe[child_idx]

    def cluster(self):
        progress = len(self.universe)
        curr_epoch = 0
        while progress / len(self.universe) * 100 > 20:
            progress = 0
            curr_epoch += 1
            len_prev = len(self.universe)
            iteration = 0
            with tqdm(total=len_prev) as pbar:
                while iteration < len_prev:
                    parent = self.universe.popleft()
                    # iteration += 1
                    # pbar.update(1)
                    sim, child = self.get_nearest(parent.center_pt)

                    if  sim >= self.eta and (len(parent.elmts) + len(child.elmts) < len(self.universe) / 20) :
                        parent.merge(child)
                        self.universe.remove(child)
                        self.universe.extend( parent.discharge(self.eta) )
                        progress += 1
                        iteration += 1
                        pbar.update(1)
                        self.universe.append(parent)
                        # shuffle(self.universe)
                print(f'{progress} / {len(self.universe)} = {progress/len(self.universe)*100: .2f}%')
        
        # while progress / len(self.universe) * 100 > 20:
        #     progress = 0
        #     curr_epoch += 1
        #     for parent in tqdm(self.universe, desc=f'epoch {curr_epoch}'):
        #         nearest = {'sim': 0, 'cluster': None}
        #         for child in self.universe:
        #             if child is not parent:
        #                 curr_sim = self.get_similarity(parent.center_pt, child.center_pt)
        #                 if curr_sim > nearest['sim']:
        #                     nearest['sim'], nearest['cluster'] = curr_sim, child
        #         sim, child = nearest['sim'], nearest['cluster']
        #         # print(len(self.universe))
        #         if  sim >= self.eta and (len(parent.elmts) + len(child.elmts) < len(self.universe) / 20) :
        #             parent.merge(child)
        #             self.universe.remove(child)
        #             self.universe.extend( parent.discharge(self.eta) )
        #             progress += 1
        #             shuffle(self.universe)
        #     print(f'{progress} / {len(self.universe)} = {progress/len(self.universe)*100: .2f}%')
        
        cnt = Counter()
        for cluster in self.universe:
            cnt.update( [len(cluster.elmts)] )
        prn = []
        for k, v in cnt.items():
            prn.append((k, v))
        prn.sort()
        for idx, (k,v) in enumerate(prn):
            print(f'{idx+1}: \t {k},\t {v}')

        return self.cluster_labeling()
    
    def cluster_labeling(self) -> List[dict]:
        output = []
        iteration = 1
        for cluster in self.universe:
            if len(cluster.elmts) < self.minPts:
                cluster_id = 0
            else:
                cluster_id = iteration
                iteration += 1
            
            for node in cluster.elmts:
                similarity = self.get_similarity(cluster.center_pt, node.feature)
                output.append({
                    'fname': node.fname,
                    'feature': node.feature,
                    'text': node.text,
                    'cluster_id': cluster_id,
                    'similarity': similarity,
                })
        return output


if __name__ == '__main__':
    pkl_data = pickleIO.loadPkl(pkl_fname= params.feature_fname)[:5000]
    output = VecScan(pkl_data).cluster()
    pickleIO.savePkl(data=output, pkl_fname='cluster.pkl')