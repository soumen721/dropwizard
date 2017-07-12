package com.example.dw.jpa;

import java.util.EnumSet;
import java.util.Properties;

import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration;
import javax.ws.rs.client.Client;

import org.eclipse.jetty.servlets.CrossOriginFilter;
import org.glassfish.jersey.server.filter.RolesAllowedDynamicFeature;

import com.example.dw.jpa.config.Config;
import com.example.dw.jpa.config.DbConfig;
import com.example.dw.jpa.resource.HomeResource;
import com.example.dw.jpa.resource.PlayerResource;
import com.example.dw.jpa.resource.UserResource;
import com.example.dw.jwt.resource.SecretsResource;
import com.example.dw.jwt.resource.StaticJWTResource;
import com.example.rest.basicauth.AppAuthenticator;
import com.example.rest.basicauth.AppAuthorizer;
import com.example.rest.basicauth.User;
import com.example.rest.healthcheck.AppHealthCheck;
import com.example.rest.healthcheck.HealthCheckController;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.persist.PersistFilter;
import com.google.inject.persist.jpa.JpaPersistModule;

import io.dropwizard.Application;
import io.dropwizard.auth.AuthDynamicFeature;
import io.dropwizard.auth.AuthValueFactoryProvider;
import io.dropwizard.auth.basic.BasicCredentialAuthFilter;
import io.dropwizard.client.JerseyClientBuilder;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

public class AppExample extends Application<Config> {

	public static void main(String[] args) throws Exception {
		new AppExample().run(args);
	}

	@Override
	public void initialize(final Bootstrap<Config> bootstrap) {
	}

	@Override
	public void run(final Config conf, final Environment env) throws Exception {
		final Injector injector = Guice.createInjector(new AppModule(conf, env), createJpaModule(conf.getDbConfig()));
		env.servlets().addFilter("persistFilter", injector.getInstance(PersistFilter.class));
		env.jersey().register(injector.getInstance(HomeResource.class));
		env.jersey().register(injector.getInstance(PlayerResource.class));
		
		env.jersey().register(injector.getInstance(UserResource.class));
		env.jersey().register(injector.getInstance(AppAuthenticator.class));
		env.jersey().register(injector.getInstance(StaticJWTResource.class));
		env.jersey().register(injector.getInstance(SecretsResource.class));

		// Application health check
		final Client client = new JerseyClientBuilder(env).build("DemoRESTClient");
		env.healthChecks().register("APIHealthCheck", new AppHealthCheck(client));

		// Run multiple health checks
		env.jersey().register(new HealthCheckController(env.healthChecks()));

		// Setup Basic Security
		env.jersey().register(new AuthDynamicFeature(
						new BasicCredentialAuthFilter.Builder<User>().setAuthenticator(injector.getInstance(AppAuthenticator.class))
								.setAuthorizer(new AppAuthorizer()).setRealm("App Security").buildAuthFilter()));
		env.jersey().register(new AuthValueFactoryProvider.Binder<>(User.class));
		env.jersey().register(RolesAllowedDynamicFeature.class);
		
		configureCors(env);
	}

	private JpaPersistModule createJpaModule(final DbConfig dbConfig) {
		final Properties properties = new Properties();
		properties.put("javax.persistence.jdbc.driver", dbConfig.getDriver());
		properties.put("javax.persistence.jdbc.url", dbConfig.getUrl());
		properties.put("javax.persistence.jdbc.user", dbConfig.getUsername());
		properties.put("javax.persistence.jdbc.password", dbConfig.getPassword());

		final JpaPersistModule jpaModule = new JpaPersistModule("DefaultUnit");
		jpaModule.properties(properties);

		return jpaModule;
	}
	
	private void configureCors(Environment environment) {
        final FilterRegistration.Dynamic cors =
                environment.servlets().addFilter("CORS", CrossOriginFilter.class);

        // Configure CORS parameters
        cors.setInitParameter(CrossOriginFilter.ALLOWED_ORIGINS_PARAM, "*");
        cors.setInitParameter(CrossOriginFilter.ALLOWED_HEADERS_PARAM, "X-Requested-With,Content-Type,Accept,Origin,Authorization");
        cors.setInitParameter(CrossOriginFilter.ALLOWED_METHODS_PARAM, "OPTIONS,GET,PUT,POST,DELETE,HEAD");
        cors.setInitParameter(CrossOriginFilter.ALLOW_CREDENTIALS_PARAM, "true");

        // Add URL mapping
        cors.addMappingForUrlPatterns(EnumSet.allOf(DispatcherType.class), true, "/*");

    }
}
