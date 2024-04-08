package com.example.restApi.global.exception;

public class GlobalException extends RuntimeException{

    public GlobalException(String resultCode, String msg){
        super("resultCode="+resultCode+";msg="+msg+";");
    }

}
