package com.homebase.ecom.cconfig.configuration;

import com.homebase.ecom.cconfig.sdk.CconfigClient;
import com.homebase.ecom.cconfig.sdk.CconfigClientImpl;
import com.homebase.ecom.cconfig.service.CconfigQueryService;
import com.homebase.ecom.cconfig.service.CconfigRetriever;
import com.homebase.ecom.cconfig.service.CconfigService;
import com.homebase.ecom.cconfig.service.healthcheck.CconfigHealthChecker;
import com.homebase.ecom.cconfig.service.impl.CconfigQueryServiceImpl;
import com.homebase.ecom.cconfig.service.impl.CconfigServiceImpl;
import com.homebase.ecom.cconfig.service.impl.DbBasedCconfigRetriever;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 Initialize all the Chenile config related services.
*/
@Configuration
public class CconfigConfiguration {
	/**
	 * Chenile Config will scan this path for all config JSONs. All configs are expected
	 * to be of the form {module}.{key}.<br/>
	 * A JSON file module.json file must exist in this path. <br/>
	 * It should contain "key" at the root level. See test cases for more info.
	 */
	@Value("${chenile.config.path:com/homebase/ecom/config}")
	private String configPath;

	/**
	 * Pass all the modules whose values are needed in this instance of the config.
	 */
	@Value("${chenile.config.modules:}")
	private List<String> modules;

	/**
	 * Pass all the modules whose values are needed in this instance of the config.
	 */
	@Value("${chenile.config.customAttributes:}")
	private List<String> customAttributes;

	@Bean public CconfigRetriever dbBasedCconfigRetriever(){
		return new DbBasedCconfigRetriever();
	}

	@Bean public CconfigClient serverCconfigClient(@Autowired @Qualifier("dbBasedCconfigRetriever") CconfigRetriever cconfigRetriever){
		return new CconfigClientImpl(configPath,cconfigRetriever);
	}
	@Bean public CconfigQueryService _cconfigQueryService_(@Autowired @Qualifier("serverCconfigClient") CconfigClient client) {
		return new CconfigQueryServiceImpl(client);
	}
	@Bean public CconfigService _cconfigService_() {
		return new CconfigServiceImpl();
	}
	@Bean CconfigHealthChecker cconfigHealthChecker(){
    	return new CconfigHealthChecker();
    }
}
