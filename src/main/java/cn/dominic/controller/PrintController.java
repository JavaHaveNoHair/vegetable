package cn.dominic.controller;

import cn.dominic.VegetableApp;
import cn.dominic.pojo.PrintData;
import cn.dominic.pojo.Vegetables;
import cn.dominic.service.VegetablesService;
import cn.dominic.util.AlertUtil;
import cn.dominic.util.CastUtil;
import cn.dominic.util.PdfUtils;
import cn.dominic.util.PrintUtil;
import cn.dominic.view.RunningView;
import com.alibaba.fastjson.JSONObject;
import de.felixroske.jfxsupport.FXMLController;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.util.Callback;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.print.PrintService;
import java.awt.print.PrinterJob;
import java.io.File;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Description :
 * @Reference :
 * @Author : dominic
 * @CreateDate : 2021/7/27 21:07
 * @Modify:
 **/
@FXMLController
public class PrintController implements DisposableBean {

    @FXML
    private TableView choiceTable;

    @FXML
    private TableColumn choiceName;

    @FXML
    private TableColumn choiceUnit;

    @FXML
    private TableColumn choiceNumber;

    @FXML
    private TableColumn choicePrice;

    @FXML
    private TableView printTable;

    @FXML
    private TableColumn printName;

    @FXML
    private TableColumn printUnit;

    @FXML
    private TableColumn printNumber;

    @FXML
    private TableColumn printPrice;

    @FXML
    private TextField selectName;

    @FXML
    private TextField customerName;

    @FXML
    private CheckBox cSelectAll;

    @FXML
    private TableColumn<Vegetables, CheckBox> cColumnSelect;

    @Resource
    private VegetablesService vegetablesService;

    /**
     * PDF????????????
     */
    private static final String PDF_PATH = File.separator + "pdf" + File.separator;

    /**
     * ????????????????????????
     */
    private static final Integer DEFAULT_KITTING_SIZE = 40;

    public void initialize() {
        select();
        choiceName.setCellValueFactory(new PropertyValueFactory("name"));
        choiceUnit.setCellValueFactory(new PropertyValueFactory("unit"));
        choiceNumber.setCellValueFactory(new PropertyValueFactory("number"));
        choicePrice.setCellValueFactory(new PropertyValueFactory("price"));
        printName.setCellValueFactory(new PropertyValueFactory("printName"));
        printUnit.setCellValueFactory(new PropertyValueFactory("printUnit"));
        printNumber.setCellValueFactory(new PropertyValueFactory("printNumber"));
        printPrice.setCellValueFactory(new PropertyValueFactory("printPrice"));

        printTable.setEditable(true);
        printNumber.setCellFactory(TextFieldTableCell.forTableColumn());
        printPrice.setCellFactory(TextFieldTableCell.forTableColumn());
        printNumber.setOnEditCommit((EventHandler<TableColumn.CellEditEvent<PrintData, String>>) event ->
                event.getTableView().getItems().get(event.getTablePosition().getRow()).setPrintNumber(event.getNewValue()));
        printPrice.setOnEditCommit((EventHandler<TableColumn.CellEditEvent<PrintData, String>>) event ->
                event.getTableView().getItems().get(event.getTablePosition().getRow()).setPrintPrice(event.getNewValue()));

        Callback<TableColumn<Vegetables, CheckBox>, TableCell<Vegetables, CheckBox>>
                selectCellFactory =
                new Callback<TableColumn<Vegetables, CheckBox>, TableCell<Vegetables, CheckBox>>() {
                    @Override
                    public TableCell call(TableColumn p) {
                        TableCell cell = new TableCell<Vegetables, CheckBox>() {
                            @Override
                            public void updateItem(CheckBox item, boolean empty) {
                                super.updateItem(item, empty);
                                setGraphic(item);
                                if (item != null) {
                                    //??????????????????-??????-??????checkbox?????????????????????????????????????????????checkbox???????????????????????????????????????
                                    item.selectedProperty()
                                            .addListener(new ChangeListener<Boolean>() {
                                                @Override
                                                public void changed(
                                                        ObservableValue<? extends Boolean> observable,
                                                        Boolean oldValue, Boolean newValue) {
                                                    boolean booSelectAll = true;
                                                    ObservableList<Vegetables> items = choiceTable.getItems();
                                                    for (Vegetables vegetables : items) {
                                                        if (!vegetables.getCb().isSelected()) {
                                                            booSelectAll = false;
                                                            vegetables.setSelect(false);
                                                        } else {
                                                            vegetables.setSelect(true);
                                                        }
                                                    }
                                                    cSelectAll.setSelected(booSelectAll);
                                                }
                                            });
                                }
                            }
                        };
                        return cell;
                    }
                };
        cColumnSelect.setCellFactory(selectCellFactory);
        // ???????????????checkbox?????????
        cColumnSelect.setCellValueFactory(cellData -> cellData.getValue().getCb().getCheckBox());
    }

    public void selectAll() {
        ObservableList<Vegetables> items = choiceTable.getItems();
        if (cSelectAll.isSelected()) {
            for (Vegetables vegetables : items) {
                vegetables.getCb().setSelected(true);
            }
        } else {
            for (Vegetables vegetables : items) {
                vegetables.getCb().setSelected(false);
            }
        }
    }

