package cn.dominic.controller;

import cn.dominic.VegetableApp;
import cn.dominic.pojo.Vegetables;
import cn.dominic.service.VegetablesService;
import cn.dominic.util.AlertUtil;
import cn.dominic.view.ManageView;
import de.felixroske.jfxsupport.FXMLController;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;

/**
 * @Description :
 * @Reference :
 * @Author : dominic
 * @CreateDate : 2021/7/28 21:54
 * @Modify:
 **/
@FXMLController
public class InsertController {

    @FXML
    private TextField name;

    @FXML
    private TextField unit;

    @FXML
    private TextField number;

    @FXML
    private TextField price;

    @Resource
    VegetablesService vegetablesService;

    public void insert() {
        if (StringUtils.isEmpty(name.getText())) {
            AlertUtil.showErrorAlert("新增失败", "名称不能为空，请填写", false);
            return;
        }
        Vegetables vegetables = new Vegetables();
        vegetables.setName(name.getText());
        vegetables.setUnit(unit.getText());
        vegetables.setNumber(StringUtils.isEmpty(number.getText()) ? null : Double.parseDouble(number.getText()));
        vegetables.setPrice(StringUtils.isEmpty(price.getText()) ? null : Double.parseDouble(price.getText()));
        vegetablesService.insert(vegetables);

        VegetableApp.showView(ManageView.class);
        AlertUtil.showInfoAlert("新增成功", "新增成功，请刷新", false);
    }

    public void back() {
        VegetableApp.showView(ManageView.class);
    }
}
