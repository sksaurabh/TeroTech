
package com.telloquent.vms.model.settingsmodel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Theme {

    @SerializedName("identifier")
    @Expose
    private String identifier;
    @SerializedName("value")
    @Expose
    private Value value;

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public Value getValue() {
        return value;
    }

    public void setValue(Value value) {
        this.value = value;
    }

}
