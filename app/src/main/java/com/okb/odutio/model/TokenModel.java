package com.okb.odutio.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by onurkilic on 08/05/2017.
 */

public class TokenModel {

    private String token;
    private String referenceCode;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getReferenceCode() {
        return referenceCode;
    }

    public void setReferenceCode(String referenceCode) {
        this.referenceCode = referenceCode;
    }
}
