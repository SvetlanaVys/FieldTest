package v1.option;

import v1.field.FieldData;

import javax.persistence.*;

@Entity
@Table(name = "option")
public class OptionData {
    public OptionData() {
    }

    public OptionData(String name, FieldData field) {
        this.name = name;
        this.field = field;
    }

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    public Long id;
    public String name;

    @ManyToOne(cascade=CascadeType.REMOVE)
    @JoinColumn(name = "field")
    public FieldData field;

}
