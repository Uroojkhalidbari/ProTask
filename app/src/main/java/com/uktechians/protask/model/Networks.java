package com.uktechians.protask.model;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Networks {

    @SerializedName("applicable")
    @Expose
    private List<Applicable> applicable = null;

    public List<Applicable> getApplicable() {
        return applicable;
    }

    public void setApplicable(List<Applicable> applicable) {
        this.applicable = applicable;
    }

}
