package cn.dominic.controller;

import cn.dominic.VegetableApp;
import cn.dominic.pojo.Vegetables;
import cn.dominic.service.VegetablesService;
import cn.dominic.util.AlertUtil;
import cn.dominic.util.SpringUtil;
import cn.dominic.view.InsertView;
import cn.dominic.view.RunningView;
import de.felixroske.jfxsupport.FXMLController;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.springframework.beans.factory.DisposableBean;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;

/**
 * @Description :
 * @Reference :
 * @Author : dominic
 * @CreateDate : 2021/7/27 21:07
 * @Modify:
 **/
@FXMLController
public class ManageController implements DisposableBean {

    @FXML
    private TableView tableView;

    @FXML
    private TableColumn name;

    @FXML
    private TableColumn unit;

    @FXML
    private TableColumn number;

    @FXML
    private TableColumn price;

    @FXML
    private TextField selectName;

    @Resource
    private VegetablesService vegetablesService;

    public void initialize() {
        select();
        name.setCellValueFactory(new PropertyValueFactory("name"));
        unit.setCellValueFactory(new PropertyValueFactory("unit"));
        number.setCellValueFactory(new PropertyValueFactory("number"));
        price.setCellValueFactory(new PropertyValueFactory("price"));
    }

    public void back() {
        VegetableApp.showView(RunningView.class);
    }

    public void insert() {
        VegetableApp.showView(InsertView.class);
    }

    public void update() throws IOException {
        Vegetables vegetables = (Vegetables) tableView.getSelectionModel().getSelectedItem();
        if (vegetables == null) {
            AlertUtil.showErrorAlert("修改失败", "请选中需要修改的数据", false);
            return;
        }

        vegetables = vegetablesService.selectById(vegetables.getId());
        if (vegetables == null) {
            AlertUtil.showErrorAlert("修改失败", "该数据已被删除，请刷新", false);
            return;
        }

        Stage stage = new Stage();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/update.fxml"));
        Parent parent = loader.load();
        UpdateController updateController = loader.getController();
        updateController.name.setText(vegetables.getName());
        updateController.unit.setText(vegetables.getUnit());
        updateController.number.setText(vegetables.getNumber() == null ? null : vegetables.getNumber().toString());
        updateController.price.setText(vegetables.getPrice() == null ? null : vegetables.getPrice().toString());
        updateController.id = vegetables.getId();
        updateController.vegetablesService = (VegetablesService) SpringUtil.getBean("vegetablesService");

        Scene scene = new Scene(parent);
        stage.setScene(scene);
        stage.show();
    }

    public void select() {
        List<Vegetables> vegetablesList = vegetablesService.select(selectName.getText());
        ObservableList<Vegetables> tableData = FXCollections.observableArrayList(vegetablesList);
        tableView.setItems(tableData);
    }

    public void delete() {
        Vegetables vegetables = (Vegetables) tableView.getSelectionModel().getSelectedItem();
        if (vegetables == null) {
            AlertUtil.showErrorAlert("删除失败", "请选中需要删除的数据", false);
            return;
        }

        vegetables = vegetablesService.selectById(vegetables.getId());
        if (vegetables == null) {
            AlertUtil.showErrorAlert("删除失败", "该数据已被删除，请刷新", false);
            return;
        }

        vegetablesService.delete(vegetables.getId());
        AlertUtil.showInfoAlert("删除成功", "该数据已被删除", false);
        select();
    }

    @Override
    public void destroy() throws Exception {

    }
}
