package vn.vano.cms.config.rest;


import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.media.multipart.MultiPartFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.spring.scope.RequestContextFilter;


/**
 *
 */
@javax.ws.rs.ApplicationPath("/v1/")
public class ApplicationResourceConfig extends ResourceConfig {

    public ApplicationResourceConfig() {
    	register(RequestContextFilter.class);

    	//register features
		register(JacksonFeature.class);
		register(MultiPartFeature.class);

    	// register  filter
        // point to packages containing your resources
        packages("vn.vano.cms.resource");
    }

}
