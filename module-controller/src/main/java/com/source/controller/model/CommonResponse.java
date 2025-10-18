package com.source.controller.model;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class CommonResponse<T> implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private boolean success;
    private String message;
    private Integer code;
    private long timestamp = System.currentTimeMillis();
    private T result;
}
