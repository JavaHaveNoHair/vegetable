package cn.dominic.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Description :
 * @Reference :
 * @Author : dominic
 * @CreateDate : 2021/7/27 21:07
 * @Modify:
 **/
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Config {

    private String printerName;

    private String printPath;

    private String templatePath;
}
