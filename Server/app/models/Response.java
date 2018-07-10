package models;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;

@Entity
@Table(name = "response")
public class Response {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    public Long id;
    public String content;
    public Integer row;

    @ManyToOne(cascade=CascadeType.REMOVE)
//    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinColumn(name = "field")
    public Field field;
}
