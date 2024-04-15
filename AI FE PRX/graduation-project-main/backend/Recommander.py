import heapq
from typing import *
from tqdm import tqdm

from models import Encoders
from utils import pickleIO, params
from utils.translator import PapagoTranslator



clip = Encoders.MultiModalClip()
meme_features = pickleIO.loadPkl(params.feature_fname)
translator = PapagoTranslator(params.papago_user)
def recommand(msg: str) -> List[Tuple[str, float]]:
    #? 전역변수로 바꿔야함
    """ input message language must be English words. """
    print(f'ko: {msg}')
    msg = translator.run(msg)
    if not msg:
        return {'result': [{'image_url': '', 'text': 'run out of papago credit'}]}
    print(f'en: {msg}')
    msg_emb = clip.get_text_feature(msg)
    bag_of_memes = []
    for info in tqdm(meme_features, desc='recommand'):
        _, similiarity = clip.inference(info['feature'][:,:512], msg_emb)
        similiarity = similiarity[0]
        heapq.heappush(bag_of_memes, (-similiarity, info['image_url']))
    output = []
    for _ in range(params.topk):
        sim, img_url = heapq.heappop(bag_of_memes)
        output.append({
            'image_url': img_url,
            'similarity': -sim
        })
    del bag_of_memes
    return output

if __name__ == '__main__':
    print(recommand("화난 고양이"))