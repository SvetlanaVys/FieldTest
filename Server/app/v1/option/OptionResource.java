package v1.option;

import v1.field.FieldData;

public class OptionResource {
    public String id;
    private String link;
    public String name;
    public FieldData field;

    public OptionResource() {
    }

    public OptionResource(String id, String link, String name, FieldData fieldData) {
        this.id = id;
        this.link = link;
        this.name = name;
        this.field = field;
    }

    public OptionResource(OptionData data, String link) {
        this.id = data.id.toString();
        this.link = link;
        this.name = data.name;
        this.field = data.field;
    }

    public String getId() {
        return id;
    }

    public String getLink() {
        return link;
    }

    public String getName() {
        return name;
    }

    public FieldData getField () { return field; }

}
