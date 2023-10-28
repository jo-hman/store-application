package com.jochmen.account.service;

import com.jochmen.account.controller.schema.response.AccessCode;
import com.jochmen.account.controller.schema.request.Account;
import com.jochmen.account.repository.AccountDatabaseEntity;
import com.jochmen.account.repository.AccountsRepository;
import com.netflix.appinfo.ApplicationInfoManager;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AccountsService {

    private final AccountsRepository accountsRepository;
    private final AccessCodeService accessCodeService;

    public AccountsService(AccountsRepository accountsRepository, AccessCodeService accessCodeService) {
        this.accountsRepository = accountsRepository;
        this.accessCodeService = accessCodeService;
    }

    public Optional<AccessCode> createAccount(Account account) {
        if (accountsRepository.existsByEmail(account.email())) {
            return Optional.empty();
        }
        var accountEntity = accountsRepository.save(new AccountDatabaseEntity(account.email(), account.password()));
        return Optional.of(accessCodeService.createAccessCode(accountEntity.getId()));
    }

    public boolean isAccessCodeValid(AccessCode accessCode) {
        return accessCodeService.verifyAccessCode(accessCode)
                .map(accountsRepository::existsById)
                .orElse(false);
    }

    public Optional<AccessCode> createToken(Account account) {
        return accountsRepository.findByEmailAndPassword(account.email(), account.password())
                .map(accountDatabaseEntity -> accessCodeService.createAccessCode(accountDatabaseEntity.getId()));
    }
}
