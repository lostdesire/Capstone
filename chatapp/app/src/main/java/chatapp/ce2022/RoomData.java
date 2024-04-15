package chatapp.ce2022;

public class RoomData {
    private String uid;
    private String rid;
    private String cuid;
    private String rsa;


    public RoomData(String uid, String rid){
        this.uid = uid;
        this.rid = rid;
        this.cuid = "NULL";
        this.rsa = "NULL";

    }

    public RoomData(String uid, String rid, String cuid, String rsa){
        this.uid = uid;
        this.rid = rid;
        this.cuid = cuid;
        this.rsa = rsa;

    }



    public String getUid() {return uid;}
    public void setUid(String uid) {this.uid = uid;}

    public String getRid() {return rid;}
    public void setRid(String rid) {this.rid = rid;}

    public String getCuid() {return cuid;}
    public void setCuid(String cuid) {this.cuid = cuid;}

    public String getRsa() {return rsa;}
    public void setRsa(String rsa) {this.rsa = rsa;}




}

