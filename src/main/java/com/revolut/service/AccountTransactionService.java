package com.revolut.service;

import com.revolut.domain.AccountTransactionInfo;
import com.revolut.model.ApplicationOpResponse;
import com.revolut.model.ErrorApplicationOpResponse;
import com.revolut.model.SuccessOpWithJSONResponse;
import com.revolut.repository.AccountInfoRepo;
import com.revolut.repository.AccountTransactionRepo;
import com.revolut.util.JsonParser;

import javax.inject.Inject;
import java.util.List;


public class AccountTransactionService {
  private AccountInfoRepo accountInfoRepo;
  private AccountTransactionRepo accountTransactionRepo;
  private JsonParser jsonParser;

  @Inject
  public AccountTransactionService(AccountInfoRepo accountInfoRepo,
                                   AccountTransactionRepo accountTransactionRepo,
                                   JsonParser jsonParser) {
    this.accountInfoRepo = accountInfoRepo;
    this.accountTransactionRepo = accountTransactionRepo;
    this.jsonParser = jsonParser;
  }

  public ApplicationOpResponse getAccountTransactionsByAccountId(String accountId) {
    if (accountInfoRepo.doesAccountExistById(Long.valueOf(accountId))) {

      List<AccountTransactionInfo> accountTransactionEntities = accountTransactionRepo.
              getAccountTransactionsByAccountId(Long.valueOf(accountId));

      String messageBodyWhenListIsEmpty = "[]";
      String messageBodyWhenListItemsAreAvailable =
              jsonParser.toJSONString(accountTransactionEntities);

      return accountTransactionEntities.isEmpty()
              ? new SuccessOpWithJSONResponse(200, messageBodyWhenListIsEmpty)
              : new SuccessOpWithJSONResponse(200, messageBodyWhenListItemsAreAvailable);

    }
    return new ErrorApplicationOpResponse(404, "Account with id = " + accountId + " does not exist");
  }
}
