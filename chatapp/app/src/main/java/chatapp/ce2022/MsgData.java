package chatapp.ce2022;

public class MsgData {
    public String uid;
    public String rid;
    public String msg;
    public String name;

    public MsgData(String uid, String rid, String msg, String name){
        this.uid = uid;
        this.rid = rid;
        this.msg = msg;
        this.name = name;
    }

    public String getUid() {return uid;}
    public void setUid(String uid) {this.uid = uid;}

    public String getRid() {return rid;}
    public void setRid(String rid) {this.rid = rid;}

    public String getMsg() {return msg;}
    public void setMsg(String msg) {this.msg = msg;}

    public String getName() {return name;}
    public void SetName(String Name) {this.name = name;}

}

