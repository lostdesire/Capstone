import os
from Scweet.scweet import scrape
from Scweet.user import get_user_information, get_users_following, get_users_followers
from Logger import Logger
import sys
sys.path.append(os.path.dirname(os.path.abspath(os.path.dirname(__file__))))
import params

def get_twits(year: int, month: int, logger: Logger, dst: str, keyword: str):
    try:
        logger.write_header()
        data = scrape(
            words=f'filter:links {keyword}',
            since=f"{year}-{month:02}-01",
            until=f"{year}-{month+1:02}-01",

            # since="2021-04-01",
            # until="2022-04-22",
            from_account = None,
            interval=1,
            headless=True, 
            display_type="Top",
            lang='ko', 
            save_images=False,
            resume=False, 
            filter_replies=True, 
            proximity=False
        )
        csv_path = os.path.join(dst, 'outputs')
        if not os.path.isdir(csv_path):
            os.makedirs(csv_path)
        data.to_csv(os.path.join(csv_path, f'WebCrawl_{year}-{month:02}.csv'), header=False, index=False)
        log = f'[{"Complete":^10}] {year} {month} ~ {month+1}'
        logger.write_log(log)
    except Exception as e:
        print(e)
        log = f'[{"Failed":^10}] {year} {month} ~ {month+1}\n\t\t{e}'
        logger.write_log(log)
        get_twits(year, month, logger, dst, keyword)

if __name__ == '__main__':
    # dst = './outputs/daily_test'
    expr_name = 'meme_kor'
    dst = os.path.join(params.scweet_path, expr_name)
    if not os.path.isdir(dst):
        os.makedirs(dst)
    logfile = 'log.txt'
    keyword = '짤방'
    logger = Logger(dst, logfile)

    for year in range(2022, 2022+1):
    #for year in range(2021, 2022+1):

        #for month in range(1,12):
        for month in range(3,6):
            if year == 2021 and month < 4:
                continue
            if year == 2022 and month == 5:
                break
            get_twits(year, month, logger, dst, keyword)
