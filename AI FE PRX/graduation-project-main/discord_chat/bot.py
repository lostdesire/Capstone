import discord
import requests

def read_token(token_path):
    with open(token_path, 'r') as f:
        return f.read()

class chatbot(discord.Client):
    async def on_ready(self):
        game = discord.Game("내용")
        await client.change_presence(status=discord.Status.online, activity=game)
        print("READY")

    async def on_message(self, message):
        if message.author.bot:
            return None

        if message.content[0] == '!':
            req_json = requests.get(f"https://pred.ga:5000/meme?message={message.content[1:]}").json()
            channel = message.channel
            # msg = "너도 바보"
            for i in range(k):
                embed = discord.Embed(title="", description=f'{message.content[1:20]}에 대한 응답{i+1}', color=0x62c1cc) # Embed의 기본 틀(색상, 메인 제목, 설명)을 잡아줍니다
                embed.set_footer(text="하단 설명") # 하단에 들어가는 조그마한 설명을 잡아줍니다
                embed.set_image(url=req_json['result'][i]['image_url'])
                await channel.send(embed=embed)
            return None


if __name__ == "__main__":
    k = 5
    client = chatbot()
    token_path = './discord_token.key'
    token = read_token(token_path)
    print(token)
    client.run(token)