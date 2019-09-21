package com.revolut.module;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;

import javax.inject.Singleton;

import com.revolut.controllers.Account;
import com.revolut.controllers.AccountImpl;
import com.revolut.repository.AccountInfoRepo;
import com.revolut.repository.AccountInfoRepoImpl;
import com.revolut.repository.AccountTransactionRepo;
import com.revolut.repository.AccountTransactionRepoImpl;
import com.revolut.util.JsonParser;
import com.revolut.util.JsonParserImpl;
import com.revolut.util.ResponseCreator;
import com.revolut.util.ResponseCreatorImpl;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class AppModule extends AbstractModule {
  private static final ThreadLocal<EntityManager> ENTITY_MANAGER_CACHE =
          new ThreadLocal<>();

  @Override
  protected void configure() {
    bind(Account.class).to(AccountImpl.class);
    bind(AccountInfoRepo.class).to(AccountInfoRepoImpl.class);
    bind(JsonParser.class).to(JsonParserImpl.class);
    bind(ResponseCreator.class).to(ResponseCreatorImpl.class);
    bind(AccountTransactionRepo.class).to(AccountTransactionRepoImpl.class);
  }

  @Provides
  @Singleton
  public EntityManagerFactory createEntityManagerFactory() {
    return Persistence.createEntityManagerFactory("db-manager");
  }

  @Provides
  public EntityManager createEntityManager(
          EntityManagerFactory entityManagerFactory) {
    EntityManager entityManager = ENTITY_MANAGER_CACHE.get();
    if (entityManager == null) {
      ENTITY_MANAGER_CACHE.set(entityManager = entityManagerFactory
              .createEntityManager());
    }
    return entityManager;
  }
}
