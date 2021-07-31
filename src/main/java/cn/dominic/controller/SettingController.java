package cn.dominic.controller;

import cn.dominic.VegetableApp;
import cn.dominic.pojo.Config;
import cn.dominic.util.AlertUtil;
import cn.dominic.util.DataConfigUtil;
import cn.dominic.util.GlobalVariable;
import cn.dominic.util.ToastUtil;
import cn.dominic.view.RunningView;
import cn.hutool.core.io.FileUtil;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import de.felixroske.jfxsupport.FXMLController;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.util.ResourceUtils;
import org.springframework.util.StringUtils;

import java.io.*;

/**
 * @Description :
 * @Reference :
 * @Author : dominic
 * @CreateDate : 2021/7/27 21:07
 * @Modify:
 **/
@Slf4j
@FXMLController
public class SettingController implements DisposableBean {

    @FXML
    private TextField printerName;

    @FXML
    private TextField printPath;

    @FXML
    private TextField templatePath;

    public void initialize() {
        JSONObject json = readConfFile();
        if (json != null) {
            printerName.setText(json.containsKey("printerName") ? json.getString("printerName") : null);
            printPath.setText(json.containsKey("printPath") ? json.getString("printPath") : null);
            templatePath.setText(json.containsKey("templatePath") ? json.getString("templatePath") : null);
            saveConfig();
        }
    }

    public void save() {
        if (StringUtils.isEmpty(printerName.getText())) {
            AlertUtil.showErrorAlert("保存失败", "打印机名称为必填项，请填写", false);
            return;
        }
        if (StringUtils.isEmpty(printPath.getText())) {
            AlertUtil.showErrorAlert("保存失败", "打印存放路径为必填项，请填写", false);
            return;
        }
        if (StringUtils.isEmpty(templatePath.getText())) {
            AlertUtil.showErrorAlert("保存失败", "打印模板路径为必填项，请填写", false);
            return;
        }
        if (!checkPathIsDirectory(printPath.getText())) {
            AlertUtil.showErrorAlert("路径不正确", "打印存放路径不是一个目录，请填写正确的目录", false);
            return;
        }
        if (!checkPathIsDirectory(templatePath.getText())) {
            AlertUtil.showErrorAlert("路径不正确", "打印模板路径不是一个目录，请填写正确的目录", false);
            return;
        }

        try {
            Config config = saveConfig();
            updateConfFile(config);
            DataConfigUtil.putMap("CONFIG", config);

            GlobalVariable.changeGlobalConfig(config);
            ToastUtil.toast("保存成功", 500);
            VegetableApp.showView(RunningView.class);
        } catch (Exception ex) {
            AlertUtil.showErrorAlert("保存配置信息发生错误", ex.getMessage(), false);
        }
    }

    private Config saveConfig() {
        Config config = new Config();
        config.setPrinterName(printerName.getText());
        config.setPrintPath(printPath.getText());
        config.setTemplatePath(templatePath.getText());
        DataConfigUtil.putMap("CONFIG", config);
        GlobalVariable.changeGlobalConfig(config);
        return config;
    }

    public void choosePrintTargetPath() {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Choose Folder");
        File directory = directoryChooser.showDialog(new Stage());
        if (directory != null) {
            printPath.setText(directory.getAbsolutePath());
        }
    }

    public void chooseTemplateTargetPath() {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Choose Folder");
        File directory = directoryChooser.showDialog(new Stage());
        if (directory != null) {
            templatePath.setText(directory.getAbsolutePath());
        }
    }

    private boolean checkPathIsDirectory(String path) {
        return FileUtil.isDirectory(path);
    }

    private void updateConfFile(Config config) {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("printerName", config.getPrinterName());
            jsonObject.put("printPath", config.getPrintPath());
            jsonObject.put("templatePath", config.getTemplatePath());
            String filePath = getConfFilePath();
            File file = new File(filePath + File.separator + "vegetable.conf");
            if (!file.exists()) {
                file.createNewFile();
            }
            FileWriter writer = new FileWriter(file.getAbsoluteFile());
            BufferedWriter bw = new BufferedWriter(writer);
            bw.write(jsonObject.toJSONString());
            bw.close();
            writer.close();
        } catch (IOException e) {
            log.error("保存配置文件异常:{}", e.getMessage());
        } catch (JSONException e) {
            log.error("保存配置文件异常,参数异常，参数不是json格式:{}", e.getMessage());
        }
    }

    public static JSONObject readConfFile() {
        JSONObject jsonObject = null;
        try {
            String filePath = getConfFilePath();
            log.error(filePath);
            File file = new File(filePath + File.separator + "vegetable.conf");
            if (!file.exists()) {
                return jsonObject;
            }
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String tempStr = null;
            StringBuilder sb = new StringBuilder();
            int line = 1;
            while ((tempStr = reader.readLine()) != null) {
                sb.append(tempStr);
                line++;
            }
            reader.close();
            jsonObject = JSONObject.parseObject(sb.toString());
        } catch (IOException e) {
            log.error("读取配置文件异常:{}", e.getMessage());
        } catch (JSONException e) {
            log.error("读取配置文件异常,参数异常，参数不是json格式:{}", e.getMessage());
        }
        return jsonObject;
    }

    private static String getConfFilePath() throws FileNotFoundException {
        String path = ResourceUtils.getURL("classpath:").getPath();
        String filePath = path.substring(0, path.lastIndexOf("/vegetable"));
        return filePath.replace("file:/", "");
    }

    public void back() {
        VegetableApp.showView(RunningView.class);
    }

    @Override
    public void destroy() throws Exception {

    }
}
