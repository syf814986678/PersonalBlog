package com.shiyifan.vo;

import lombok.Data;

import java.util.Map;

@Data
public class Result {
    private int codeState;
    private Map<String,Object> msg;
}
