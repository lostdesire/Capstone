package chatapp.ce2022;

import com.google.gson.annotations.SerializedName;

public class Post {
    @SerializedName("id")
    private String id;
    @SerializedName("password")
    private String password;
    @SerializedName("name")
    private String name;
    @SerializedName("msg")
    private String msg;
    @SerializedName("img")
    private String img;
    private String token;

    public Post(String id){
        this.id = id;
        this.password = "";
    }


    public String getId() {
        return id;
    };
    public void setId(String id) {
        this.id = id;
    };
    public String getPassword() {
        return password;
    };
    public void setPassword(String password) {
        this.password = password;
    };
    public String getName() {
        return name;
    };
    public void setName(String name) {
        this.name = name;
    };
    public String getMsg() {
        return msg;
    };
    public void setMsg(String msg) {
        this.msg = msg;
    };
    public String getImg() {
        return img;
    };
    public void setImg(String img) {
        this.img = img;
    };
    public String getToken() {return token;}
    public void setToken(String token) {this.token = token;}



};

