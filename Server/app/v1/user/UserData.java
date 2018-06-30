package v1.user;

import javax.persistence.*;

/**
 * Data returned from the database
 */
@Entity
@Table(name = "public.user")
public class UserData {

    public UserData() {
    }

    public UserData(String email, String password, String firstName, String lastName, String phone) {
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
    }

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    public Long id;
    @Column(unique = true)
    public String email;
    public String password;
    public String firstName;
    public String lastName;
    public String phone;
}
