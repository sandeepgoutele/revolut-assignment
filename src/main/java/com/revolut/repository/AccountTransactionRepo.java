package com.revolut.repository;

import com.revolut.domain.AccountTransactionInfo;
import com.revolut.domain.TransactionStatus;

import java.util.List;

public interface AccountTransactionRepo {
  void createAccountTransaction(AccountTransactionInfo accountTransactionInfo,
                                TransactionStatus transactionStatus,
                                String reason) throws Exception;

  List<AccountTransactionInfo> getAccountTransactionsByAccountId(Long accountId);
}
