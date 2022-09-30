package model.rules;

import java.util.List;

public class Property {

    PropertyType propertyType;
    List<Object> args;
    
    public Property(PropertyType propertyType, List<Object> args) {
        this.propertyType = propertyType;
        this.args = args;
    }

    public String toString() {
        return "[" + propertyType.toString() + " " + args.toString() + "]";
    }

}
