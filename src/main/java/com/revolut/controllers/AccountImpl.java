package com.revolut.controllers;

import com.revolut.domain.AccountInfo;
import com.revolut.domain.AccountTransactionInfo;
import com.revolut.service.AccountService;
import com.revolut.service.AccountTransactionService;
import com.revolut.util.JsonParser;
import com.revolut.util.ResponseCreator;
import spark.Request;
import spark.Response;

import javax.inject.Inject;

public class AccountImpl implements Account {
  private AccountService accountService;
  private JsonParser jsonParser;
  private ResponseCreator responseCreator;
  private AccountTransactionService accountTransactionService;

  @Inject
  AccountImpl(AccountService accountService, JsonParser jsonParser,
              ResponseCreator responseCreator,
              AccountTransactionService accountTransactionService) {
    this.accountService = accountService;
    this.jsonParser = jsonParser;
    this.responseCreator = responseCreator;
    this.accountTransactionService = accountTransactionService;
  }

  @Override
  public String createAccount(Request request, Response response) {
    AccountInfo accountInfo =
            jsonParser.toJsonPOJO(request.body(), AccountInfo.class);
    return responseCreator
            .respondToHttpEndpoint(response, accountService.createAccount(accountInfo));
  }

  @Override
  public String getAllAccounts(Request request, Response response) {
    return responseCreator
            .respondToHttpEndpoint(response, accountService.getAllAccounts());
  }

  @Override
  public String getAccountById(Request request, Response response) {
    return responseCreator
            .respondToHttpEndpoint(response,
                    accountService.getAccountById(request.params("id")));
  }

  @Override
  public String deleteAccountById(Request request, Response response) {
    return responseCreator
            .respondToHttpEndpoint(response,
                    accountService.deleteAccountById(request.params("id")));
  }

  @Override
  public String createAccountTransaction(Request request, Response response) throws Exception {
    AccountTransactionInfo accountTransactionInfo =
            jsonParser.toJsonPOJO(request.body(), AccountTransactionInfo.class);

    String accountId = request.params("id");

    return responseCreator
            .respondToHttpEndpoint(response,
                    accountService.createAccountTransaction(accountId,
                            accountTransactionInfo));
  }

  @Override
  public String getAllTransactionsOfAccount(Request request, Response response) {
    String accountId = request.params("id");

    return responseCreator
            .respondToHttpEndpoint(response,
                    accountTransactionService.getAccountTransactionsByAccountId(accountId));
  }

}
