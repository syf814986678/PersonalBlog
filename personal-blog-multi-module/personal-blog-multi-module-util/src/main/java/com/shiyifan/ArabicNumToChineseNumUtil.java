package com.shiyifan;

import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;

/**
 * @author ZouCha
 * @name ArabicNumToChineseNumUtil
 * @date 2020-12-01 15:27:33
 **/
@Component
@Log4j2
public class ArabicNumToChineseNumUtil {
    static char[] cnArr = new char[]{'一', '二', '三', '四', '五', '六', '七', '八', '九'};

    /**
     * 数字转换为中文数字， 这里只写到了万
     *
     * @return java.lang.String
     * @author ZouCha
     * @date 2020-11-20 15:31:42
     * @method arabicNumToChineseNum
     * @params [intInput]
     **/
    public String arabicNumToChineseNum(int intInput) {
        String si = String.valueOf(intInput);
        String sd = "";
        if (si.length() == 1) {
            if (intInput == 0) {
                return sd;
            }
            sd += cnArr[intInput - 1];
            return sd;
        } else if (si.length() == 2) {
            if ("1".equals(si.substring(0, 1))) {
                sd += "十";
                if (intInput % 10 == 0) {
                    return sd;
                }
            } else {
                sd += (cnArr[intInput / 10 - 1] + "十");
            }
            sd += arabicNumToChineseNum(intInput % 10);
        } else if (si.length() == 3) {
            sd += (cnArr[intInput / 100 - 1] + "百");
            if (String.valueOf(intInput % 100).length() < 2) {
                if (intInput % 100 == 0) {
                    return sd;
                }
                sd += "零";
            }
            sd += arabicNumToChineseNum(intInput % 100);
        } else if (si.length() == 4) {
            sd += (cnArr[intInput / 1000 - 1] + "千");
            if (String.valueOf(intInput % 1000).length() < 3) {
                if (intInput % 1000 == 0) {
                    return sd;
                }
                sd += "零";
            }
            sd += arabicNumToChineseNum(intInput % 1000);
        } else if (si.length() == 5) {
            sd += (cnArr[intInput / 10000 - 1] + "万");
            if (String.valueOf(intInput % 10000).length() < 4) {
                if (intInput % 10000 == 0) {
                    return sd;
                }
                sd += "零";
            }
            sd += arabicNumToChineseNum(intInput % 10000);
        }
        return sd;
    }
}
