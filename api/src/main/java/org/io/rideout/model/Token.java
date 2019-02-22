package org.io.rideout.model;

public class Token {

    public final static String BEARER = "bearer";

    private String token;
    private String type = BEARER;

    public Token() {}

    public Token(String token) {
        this.token = token;
    }

    public Token(String token, String type) {
        this(token);
        this.type = type;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
