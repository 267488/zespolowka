/*
 * Copyright 2016 Agata Kostrzewa
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package pz.twojaszkola.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.module.jaxb.JaxbAnnotationModule;
import com.fasterxml.jackson.module.jaxb.JaxbAnnotationModule.Priority;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.embedded.ConfigurableEmbeddedServletContainer;
import org.springframework.boot.context.embedded.EmbeddedServletContainerCustomizer;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.config.annotation.ContentNegotiationConfigurer;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * @author Michael J. Simons, 2014-02-15
 */
@Configuration
public class WebConfig extends WebMvcConfigurerAdapter {

    /**
     * Allow cross origin requests for all api endpoints.
     * @param registry 
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
	registry.addMapping("/api/**").allowedOrigins("*");
    }
    
    /**
     * Maps all AngularJS routes to index so that they work with direct linking.
     */
    @Controller
    static class Routes {

	@RequestMapping({
	    "/",
	    "/bikes",
            "/index.html/uczen",
            "/index.html/szkola",
            "/index.html/nowaSzkola",
            "/index.html/profilSzkoly",
            "/index.html/aktualnosciSzkola",
            "/index.html/profile",
            "/index.html/ocenaPrzedmiotu",
            "/index.html/rozszerzonePrzedmioty",
            "/index.html/proponowaneSzkoly",
            "/index.html/zainteresowania",
            "/index.html/kolkaZainteresowan",
	    "/index.html/gallery",
	    "/index.html/location",
	    "/index.html/about",
	})
	public String index() {
	    return "forward:/login.html";
	}
    }
    
    /**
     * Enable favor of format parameter over requested content type, needed for
     * {@code OEmbedController#getEmbeddableTrack(java.lang.String, java.lang.String, java.lang.Integer, java.lang.Integer, javax.servlet.http.HttpServletRequest)}
     *
     * @param configurer
     */
    @Override
    public void configureContentNegotiation(ContentNegotiationConfigurer configurer) {
	super.configureContentNegotiation(configurer);
	configurer.favorParameter(true);
    }

    /**
     * This makes mapping of
     * {@code TracksController#downloadTrack(java.lang.String, java.lang.String, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)} 
     * and the default mapping in separate methods possible.
     * @param configurer
     */
    @Override
    public void configurePathMatch(final PathMatchConfigurer configurer) {
	configurer.setUseRegisteredSuffixPatternMatch(true);
    }

    /**
     * {@code OEmbedResponse} uses XmlElement annotations to be configured for
     * JAXB as well as JSON so we need the {@link JaxbAnnotationModule} as well
     *
     * @return
     */
    @Bean
    public ObjectMapper jacksonObjectMapper() {
	return new ObjectMapper().registerModules(
		new JaxbAnnotationModule().setPriority(Priority.SECONDARY)
	);
    }

    @Bean    
    public EmbeddedServletContainerCustomizer embeddedServletContainerCustomizer(
	    final @Value("${biking2.connector.proxyName:}") String proxyName,
	    final @Value("${biking2.connector.proxyPort:80}") int proxyPort
    ) {
	return (ConfigurableEmbeddedServletContainer configurableContainer) -> {
	    if (configurableContainer instanceof TomcatEmbeddedServletContainerFactory) {
		final TomcatEmbeddedServletContainerFactory containerFactory = (TomcatEmbeddedServletContainerFactory) configurableContainer;
		containerFactory.setTldSkip("*.jar");
		if(!proxyName.isEmpty()) {
		    containerFactory.addConnectorCustomizers(connector -> {
			connector.setProxyName(proxyName);
			connector.setProxyPort(proxyPort);
		    });
		}
	    }
	};
    }
}
