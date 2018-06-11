package com.example.ed3907en.spot2deez.spotify;

public class Token {

    public String access_token;
    public String token_type;
    public int expries_in;

    private long expiresAt;

    public boolean isValid(){
        return System.currentTimeMillis() < expiresAt;
    }

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public String getToken_type() {
        return token_type;
    }

    public void setToken_type(String token_type) {
        this.token_type = token_type;
    }

    public int getExpries_in() {
        return expries_in;
    }

    public void setExpries_in(int expries_in) {
        this.expries_in = expries_in;
    }
}
