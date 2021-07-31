package cn.dominic.controller;

import cn.dominic.VegetableApp;
import cn.dominic.view.ManageView;
import cn.dominic.view.PrintView;
import cn.dominic.view.SettingView;
import de.felixroske.jfxsupport.FXMLController;
import org.springframework.beans.factory.DisposableBean;

/**
 * @Description :
 * @Reference :
 * @Author : dominic
 * @CreateDate : 2021/7/27 21:07
 * @Modify:
 **/
@FXMLController
public class RunningController implements DisposableBean {

    public void showManage() {
        VegetableApp.showView(ManageView.class);
    }

    public void showPrint() {
        VegetableApp.showView(PrintView.class);
    }

    public void showSetting() {
        VegetableApp.showView(SettingView.class);
    }

    @Override
    public void destroy() throws Exception {

    }
}
