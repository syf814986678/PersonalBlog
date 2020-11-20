package com.shiyifan.vo;

import lombok.Data;

import java.util.Map;

/**
 *
 * @author ZouCha
 * @name Result
 * @date 2020-11-20 15:35:32
 *
 **/
@Data
public class Result {
    private int codeState;
    private Map<String,Object> msg;
}
