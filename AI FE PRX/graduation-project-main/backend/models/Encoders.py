from pyexpat import features
import re
from symbol import with_stmt
import torch
from typing import *
from PIL import Image
from transformers import CLIPProcessor, CLIPModel


class MultiModalClip:
    """ generates MultiModal Feature.
    """

    def __init__(self):
        self.device = 'cuda' if torch.cuda.is_available() else 'cpu'
        self.clip = CLIPModel.from_pretrained('openai/clip-vit-base-patch32').to(self.device)
        self.processor = CLIPProcessor.from_pretrained('openai/clip-vit-base-patch32')
        self.mark = re.compile('[^0-9|a-z|A-Z|\s|\.|\^|\$|\*|\+|\?|!|\\|@|#|\~|·_;:,|/|\-|>|<|%|…|─|&|\'|\(|\)]')
        self.web = re.compile("(ftp:\/\/|www\.|https?:\/\/){1}[a-zA-Z0-9u00a1-\uffff0-]{2,}\.[a-zA-Z0-9u00a1-\uffff0-]{2,}(\S*)")
        
    def normalize(self, feature: torch.Tensor)-> torch.Tensor:
        return feature / feature.norm(dim=-1, keepdim=True)

    def text_preprocess(self, text: str) -> str:
        masks = (
            ('[^0-9|a-z|A-Z|\s|\.|\^|\$|\*|\+|\?|!|\\|@|#|\~|·_;:,|/|\-|>|<|%|…|─|&|\'|\(|\)]', ''),
            ('(ftp:\/\/|www\.|https?:\/\/){1}[a-zA-Z0-9u00a1-\uffff0-]{2,}\.[a-zA-Z0-9u00a1-\uffff0-]{2,}(\S*)', '<url>'),
            ('[\.]+', '.'),
            ('[,]+', ','),
            ('[~]+', '~'),
            ('[!]+', '!'),
            ('@\w+', '@someone'))
        for mask, stamp in masks:
            text = re.sub(mask, stamp, text)
        return text[:250]

    @torch.no_grad()
    def get_text_feature(self, texts: List[str]) -> torch.Tensor:
        if texts is None: return None
        if not isinstance(texts, list): texts = [texts]
        texts = [self.text_preprocess(s) for s in texts]
        texts = self.processor(text=texts, return_tensors='pt', padding=True).to(self.device)
        txt_emb = self.clip.get_text_features(**texts)
        return self.normalize(txt_emb)

    @torch.no_grad()
    def get_image_feature(self, image: Image) -> torch.Tensor:
        if image is None: return None
        image = self.processor(images=image, return_tensors='pt', padding=True).to(self.device)
        img_emb = self.clip.get_image_features(**image)
        return self.normalize(img_emb)

    @torch.no_grad()
    def get_multimodal_feature(self, image: Image=None, text: str=None, device: str='cpu') -> torch.Tensor:
        assert image is not None or text is not None, "needs <PIL.Image> or <str>"
        img_emb = self.get_image_feature(image).to(device)
        txt_emb = self.get_text_feature(text).to(device)
        if image is not None and text is not None:
            return self.normalize(torch.cat( (img_emb, txt_emb), dim=-1 ))
        if image is not None:
            return img_emb
        if text is not None:
            return txt_emb
    
    @torch.no_grad()
    def inference(self, feature: torch.Tensor, msg_emb: List[str])-> torch.Tensor:
        img_emb = self.normalize(feature[:,:512]).to(self.device)
        img_sim = img_emb @ msg_emb.T
        # if len(feature) == 1024:
        if feature.shape[1] == 1024:
            txt_emb = self.normalize(feature[:,512:]).to(self.device)
            txt_sim = txt_emb @ msg_emb.T
            similiarity = (img_sim + txt_sim) / 2
        else:
            similiarity = img_sim
        _, pred = torch.max(similiarity, -1)
        return pred.to('cpu'), list(map(float, similiarity.to('cpu')[0]))
    

if __name__ == '__main__':
    # strs = 'TC Sparkler Update Bracket play, Day 1 Shoutout to our two studs in the circle, @JaydenHeavener & @myaholt25 for throwing a combined no-hitter in our 9-0 win vs TN Mojo Mobley Bracket game #2 tomorrow @ 12p vs TN Mojo Hughes #boltsboom #boltsPremier2024 #L1nked'
    strs = 'abcd'
    img = Image.open('/home/seungchan/Desktop/Projs/graduation-project/dataset/2022_02_all/images/2021_0.jpg')
    clip = MultiModalClip()
    # multi_emb = clip.get_multimodal_feature(image=img, text=strs)
    # print(multi_emb)
    img_emb = clip.get_image_feature(image=img)
    print(img_emb)