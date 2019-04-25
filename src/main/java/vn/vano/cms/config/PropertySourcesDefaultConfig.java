package vn.vano.cms.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import vn.yotel.commons.annotation.Development;
import vn.yotel.commons.annotation.Production;

/**
 *
 */
@Configuration
//@Profile("HRYotel")
public class PropertySourcesDefaultConfig {

	private static final Resource[] DEV_PROPERTIES = new ClassPathResource[] { new ClassPathResource("properties/application_dev.properties")};
	private static final Resource[] PROD_MASTER_PROPERTIES = new ClassPathResource[] { new ClassPathResource("properties/application.properties")};
	private static final Resource[] PROD_CMS_PROPERTIES = new ClassPathResource[] { new ClassPathResource("properties/application_cms.properties")};
	private static final Resource[] PROD_BACKUP_PROPERTIES = new ClassPathResource[] { new ClassPathResource("properties/application_backup.properties")};

	@Production
	public static class ProdConfig {
		@Bean
		public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
			PropertySourcesPlaceholderConfigurer pspc = new PropertySourcesPlaceholderConfigurer();
			pspc.setFileEncoding("UTF-8");
			pspc.setIgnoreResourceNotFound(false);
			pspc.setIgnoreUnresolvablePlaceholders(false);
			pspc.setOrder(0);
			pspc.setLocalOverride(true);
			pspc.setLocations(PROD_MASTER_PROPERTIES);
			return pspc;
		}
	}
	
	
	@Development
	public static class DevConfig {
		@Bean
		public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
			PropertySourcesPlaceholderConfigurer pspc = new PropertySourcesPlaceholderConfigurer();
			pspc.setFileEncoding("UTF-8");
			pspc.setIgnoreResourceNotFound(false);
			pspc.setIgnoreUnresolvablePlaceholders(false);
			pspc.setOrder(0);
			pspc.setLocalOverride(true);
			pspc.setLocations(DEV_PROPERTIES);
			return pspc;
		}
	}

}
