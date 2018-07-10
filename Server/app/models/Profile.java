package models;

import javax.persistence.*;

@Entity
@Table(name = "public.user")
public class Profile {
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
