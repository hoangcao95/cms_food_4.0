package vn.vano.cms.config.web;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.thymeleaf.extras.springsecurity4.dialect.SpringSecurityDialect;
import org.thymeleaf.spring4.SpringTemplateEngine;
import org.thymeleaf.spring4.templateresolver.SpringResourceTemplateResolver;
import org.thymeleaf.spring4.view.ThymeleafViewResolver;
import org.thymeleaf.templatemode.TemplateMode;


@Configuration
//@Profile("HRYotel")
public class ThymeleafViewConfig {
	
	@Value("${spring.mvc.thymeleaf.cacheable}")
	boolean cacheable;
	
	@Bean
	public SpringResourceTemplateResolver templateResolver() {
		SpringResourceTemplateResolver templateResolver = new SpringResourceTemplateResolver();
		templateResolver.setPrefix("/WEB-INF/templates/");
		templateResolver.setSuffix(".html");
		templateResolver.setTemplateMode(TemplateMode.HTML);
		templateResolver.setCharacterEncoding("UTF-8");
		templateResolver.setCacheable(cacheable);
		return templateResolver;
	}
	
	@Bean
	public SpringTemplateEngine templateEngine() {
//		SpringTemplateEngine templateEngine = new SpringTemplateEngine();
//		templateEngine.setTemplateResolver(templateResolver());
//		// add dialects
//		Set<IDialect> additionalDialects = new LinkedHashSet<IDialect>();
//		additionalDialects.add(new SpringSecurityDialect());
//		additionalDialects.add(new ConditionalCommentsDialect());
//		additionalDialects.add(new LayoutDialect());
//		// IE comment
//		templateEngine.setAdditionalDialects(additionalDialects);
//		return templateEngine;
		
		SpringTemplateEngine templateEngine = new SpringTemplateEngine();
		templateEngine.setTemplateResolver(templateResolver());
		// add dialects
		templateEngine.addDialect(new SpringSecurityDialect());
//		templateEngine.addDialect(new ConditionalCommentsDialect());
//		templateEngine.addDialect(new LayoutDialect());
		return templateEngine;
	}
	
	@Bean
	public ThymeleafViewResolver thymeleafViewResolver(){
		ThymeleafViewResolver viewResolver = new ThymeleafViewResolver();
		viewResolver.setTemplateEngine(templateEngine());
		viewResolver.setCharacterEncoding("UTF-8");
		viewResolver.setOrder(Ordered.HIGHEST_PRECEDENCE);
		viewResolver.setCache(cacheable);
		viewResolver.setOrder(2);
		return viewResolver;
		
	}
}
