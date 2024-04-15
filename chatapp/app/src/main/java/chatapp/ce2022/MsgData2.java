package chatapp.ce2022;

public class MsgData2 {
    public String type;
    public String sender;
    public String content;
    public String name;
    public String time;
    public String iv;



    public int position = 0;

    public MsgData2(String type, String sender, String name, String content, String time, String iv){
        this.type = type;
        this.sender = sender;
        this.name = name;
        this.content = content;
        this.time = time;
        this.iv = iv;
    }

    public String getType() {return type;}
    public void setType(String type) {this.type = type;}

    public String getSender() {return sender;}
    public void setSender(String sender) {this.sender = sender;}

    public String getContent() {return content;}
    public void setContent(String content) {this.content = content;}

    public String getName() {return name;}
    public void setName(String name) {this.name = name;}

    public String getTime() {return time;}
    public void setTime(String time) {this.time = time;}

    public String getIv() {return iv;}
    public void setIv(String iv) {this.iv = iv;}

    public int getPosition() {return position;}
    public void setPosition(int position) {this.position = position;}
}
