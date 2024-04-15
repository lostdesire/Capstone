package chatapp.ce2022;

public class Login {
    private String id;
    private String password;
    private String token;

    public Login(String id, String password){
        this.id = id;
        this.password = password;
    }

    public String getId(){
        return id;
    }
    public void setId(String id){
        this.id = id;
    }

    public String getPassword(){
        return password;
    }
    public void setPassword(String password){
        this.password = password;
    }
    public String getToken(){
        return token;
    }
    public void setToken(String token){
        this.token = token;
    }


}
