package cn.dominic;

import cn.dominic.view.RunningView;
import de.felixroske.jfxsupport.AbstractJavaFxApplicationSupport;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.boot.autoconfigure.SpringBootApplication;


/**
 * 主应用类
 * @author VULCAN
 */
@Slf4j
@SpringBootApplication
public class VegetableApp extends AbstractJavaFxApplicationSupport implements DisposableBean {
    public static void main(String[] args) {
        launch(VegetableApp.class, RunningView.class, args);
    }

    @Override
    public void destroy() {
        log.info("Application关闭");
    }
}
