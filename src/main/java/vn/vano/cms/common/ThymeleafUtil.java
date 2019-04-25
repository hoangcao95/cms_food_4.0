package vn.vano.cms.common;

import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.thymeleaf.templateresolver.FileTemplateResolver;

import java.util.HashMap;
import java.util.Locale;

public class ThymeleafUtil {
	
	public static String getHtmlContentInClassPath(String templateFile, HashMap<String, Object> map) {
        ClassLoaderTemplateResolver templateresolver = new ClassLoaderTemplateResolver();
        templateresolver.setTemplateMode("HTML5");
        templateresolver.setCharacterEncoding("UTF-8");
        TemplateEngine templateEngine = new TemplateEngine();
        templateEngine.setTemplateResolver(templateresolver);

        final Context ctx = new Context(Locale.US);
        for (String key : map.keySet()) {
            Object val = map.get(key);
            ctx.setVariable(key, val);
        }
        final String htmlContent = templateEngine.process(templateFile, ctx);
        return htmlContent;
    }

    public static String getHtmlContentInFile(String prefix, String templateFile, HashMap<String, Object> map) {
        FileTemplateResolver templateresolver = new FileTemplateResolver();
        templateresolver.setTemplateMode("HTML5");
        templateresolver.setPrefix(prefix);
        templateresolver.setCharacterEncoding("UTF-8");
        TemplateEngine templateEngine = new TemplateEngine();
        templateEngine.setTemplateResolver(templateresolver);

        final Context ctx = new Context(Locale.US);
        for (String key : map.keySet()) {
            Object val = map.get(key);
            ctx.setVariable(key, val);
        }
        final String htmlContent = templateEngine.process(templateFile, ctx);
        return htmlContent;
    }
}
