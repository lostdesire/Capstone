package chatapp.ce2022;

public class enterData {
    public String type;
    public String content;
    public String rsa;


    public enterData(String type, String content, String rsa ){
        this.type = type;

        this.rsa = rsa;
        this.content = content;

    }

    public String getType() {return type;}
    public void setType(String type) {this.type = type;}


    public String getContent() {return content;}
    public void setContent(String content) {this.content = content;}

    public String getRsa() {return rsa;}
    public void setRsa(String rsa) {this.rsa = rsa;}

}
