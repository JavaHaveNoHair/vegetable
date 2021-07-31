package cn.dominic.util;

import com.lowagie.text.pdf.BaseFont;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templateresolver.FileTemplateResolver;
import org.w3c.dom.Document;
import org.xhtmlrenderer.pdf.ITextFontResolver;
import org.xhtmlrenderer.pdf.ITextRenderer;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.*;
import java.util.Map;

/**
 * @Description :
 * @Reference :
 * @Author : dominic
 * @CreateDate : 2021/7/27 22:42
 * @Modify:
 **/
public class PdfUtils {

    public static String pdfImg(Map<String, Object> map, String filePath, String templateName, String templatePath, String imageBaseURL) {
        String outFileName = filePath + System.currentTimeMillis() + ".pdf";
        try {
            File dateFile = new File(filePath);
            if (!dateFile.exists()) {
                dateFile.mkdirs();
            }
            OutputStream out = new FileOutputStream(new File(outFileName));
            String htmlStr = generate(templatePath, templateName, map);

            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document doc = builder.parse(new ByteArrayInputStream(htmlStr.getBytes("UTF-8")));
            ITextRenderer renderer = new ITextRenderer();

            renderer.getSharedContext().setReplacedElementFactory(new Base64ImgReplacedElementFactory());
            renderer.getSharedContext().getTextRenderer().setSmoothingThreshold(1);
            renderer.getSharedContext().setBaseURL("file:/" + imageBaseURL + File.separator);
//            renderer.getSharedContext().setBaseURL(
//                    imageBaseURL.toURI().toURL().toString());

            renderer.setDocumentFromString(htmlStr);

            ITextFontResolver fontResolver = renderer.getFontResolver();
            fontResolver.addFont(templatePath + "simsun.ttf", BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);

            renderer.setDocument(doc, null);
            renderer.layout();
            renderer.createPDF(out);
        } catch (Exception e) {
            throw new RuntimeException("生成PDF失败:" + e.getMessage());
        }
        return outFileName;
    }

    public static String method(Map<String, Object> map, String filePath, String templateName, String templatePath) {
        String outFileName = filePath + System.currentTimeMillis() + ".pdf";
        try {
            File dateFile = new File(filePath);
            if (!dateFile.exists()) {
                dateFile.mkdirs();
            }
            OutputStream out = new FileOutputStream(new File(outFileName));
            String htmlStr = generate(templatePath, templateName, map);

            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            Document doc = builder.parse(new ByteArrayInputStream(htmlStr.getBytes("UTF-8")));
            ITextRenderer renderer = new ITextRenderer();

            renderer.getSharedContext().setReplacedElementFactory(new Base64ImgReplacedElementFactory());
            renderer.getSharedContext().getTextRenderer().setSmoothingThreshold(1);

            renderer.setDocumentFromString(htmlStr);

            ITextFontResolver fontResolver = renderer.getFontResolver();
            fontResolver.addFont(templatePath + "simsun.ttf", BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);

            renderer.setDocument(doc, null);
            renderer.layout();
            renderer.createPDF(out);
        } catch (Exception e) {
            throw new RuntimeException("生成PDF失败:" + e.getMessage());
        }
        return outFileName;
    }

    public static String generate(String filePath, String template, Map<String, Object> variables) throws Exception {

        //创建模板解析器
        FileTemplateResolver templateResolver = new FileTemplateResolver();
        templateResolver.setPrefix(filePath);
        templateResolver.setSuffix(".html");
        templateResolver.setCharacterEncoding("UTF-8");
        templateResolver.setTemplateMode("HTML");
        templateResolver.setCacheTTLMs(Long.valueOf(3600000L));
        templateResolver.setCacheable(true);

        //创建模板引擎并初始化解析器
        TemplateEngine engine = new TemplateEngine();
        engine.setTemplateResolver(templateResolver);
        engine.isInitialized();

        //输出流
        StringWriter stringWriter = new StringWriter();
        BufferedWriter writer = new BufferedWriter(stringWriter);

        //获取上下文
        Context ctx = new Context();
        ctx.setVariables(variables);
        engine.process(template, ctx, writer);

        stringWriter.flush();
        stringWriter.close();
        writer.flush();
        writer.close();

        //输出html
        String htmlStr = stringWriter.toString();
        return htmlStr;
    }
}
