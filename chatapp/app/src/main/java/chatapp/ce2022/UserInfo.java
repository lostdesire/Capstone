package chatapp.ce2022;

public class UserInfo {
    public String USER_ID;
    public String USER_NAME;
    public String STATUS_MSG;
    public String PROFILE_IMG;


    public String getUser_id() {
        return USER_ID;
    }

    public void setUser_id(String user_id) {
        this.USER_ID = user_id;
    }

    public String getUser_name() {
        return USER_NAME;
    }

    public void setUser_name(String user_name) {
        this.USER_NAME = user_name;
    }

    public String getStatus_msg() {
        return STATUS_MSG;
    }

    public void setStatus_msg(String status_msg) {
        this.STATUS_MSG = status_msg;
    }

    public String getProfile_img() {
        return PROFILE_IMG;
    }

    public void setProfile_img(String profile_img) {
        this.PROFILE_IMG = profile_img;
    }
}
