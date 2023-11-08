package com.jochmen.account.service;

import com.jochmen.account.controller.schema.response.AccessCodeResponse;
import com.jochmen.account.controller.schema.request.AccountCreationRequest;
import com.jochmen.account.repository.AccountDatabaseEntity;
import com.jochmen.account.repository.AccountsRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class AccountsService {

    private final AccountsRepository accountsRepository;
    private final AccessCodeService accessCodeService;

    public AccountsService(AccountsRepository accountsRepository, AccessCodeService accessCodeService) {
        this.accountsRepository = accountsRepository;
        this.accessCodeService = accessCodeService;
    }

    public Optional<AccessCodeResponse> createAccount(AccountCreationRequest account) {
        if (accountsRepository.existsByEmail(account.email())) {
            return Optional.empty();
        }
        var accountEntity = accountsRepository.save(new AccountDatabaseEntity(account.email(), account.password()));
        return Optional.of(accessCodeService.createAccessCode(accountEntity.getId()));
    }

    public Optional<UUID> isAccessCodeValid(AccessCodeResponse accessCodeResponse) {
        return accessCodeService.verifyAccessCode(accessCodeResponse)
                .flatMap(accountsRepository::findById)
                .map(AccountDatabaseEntity::getId);
    }

    public Optional<AccessCodeResponse> createToken(AccountCreationRequest account) {
        return accountsRepository.findByEmailAndPassword(account.email(), account.password())
                .map(accountDatabaseEntity -> accessCodeService.createAccessCode(accountDatabaseEntity.getId()));
    }
}
