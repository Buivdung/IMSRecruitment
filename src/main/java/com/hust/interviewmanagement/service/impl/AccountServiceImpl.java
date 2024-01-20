package com.hust.interviewmanagement.service.impl;

import com.hust.interviewmanagement.entities.Account;
import com.hust.interviewmanagement.enums.EMessageUser;
import com.hust.interviewmanagement.repository.AccountRepository;
import com.hust.interviewmanagement.service.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountServiceImpl implements AccountService {
    private final AccountRepository accountRepository;

    @Override
    public Account findAccountByEmail(String email) {
        return accountRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(EMessageUser.DONT_USER.getValue()));
    }

    @Override
    public boolean existedAccountByEmail(String email) {
        return accountRepository.existsByEmail(email);
    }
}
