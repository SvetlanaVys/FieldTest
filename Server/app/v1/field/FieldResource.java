package v1.field;

import v1.option.OptionData;

import java.util.List;

/**
 * Resource for the API.  This is a presentation class for frontend work.
 */
public class FieldResource {
    public String id;
    private String link;
    public String label;
    public String type;
    public Boolean required;
    public Boolean isActive;
    public Boolean isDeleted;
//    public List<OptionData> options;

    public FieldResource() {
    }

    public FieldResource(String id, String link, String label, String type, Boolean required, Boolean isactive, Boolean isDeleted) {
        this.id = id;
        this.link = link;
        this.label = label;
        this.type = type;
        this.required = required;
        this.isActive = isactive;
        this.isDeleted = isDeleted;
//        this.options = options;
    }

    public FieldResource(FieldData data, String link) {
        this.id = data.id.toString();
        this.link = link;
        this.label = data.label;
        this.type = data.type;
        this.required = data.required;
        this.isActive = data.isActive;
        this.isDeleted = data.isDeleted;
//        this.options = data.options;
    }

    public String getId() {
        return id;
    }

    public String getLink() {
        return link;
    }

    public String getLabel() {
        return label;
    }

    public String getType () { return type; }

    public Boolean getRequired() {
        return required;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public Boolean getIsDeleted() {
        return isDeleted;
    }

}