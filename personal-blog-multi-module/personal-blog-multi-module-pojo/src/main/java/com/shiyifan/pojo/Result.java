package com.shiyifan.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * @author ZouCha
 * @name Result
 * @date 2020-11-30 15:43
 **/
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Result {
    private int codeState;
    private String msg;
    private Map<String,Object> data;
}
