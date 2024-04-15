# discord 라이브러리 사용 선언
import discord

def read_token(token_path):
    with open(token_path, 'r') as f:
        return f.read()

class chatbot(discord.Client):
    # 프로그램이 처음 실행되었을 때 초기 구성
    async def on_ready(self):
        # 상태 메시지 설정
        # 종류는 3가지: Game, Streaming, CustomActivity
        game = discord.Game("내용")
        # 계정 상태를 변경한다.
        # 온라인 상태, game 중으로 설정
        await client.change_presence(status=discord.Status.online, activity=game)
        # 준비가 완료되면 콘솔 창에 "READY!"라고 표시
        print("READY")
    # 봇에 메시지가 오면 수행 될 액션
    async def on_message(self, message):
        # SENDER가 BOT일 경우 반응을 하지 않도록 한다.
        if message.author.bot:
            return None

        if message.content == "!바보":
            # 현재 채널을 받아옴
            channel = message.channel
            # 답변 내용 구성
            msg = "너도 바보"
            # msg에 지정된 내용대로 메시지를 전송
            embed = discord.Embed(title="메인 제목", description="설명", color=0x62c1cc) # Embed의 기본 틀(색상, 메인 제목, 설명)을 잡아줍니다
            embed.set_footer(text="하단 설명") # 하단에 들어가는 조그마한 설명을 잡아줍니다
            embed.set_image(url="http://ark10806.iptime.org/Projs/graduation-project/en_ko/images/en_13261.jpg")
            await channel.send(msg)
            await channel.send(embed=embed)
            return None


# 프로그램이 실행되면 제일 처음으로 실행되는 함수
if __name__ == "__main__":
    # 객체를 생성
    client = chatbot()
    # TOKEN 값을 통해 로그인하고 봇을 실행
    token_path = './discord_token.key'
    token = read_token(token_path)
    print(token)
    client.run(token)