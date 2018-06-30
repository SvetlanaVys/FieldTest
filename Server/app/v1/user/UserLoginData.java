package v1.user;

public class UserLoginData {

    public UserLoginData() {
    }

    public UserLoginData(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String email;
    public String password;
}