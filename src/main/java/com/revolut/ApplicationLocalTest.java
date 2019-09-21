package com.revolut;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.revolut.controllers.Account;
import com.revolut.module.AppModule;
import spark.Filter;
import spark.Spark;

import java.util.HashMap;

import static spark.Spark.*;

public class ApplicationLocalTest {
  private static final int MAIN_PORT = 8080;

  public static void main(String[] args) {
    setupPortNumber();
    applyCORSFilter();
    initializeRoutes();
  }

  private static void initializeRoutes() {
    Injector injector = Guice.createInjector(new AppModule());
    Account account = injector.getInstance(Account.class);

    post("/accounts", account::createAccount);
    get("/accounts", account::getAllAccounts);
    get("/accounts/:id", account::getAccountById);
    delete("/accounts/:id", account::deleteAccountById);
    post("/accounts/:id/transactions", account::createAccountTransaction);
    get("/accounts/:id/transactions", account::getAllTransactionsOfAccount);
  }

  private static void setupPortNumber() {
    port(MAIN_PORT);
  }

  public static void startApp() {
    ApplicationLocalTest.main(null);
    awaitInitialization();
  }

  public static void stopApp() {
    stop();
    awaitStop();
  }

  private static final HashMap<String, String> corsHeaders = new HashMap<>();

  static {
    corsHeaders.put("Access-Control-Allow-Methods", "GET,PUT,POST,DELETE,OPTIONS");
    corsHeaders.put("Access-Control-Allow-Origin", "*");
    corsHeaders.put("Access-Control-Allow-Headers", "Content-Type," +
            "Authorization,X-Requested-With,Content-Length,Accept,Origin,");
    corsHeaders.put("Access-Control-Allow-Credentials", "true");
  }

  private static void applyCORSFilter() {
    Filter filter =
            (request, response) -> corsHeaders.forEach((key, value) -> response.header(key, value));
    Spark.after(filter);
  }
}