    public void back() {
        VegetableApp.showView(RunningView.class);
    }

    public void add() {
        ObservableList<Vegetables> data = choiceTable.getItems();
        List<Vegetables> choiceData = data.stream().filter(d -> d != null && d.isSelect()).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(choiceData)) {
            AlertUtil.showErrorAlert("????????????", "??????????????????????????????", false);
            return;
        }
        ObservableList<Vegetables> choiceList = FXCollections.observableArrayList(choiceData);

        List<PrintData> printDataList = new ArrayList<>();
        for (Vegetables vegetables : choiceList) {
            PrintData printData = new PrintData();
            printData.setPrintId(vegetables.getId());
            printData.setPrintName(vegetables.getName());
            printData.setPrintUnit(vegetables.getUnit());
            printData.setPrintPrice(vegetables.getPrice() == null ? null : vegetables.getPrice().toString());
            printDataList.add(printData);
        }

        ObservableList<PrintData> finalPrintDataList = FXCollections.observableArrayList(printDataList);
        printTable.getItems().addAll(finalPrintDataList);
    }

    public void select() {
        List<Vegetables> vegetablesList = vegetablesService.select(selectName.getText());
        ObservableList<Vegetables> tableData = FXCollections.observableArrayList(vegetablesList);
        choiceTable.setItems(tableData);
    }

    public void delete() {
        int index = printTable.getSelectionModel().getSelectedIndex();
        if (index < 0) {
            AlertUtil.showErrorAlert("????????????", "??????????????????????????????", false);
            return;
        }
        printTable.getItems().remove(index);
    }

    public void clear() {
        printTable.getItems().clear();
    }

    public void print() {
        if (printTable.getItems().isEmpty()) {
            AlertUtil.showErrorAlert("????????????", "??????????????????????????????????????????", false);
            return;
        }

        List<PrintData> printDataList = printTable.getItems();
        for (PrintData printData : printDataList) {
            if (printData.getPrintNumber() == null) {
                AlertUtil.showErrorAlert("????????????", "???????????????", false);
                return;
            }
        }

        if (StringUtils.isEmpty(customerName.getText())) {
            AlertUtil.showErrorAlert("????????????", "?????????????????????", false);
            return;
        }

        JSONObject confJson = SettingController.readConfFile();
        if (confJson == null) {
            AlertUtil.showErrorAlert("????????????", "??????????????????", false);
            return;
        }
        String localPrinterName = confJson.containsKey("printerName") ? confJson.getString("printerName") : null;
        String localPrintPath = confJson.containsKey("printPath") ? confJson.getString("printPath") : null;
        String templatePath = confJson.containsKey("templatePath") ? confJson.getString("templatePath") : null;

        if (StringUtils.isEmpty(localPrinterName)) {
            AlertUtil.showErrorAlert("????????????", "???????????????????????????", false);
            return;
        }

        boolean boo = checkPrinter(localPrinterName);
        if (!boo) {
            AlertUtil.showErrorAlert("????????????", "????????????????????????????????????", false);
            return;
        }

        for (int i = 0; i < printDataList.size(); i++) {
            printDataList.get(i).setSeq(i + 1);
            if (!StringUtils.isEmpty(printDataList.get(i).getPrintPrice())) {
                printDataList.get(i).setPrintTotalPrice(String.format("%.2f", Double.parseDouble(printDataList.get(i).getPrintPrice()) * Double.parseDouble(printDataList.get(i).getPrintNumber())));
            }
        }

        double sumPrice = printDataList.stream().filter(p -> p.getPrintTotalPrice() != null).
                mapToDouble(p -> Double.parseDouble(p.getPrintTotalPrice())).sum();
        String upperCasePrice = CastUtil.moneyToUpperCase(String.format("%.2f", sumPrice));

        NumberFormat format = NumberFormat.getCurrencyInstance();

        Map<String, Object> map = new HashMap<>();
        map.put("printDataList", printDataList);
        map.put("customerName", customerName.getText());
        map.put("sumPrice", format.format(sumPrice));
        map.put("upperCasePrice", upperCasePrice);
        map.put("createDate", new SimpleDateFormat("yyyy-MM-dd").format(new Date()));

        List<String> pdfList = new ArrayList<>();
        if (printDataList.size() < DEFAULT_KITTING_SIZE) {
            pdfList.add(PdfUtils.method(map, localPrintPath + PDF_PATH, "template",
                    templatePath + File.separator));
        } else {
            int i = 1;
            int pageNum = 1;
            int totalPage = printDataList.size() / DEFAULT_KITTING_SIZE;
            int remainder = printDataList.size() % DEFAULT_KITTING_SIZE;
            if (remainder != 0) {
                totalPage += 1;
            }
        }

        // ???????????????
        pdfList.forEach(p -> PrintUtil.printPdf(localPrinterName, p));
    }

    private boolean checkPrinter(String printerName) {
        for (PrintService ps : PrinterJob.lookupPrintServices()) {
            String psName = ps.toString();
            if (psName.contains(printerName)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void destroy() throws Exception {

    }
}