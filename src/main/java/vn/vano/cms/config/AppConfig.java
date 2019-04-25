package vn.vano.cms.config;


import org.apache.commons.dbcp2.BasicDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.*;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import vn.vano.cms.automl.DatasetApi;
import vn.vano.cms.automl.ModelApi;
import vn.vano.cms.automl.PredictionApi;
import vn.vano.cms.common.ChargingCSPApi;
import vn.vano.cms.jpa.CoreMoQueue;
import vn.yotel.commons.context.AppContext;

import javax.sql.DataSource;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

@Configuration
@EnableAsync
@EnableScheduling
@EnableAspectJAutoProxy(proxyTargetClass = true)
@ComponentScan(basePackages = { "vn.yotel", "vn.vano" })
@ImportResource(value = { "classpath:applicationContext-main.xml" })
//@Profile("HRYotel")
public class AppConfig {

	/**
	 * JDBC properties
	 */
	@Value("${jdbc.driverClassName}")
	private String jdbcDriverClassName;

	@Value("${jdbc.url}")
	private String jdbcUrl;

	@Value("${jdbc.username}")
	private String jdbcUsername;

	@Value("${jdbc.password}")
	private String jdbcPassword;

	@Value("${jndi.datasource}")
	private String jndiDatasource;

	@Value("${jdbc.max-total-connection}")
	private int jdbcMaxTotalConnection;

	@Value("${jdbc.max-idle-connection}")
	private int jdbcMaxIdleConnection;

	@Value("${jdbc.max-init-connection}")
	private int jdbcInitConnection;

	//ChargingCSP Config
	@Value("${chargingCSP.serviceNumber}")
	private String ccspServiceNumber;
	@Value("${chargingCSP.username}")
	private String ccspUserName;
	@Value("${chargingCSP.password}")
	private String ccspPassword;
	@Value("${chargingCSP.wsUrl}")
	private String ccspWsUrl;
	@Value("${chargingCSP.wsTargetNamespace}")
	private String ccspWsTargetNamespace;
	@Value("${chargingCSP.wsNamespacePrefix}")
	private String ccspWsNamespacePrefix;
	@Value("${chargingCSP.headerKey}")
	private String ccspHeaderKey;
	@Value("${chargingCSP.headerValue}")
	private String ccspHeaderValue;
	@Value("${chargingCSP.sendMessageUrl}")
	private String ccspSendMessageUrl;
	@Value("${chargingCSP.sendMessageFunc}")
	private String ccspSendMessageFunc;
	@Value("${chargingCSP.actionServiceUrl}")
	private String ccspActionServiceUrl;
	@Value("${chargingCSP.actionServiceFunc}")
	private String ccspActionServiceFunc;
	@Value("${chargingCSP.chargeContentUrl}")
	private String ccspChargeContentUrl;
	@Value("${chargingCSP.chargeContentFunc}")
	private String ccspChargeContentFunc;
	@Value("${chargingCSP.sendMTRemindUrl}")
	private String ccspSendMTRemindUrl;
	@Value("${chargingCSP.sendMTRemindFunc}")
	private String ccspSendMTRemindFunc;

	@Bean
	public DataSource dataSource() {
		BasicDataSource basicDataSource = new BasicDataSource();
		basicDataSource.setDriverClassName(this.jdbcDriverClassName);
		basicDataSource.setUrl(this.jdbcUrl);
//		basicDataSource.setUsername(PasswordUtil.decryptPropertyValue(this.jdbcUsername));
//		basicDataSource.setPassword(PasswordUtil.decryptPropertyValue(this.jdbcPassword));
		basicDataSource.setUsername(this.jdbcUsername);
		basicDataSource.setPassword(this.jdbcPassword);
		basicDataSource.setInitialSize(this.jdbcInitConnection);
		basicDataSource.setMaxIdle(this.jdbcMaxIdleConnection);
		basicDataSource.setMaxTotal(this.jdbcMaxTotalConnection);
		return basicDataSource;
	}
	
	@Bean
	@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
	@Primary
	public AppContext appContext() {
		return new AppContext();
	}
	
	@Bean
	@Primary
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean(name = "moProcessQueue")
	@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
	public Queue<CoreMoQueue> moProcessQueue() {
		return new ConcurrentLinkedQueue<CoreMoQueue>();
	}

	@Bean(name = "moProcessQueueNotifier")
	@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
	public Object moProcessQueueNotifier() {
		return new Object();
	}

	@Bean(name = "mtProcessCSPQueue")
	@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
	public Queue<CoreMoQueue> mtProcessCSPQueue() {
		return new ConcurrentLinkedQueue<CoreMoQueue>();
	}

	@Bean(name = "mtProcessCSPQueueNotifier")
	@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
	public Object mtProcessCSPQueueNotifier() {
		return new Object();
	}

	@Bean(name = "mtProcessSMSCQueue")
	@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
	public Queue<CoreMoQueue> mtProcessSMSCQueue() {
		return new ConcurrentLinkedQueue<CoreMoQueue>();
	}

	@Bean(name = "mtProcessSMSCQueueNotifier")
	@Scope(ConfigurableBeanFactory.SCOPE_SINGLETON)
	public Object mtProcessSMSCQueueNotifier() {
		return new Object();
	}

	@Bean(name = "chargingCSPApi")
	public ChargingCSPApi chargingCSPApi() {
		ChargingCSPApi cspApi = new ChargingCSPApi();
		cspApi.setServiceNumber(ccspServiceNumber);
		cspApi.setUsername(ccspUserName);
		cspApi.setPassword(ccspPassword);
		cspApi.setWsUrl(ccspWsUrl);
		cspApi.setWsTargetNamespace(ccspWsTargetNamespace);
		cspApi.setWsNamespacePrefix(ccspWsNamespacePrefix);
		cspApi.setHeaderKey(ccspHeaderKey);
		cspApi.setHeaderValue(ccspHeaderValue);
		cspApi.setSendMessageUrl(ccspSendMessageUrl);
		cspApi.setSendMessageFunc(ccspSendMessageFunc);
		cspApi.setActionServiceUrl(ccspActionServiceUrl);
		cspApi.setActionServiceFunc(ccspActionServiceFunc);
		cspApi.setChargeContentUrl(ccspChargeContentUrl);
		cspApi.setChargeContentFunc(ccspChargeContentFunc);
		cspApi.setSendMTRemindUrl(ccspSendMTRemindUrl);
		cspApi.setSendMTRemindFunc(ccspSendMTRemindFunc);
		return cspApi;
	}

	@Bean(name = "datasetApi")
	public DatasetApi datasetApi() {
		DatasetApi api = new DatasetApi();
		return api;
	}

	@Bean(name = "modelApi")
	public ModelApi modelApi() {
		ModelApi api = new ModelApi();
		return api;
	}

	@Bean(name = "predictionApi")
	public PredictionApi predictionApi() {
		PredictionApi api = new PredictionApi();
		return api;
	}
}
