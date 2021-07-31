package cn.dominic.pojo;

import cn.dominic.util.Checkbox;
import lombok.Data;

/**
 * @Description :
 * @Reference :
 * @Author : dominic
 * @CreateDate : 2021/7/27 21:07
 * @Modify:
 **/
@Data
public class PrintData {

    private Integer printId;

    private String printName;

    private String printUnit;

    private String printNumber;

    private String printPrice;

    private String printTotalPrice;

    private boolean select;

    private Checkbox cb = new Checkbox();

}
