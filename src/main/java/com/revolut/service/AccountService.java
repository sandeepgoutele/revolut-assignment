package com.revolut.service;

import com.revolut.domain.AccountInfo;
import com.revolut.domain.AccountTransactionInfo;
import com.revolut.domain.TransactionStatus;
import com.revolut.model.ApplicationOpResponse;
import com.revolut.model.ErrorApplicationOpResponse;
import com.revolut.model.SuccessOpWithEmptyResponse;
import com.revolut.model.SuccessOpWithJSONResponse;
import com.revolut.repository.AccountInfoRepo;
import com.revolut.repository.AccountTransactionRepo;
import com.revolut.util.JsonParser;

import javax.inject.Inject;
import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import java.math.BigDecimal;
import java.util.*;

public class AccountService {
  private final AccountInfoRepo accountInfoRepo;
  private final AccountTransactionRepo accountTransactionRepo;
  private JsonParser jsonParser;

  @Inject
  AccountService(AccountInfoRepo accountInfoRepo,
                 AccountTransactionRepo accountTransactionRepo,
                 JsonParser jsonParser) {
    this.accountInfoRepo = accountInfoRepo;
    this.accountTransactionRepo = accountTransactionRepo;
    this.jsonParser = jsonParser;
  }

  public ApplicationOpResponse createAccount(AccountInfo accountInfo) {

    Set<ConstraintViolation<AccountInfo>> constraintViolations =
            Validation.buildDefaultValidatorFactory().getValidator().validate(accountInfo);

    if (constraintViolations.size() == 0) {
      this.accountInfoRepo.saveAccount(accountInfo);
      return new SuccessOpWithEmptyResponse(201);
    }
    return new ErrorApplicationOpResponse(400, constraintViolations.iterator().next().getMessage());
  }

  public ApplicationOpResponse createAccountTransaction(String senderAccountId, AccountTransactionInfo accountTransactionInfo) throws Exception {
    Set<ConstraintViolation<AccountTransactionInfo>> constraintViolations =
            Validation.buildDefaultValidatorFactory().getValidator().validate(accountTransactionInfo);

    accountTransactionInfo.setSendingAccountId(Long.valueOf(senderAccountId));

    if (constraintViolations.size() == 0) {
      Long receiverAccountId = accountTransactionInfo.getReceivingAccountId();
      if (accountInfoRepo.doesAccountExistById(Long.valueOf(senderAccountId)) && accountInfoRepo.doesAccountExistById(receiverAccountId)) {

        if (canUserInitiateMoneyTransfer(accountTransactionInfo)) {

          try {
            updateUserAccountBalancesInDatastore(accountTransactionInfo);
          } catch (Exception ex) {
            return new ErrorApplicationOpResponse(500, "Could not complete request");
          }
          return new SuccessOpWithEmptyResponse(201);
        }
        String reasonForFailure = "Not Enough Balance to initiate transaction";
        createAccountTransactionInDatastore(accountTransactionInfo, TransactionStatus.FAILED, reasonForFailure);
        return new ErrorApplicationOpResponse(403, reasonForFailure);//we dont want to expose financial information with "not enough balance"
      }
      return new ErrorApplicationOpResponse(404, "Account with id = " + senderAccountId + " does not exist.");
    }
    String errorMessage = constraintViolations.iterator().next().getMessage();
    return new ErrorApplicationOpResponse(400, errorMessage);
  }

  public ApplicationOpResponse getAllAccounts() {
    List<AccountInfo> accountEntities = this.accountInfoRepo.getAllAccounts();
    if (accountEntities.isEmpty())
      return new SuccessOpWithJSONResponse(200, "[]");
    return new SuccessOpWithJSONResponse(200, this.jsonParser.toJSONString(accountEntities));
  }

  public ApplicationOpResponse getAccountById(String id) {
    AccountInfo accountInfo = this.accountInfoRepo.getAccountById(Long.valueOf(id));
    if (Objects.nonNull(accountInfo))
      return new SuccessOpWithJSONResponse(200, this.jsonParser.toJSONString(accountInfo));
    return new ErrorApplicationOpResponse(404, "Account with id=" + id + " not found.");
  }

  public ApplicationOpResponse deleteAccountById(String id) {
    if (Objects.nonNull(accountInfoRepo.getAccountById(Long.valueOf(id)))) {

      accountInfoRepo.deleteAccount(Long.valueOf(id));
      return new SuccessOpWithEmptyResponse(204);
    }
    return new ErrorApplicationOpResponse(404, "Account with id =" + id + " not found.");
  }

  private boolean canUserInitiateMoneyTransfer(AccountTransactionInfo accountTransactionInfo) {
    AccountInfo senderAccount =
            accountInfoRepo.getAccountById(accountTransactionInfo.getSendingAccountId());

    BigDecimal transactionAmount =
            accountTransactionInfo.getTransactionAmount();

    int comparisonResult =
            senderAccount.getAccountBalance().compareTo(transactionAmount);
    return comparisonResult >= 0;
  }

  private void updateUserAccountBalancesInDatastore(AccountTransactionInfo accountTransactionInfo) {
    AccountInfo senderAccount =
            accountInfoRepo.getAccountById(accountTransactionInfo.getSendingAccountId());

    AccountInfo receiverAccount =
            accountInfoRepo.getAccountById(accountTransactionInfo.getReceivingAccountId());

    BigDecimal transactionAmount = accountTransactionInfo.getTransactionAmount();


    AccountInfo updatedSenderAccountBalance = debitAccountEntity(senderAccount, transactionAmount);
    AccountInfo updatedRecieverAccountBalance = creditAccountEntity(receiverAccount, transactionAmount);

    accountInfoRepo
            .updateAccountBalancesAndTransactionLog(updatedSenderAccountBalance
                    , updatedRecieverAccountBalance
                    , accountTransactionInfo);
  }

  private AccountInfo creditAccountEntity(AccountInfo accountInfo, BigDecimal amountToCredit) {

    BigDecimal currentBalanceBeforeAddition = accountInfo.getAccountBalance();

    BigDecimal currentBalanceAfterAddition = currentBalanceBeforeAddition.add(amountToCredit);

    accountInfo.setAccountBalance(currentBalanceAfterAddition);

    return accountInfo;
  }

  private AccountInfo debitAccountEntity(AccountInfo accountInfo, BigDecimal amountToDebit) {

    BigDecimal currentBalanceBeforeDebit = accountInfo.getAccountBalance();

    BigDecimal currentBalanceAfterDebit = currentBalanceBeforeDebit.subtract(amountToDebit);

    accountInfo.setAccountBalance(currentBalanceAfterDebit);

    return accountInfo;
  }

  private void createAccountTransactionInDatastore(AccountTransactionInfo accountTransactionInfo, TransactionStatus transactionStatus, String reason) throws Exception {
    accountTransactionRepo.createAccountTransaction(accountTransactionInfo, transactionStatus, reason);
  }
}
