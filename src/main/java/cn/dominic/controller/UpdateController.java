package cn.dominic.controller;

import cn.dominic.pojo.Vegetables;
import cn.dominic.service.VegetablesService;
import cn.dominic.util.AlertUtil;
import de.felixroske.jfxsupport.FXMLController;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.springframework.beans.factory.DisposableBean;
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
public class UpdateController implements DisposableBean {

    public Integer id;

    @FXML
    public TextField name;

    @FXML
    public TextField unit;

    @FXML
    public TextField number;

    @FXML
    public TextField price;

    @FXML
    private Button btnUpdate;

    @FXML
    private Button btnClose;

    @Resource
    VegetablesService vegetablesService;

    public void update() {
        if (StringUtils.isEmpty(name.getText())) {
            AlertUtil.showErrorAlert("修改失败", "名称不能为空，请填写", false);
            return;
        }
        Vegetables vegetables = new Vegetables();
        vegetables.setName(name.getText());
        vegetables.setUnit(unit.getText());
        vegetables.setNumber(number.getText() == null ? null : Double.parseDouble(number.getText()));
        vegetables.setPrice(price.getText() == null ? null : Double.parseDouble(price.getText()));
        vegetables.setId(id);
        vegetablesService.update(vegetables);
        Stage stage = (Stage) btnUpdate.getScene().getWindow();
        stage.close();
        AlertUtil.showInfoAlert("修改成功", "修改成功，请刷新", false);
    }

    public void close() {
        Stage stage = (Stage) btnClose.getScene().getWindow();
        stage.close();
    }

    @Override
    public void destroy() throws Exception {

    }
}
