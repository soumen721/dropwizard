package com.sou.dw.jpa;

import com.google.inject.AbstractModule;
import com.google.inject.Singleton;
import com.sou.dw.jpa.config.Config;
import com.sou.dw.jpa.dao.Dao;
import com.sou.dw.jpa.dao.DaoImpl;
import com.sou.dw.jpa.service.PlayerService;
import com.sou.dw.jpa.service.ScoreService;

import io.dropwizard.setup.Environment;
import io.jsonwebtoken.jjwtfun.service.SecretService;

public class AppModule extends AbstractModule {

    final Config configuration;
    final Environment environment;

    public AppModule(final Config configuration, final Environment environment) {
        this.configuration = configuration;
        this.environment = environment;
    }

    @Override
    protected void configure() {
        bind(Config.class).toInstance(configuration);
        bind(Environment.class).toInstance(environment);
        bind(Dao.class).to(DaoImpl.class).in(Singleton.class);
        bind(PlayerService.class).in(Singleton.class);
        bind(ScoreService.class).in(Singleton.class);
        bind(SecretService.class).in(Singleton.class);
    }
}
