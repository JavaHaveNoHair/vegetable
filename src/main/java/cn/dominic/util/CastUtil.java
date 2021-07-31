package cn.dominic.util;

/**
 * @Description :
 * @Reference :
 * @Author : dominic
 * @CreateDate : 2021/7/31 18:52
 * @Modify:
 **/
public class CastUtil {

    /**
     * 汉语中数字大写
     */
    private static final String[] CN_UPPER_NUMBER = {"零", "壹", "贰", "叁", "肆", "伍", "陆", "柒", "捌", "玖"};
    /**
     * 汉语中货币单位大写，这样的设计类似于占位符
     */
    private static final String[] CN_UPPER_MONEYTRAY_UNIT = {"分", "角", "元", "拾", "佰", "仟", "万", "拾", "佰", "仟",
            "亿", "拾", "佰", "仟", "兆", "拾", "佰", "仟"};

    /**
     * 金额转中文大写
     *
     * @param money 金额
     * @return 中文大写金额
     */
    public static String moneyToUpperCase(String money) {
        char[] chars = money.toCharArray();
        StringBuilder sb = new StringBuilder();
        int numberIndex;
        int unitIndex;
        for (int i = 0; i < chars.length; i++) {
            if (chars[i] == '.') {
                continue;
            }
            numberIndex = Integer.parseInt(String.valueOf(chars[i]));
            unitIndex = i > chars.length - 3 ? chars.length - 1 - i : chars.length - 2 - i;

            if (chars[i] == '0') {
                //分为0或角分均为0,不拼接
                if (i == chars.length - 1 || i == chars.length - 2 && chars[chars.length - 1] == chars[i]) {
                    continue;
                }
                //个位数为零,拼接元  七十元,一百二十元,三百元...
                if (i == chars.length - 4) {
                    if (chars[i - 1] == '0') {
                        sb.deleteCharAt(sb.length() - 1);
                    }
                    sb.append(CN_UPPER_MONEYTRAY_UNIT[unitIndex]);
                    continue;
                }
                //前一位不为零,则拼接零  二百零三元   前一位若为零,则已拼接过,无需再次拼接 三千零四元
                if (chars[i - 1] != '0') {
                    sb.append(CN_UPPER_NUMBER[numberIndex]);
                    continue;
                }
                //角为零,分不为零,则拼接零  五元零七分
                if (i == chars.length - 2 && chars[chars.length - 1] != '0') {
                    sb.append(CN_UPPER_NUMBER[numberIndex]);
                }
                continue;
            }
            sb.append(CN_UPPER_NUMBER[numberIndex]);
            sb.append(CN_UPPER_MONEYTRAY_UNIT[unitIndex]);
        }

        if (chars[chars.length - 1] == chars[chars.length - 2] && chars[chars.length - 1] == '0') {
            sb.append("整");
        }
        return sb.toString();
    }
}
