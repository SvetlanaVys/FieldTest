package v1.field;

import v1.option.OptionData;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Data returned from the database
 */
@Entity
@Table(name = "field")
public class FieldData {

    public FieldData() {
    }

    public FieldData(String label, String type, Boolean required, Boolean isactive, Boolean isDeleted) {
        this.label = label;
        this.type = type;
        this.required = required;
        this.isActive = isactive;
        this.isDeleted = isDeleted;
//        this.options = options;
    }

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    public Long id;
    public String label;
    public String type;
    public Boolean required;
    public Boolean isActive;
    public Boolean isDeleted;

//    @OneToMany(cascade=CascadeType.REMOVE, fetch = FetchType.EAGER )
//    public List<OptionData> options = new ArrayList<>();

}
