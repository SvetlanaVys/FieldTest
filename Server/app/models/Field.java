package models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "field")
public class Field {
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    public Long id;
    public String label;
    public String type;
    public Boolean required;
    public Boolean isActive;
    public Integer rowNumber;
}
