package cn.dominic.util;

import javafx.scene.control.Alert;

/**
 * @Description :
 * @Reference :
 * @Author : dominic
 * @CreateDate : 2021/7/27 22:42
 * @Modify:
 **/
public class AlertUtil {
    private AlertUtil() {
    }

    private static final String INFO = "信息";
    private static final String ERROR = "错误";
    private static final String CONFIRM = "确认";
    private static final String WARNING = "警告";

    public static void showInfoAlert(String headerText, String contentText, boolean stop) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        setAlert(INFO, headerText, contentText, alert);
        if (stop) {
            alert.showAndWait();
        } else {
            alert.show();
        }
    }

    public static void showErrorAlert(String headerText, String contentText, boolean stop) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        setAlert(ERROR, headerText, contentText, alert);
        if (stop) {
            alert.showAndWait();
        } else {
            alert.show();
        }
    }

    public static void showWarningAlert(String headerText, String contentText, boolean stop) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        setAlert(WARNING, headerText, contentText, alert);
        alert.showAndWait();
        if (stop) {
            alert.showAndWait();
        } else {
            alert.show();
        }
    }

    public static void showConfirmAlert(String headerText, String contentText, boolean stop) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        setAlert(CONFIRM, headerText, contentText, alert);
        alert.showAndWait();
        if (stop) {
            alert.showAndWait();
        } else {
            alert.show();
        }
    }

    private static void setAlert(String title, String headerText, String contentText, Alert alert) {
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
    }
}
