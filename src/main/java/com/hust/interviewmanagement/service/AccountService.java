package com.hust.interviewmanagement.service;

import com.hust.interviewmanagement.entities.Account;

public interface AccountService {
    Account findAccountByEmail(String email);
    boolean existedAccountByEmail(String email);
}
