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
public class Vegetables {

    private Integer id;

    private String name;

    private String unit;

    private Double number;

    private Double price;

    private boolean select;

    private Checkbox cb = new Checkbox();

}
