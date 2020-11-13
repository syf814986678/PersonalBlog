package com.shiyifan.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

/**
 * @author 81498
 */
@Data
public class Result {
    private int codeState;
    private Map<String,Object> msg;
}
