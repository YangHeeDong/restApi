package com.example.restApi.global.rsData;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatusCode;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class RsData<T> {

    private String resultCode;
    private String msg;
    private  T data;
    private Boolean isSuccess;
    private Boolean isFail;

    public static <T> RsData<T> of(String resultCode, String msg, T data){

        Boolean isSuccess = resultCode.startsWith("S-");

        return new RsData<>(resultCode, msg, data, isSuccess, !isSuccess);
    }

    public static <T> RsData<T> of(String resultCode, String msg){

        Boolean isSuccess = resultCode.startsWith("S-");

        return new RsData<>(resultCode, msg,null, isSuccess, !isSuccess);
    }

}
