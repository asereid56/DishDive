// CountryResponse.java

// YApi QuickType插件生成，具体参考文档:https://plugins.jetbrains.com/plugin/18847-yapi-quicktype/documentation

package com.example.dishdive.model;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CountryResponse {
    @SerializedName("meals")
    private List<Country> areaNames;

    public List<Country> getAreaNames() { return areaNames; }
    public void setAreaNames(List<Country> value) { this.areaNames = value; }
}


