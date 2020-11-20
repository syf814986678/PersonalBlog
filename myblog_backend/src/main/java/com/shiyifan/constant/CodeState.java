package com.shiyifan.constant;
/**
 *
 * @author ZouCha
 * @name CodeState
 * @date 2020-11-20 15:04:37
 *
 **/
public class CodeState {
    //操作成功
    public static final int success=200;
    //登录失败
    public static final int loginError=201;
    //操作失败
    public static final int operationError=202;
    //token错误
    public static final int tokenError=701;
    //token过期
    public static final int tokenTimelimit=702;

    //捕获异常
    public static final int exception=999;
    //oss-callback异常
    public static final int ossException=998;
}
