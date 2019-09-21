package com.revolut.repository;

import com.revolut.domain.AccountInfo;
import com.revolut.domain.AccountTransactionInfo;

import java.math.BigDecimal;
import java.util.List;

public interface AccountInfoRepo {
  AccountInfo saveAccount(AccountInfo accountInfo);

  AccountInfo getAccountById(Long id);

  AccountInfo getAccountByEmail(String emailAddress);

  List<AccountInfo> getAllAccounts();

  void deleteAccount(Long id);

  boolean doesAccountExistById(Long id);

  void updateAccountBalancesAndTransactionLog(AccountInfo updatedSenderAccountBalance,
                                              AccountInfo updatedReceiverAccountBalance,
                                              AccountTransactionInfo accountTransactionInfo);
}
