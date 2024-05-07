package com.butlergram.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CMRespDto<T> {

    private int code; // 1(성공), -1(실패) <- 응답할때의 코드
    private String message;
    private T data;

    public CMRespDto(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }
}
