package com.example;

import javax.ws.rs.client.Client;

import org.glassfish.jersey.server.filter.RolesAllowedDynamicFeature;
import org.skife.jdbi.v2.DBI;

import com.example.core.Employee;
import com.example.dao.EmployeeDAO;
import com.example.dao.EmployeeHibernetDAO;
import com.example.dao.PersonDAO;
import com.example.resources.EmployeeRESTController;
import com.example.resources.EmployeeRESTControllerWithSec;
import com.example.resources.PersonResource;
import com.example.rest.basicauth.AppAuthenticator;
import com.example.rest.basicauth.AppAuthorizer;
import com.example.rest.basicauth.User;
import com.example.rest.healthcheck.AppHealthCheck;
import com.example.rest.healthcheck.HealthCheckController;

import io.dropwizard.Application;
import io.dropwizard.auth.AuthDynamicFeature;
import io.dropwizard.auth.AuthValueFactoryProvider;
import io.dropwizard.auth.basic.BasicCredentialAuthFilter;
import io.dropwizard.client.JerseyClientBuilder;
import io.dropwizard.configuration.EnvironmentVariableSubstitutor;
import io.dropwizard.configuration.SubstitutingSourceProvider;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.hibernate.HibernateBundle;
import io.dropwizard.jdbi.DBIFactory;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;

public class ExampleApplication extends Application<ExampleConfiguration> {
	public static void main(String[] args) throws Exception {
		new ExampleApplication().run(args);
	}

	private final HibernateBundle<ExampleConfiguration> hibernateBundle = new HibernateBundle<ExampleConfiguration>(Employee.class) {
		@Override
		public DataSourceFactory getDataSourceFactory(ExampleConfiguration configuration) {
			return configuration.getDataSourceFactory();
		}
	};

	@Override
	public String getName() {
		return "dropwizard-jdbi";
	}

	@Override
	public void initialize(Bootstrap<ExampleConfiguration> bootstrap) {
		// Enable variable substitution with environment variables
        bootstrap.setConfigurationSourceProvider(
                new SubstitutingSourceProvider(
                        bootstrap.getConfigurationSourceProvider(),
                        new EnvironmentVariableSubstitutor(false)
                )
        );

        /*bootstrap.addCommand(new RenderCommand());
        bootstrap.addBundle(new AssetsBundle());
        bootstrap.addBundle(new MigrationsBundle<ExampleConfiguration>() {
            @Override
            public DataSourceFactory getDataSourceFactory(ExampleConfiguration configuration) {
                return configuration.getDataSourceFactory();
            }
        });*/
        //bootstrap.addBundle(hibernateBundle);
	}

	@Override
	public void run(ExampleConfiguration configuration, Environment environment) throws ClassNotFoundException {
		final DBIFactory factory = new DBIFactory();
		final DBI jdbi = factory.build(environment, configuration.getDataSourceFactory(), "h2");

		final PersonDAO personDAO = jdbi.onDemand(PersonDAO.class);
		final PersonResource personResource = new PersonResource(personDAO);
		environment.jersey().register(personResource);

		final EmployeeDAO employeeDAO = jdbi.onDemand(EmployeeDAO.class);
		final EmployeeHibernetDAO employeeHibernetDAO = null; //new EmployeeHibernetDAO(hibernateBundle.getSessionFactory());
		
		environment.jersey().register(new EmployeeRESTControllerWithSec(environment.getValidator(), employeeDAO, employeeHibernetDAO));
		environment.jersey().register(new EmployeeRESTController(environment.getValidator(), employeeDAO, employeeHibernetDAO));

		// Application health check
		final Client client = new JerseyClientBuilder(environment).build("DemoRESTClient");
		environment.healthChecks().register("APIHealthCheck", new AppHealthCheck(client));

		// Run multiple health checks
		environment.jersey().register(new HealthCheckController(environment.healthChecks()));

		// Setup Basic Security
		environment.jersey().register(new AuthDynamicFeature(
						new BasicCredentialAuthFilter.Builder<User>().setAuthenticator(new AppAuthenticator(null))
								.setAuthorizer(new AppAuthorizer()).setRealm("App Security").buildAuthFilter()));
		environment.jersey().register(new AuthValueFactoryProvider.Binder<>(User.class));
		environment.jersey().register(RolesAllowedDynamicFeature.class);
	}
}