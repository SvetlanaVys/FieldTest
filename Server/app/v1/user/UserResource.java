package v1.user;

/**
 * Resource for the API.  This is a presentation class for frontend work.
 */
public class UserResource {
    private String id;
    private String link;
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private String phone;

    public UserResource() {
    }

    public UserResource(String id, String link, String email, String password, String firstName, String lastName, String phone) {
        this.id = id;
        this.link = link;
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
    }

    public UserResource(UserData data, String link) {
        this.id = data.id.toString();
        this.link = link;
        this.email = data.email;
        this.password = data.password;
        this.firstName = data.firstName;
        this.lastName = data.lastName;
        this.phone = data.phone;
    }

    public String getId() {
        return id;
    }

    public String getLink() {
        return link;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPassword(String password) { this.password = password; }
}