package com.jochmen.account.controller;


import com.jochmen.account.controller.schema.response.AccessCode;
import com.jochmen.account.controller.schema.request.Account;
import com.jochmen.account.service.AccountsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/accounts")
public class AccountsController {

    private final AccountsService accountsService;

    public AccountsController(AccountsService accountsService) {
        this.accountsService = accountsService;
    }

    @PostMapping
    public ResponseEntity<AccessCode> createAccount(@RequestBody Account account) {
        return accountsService.createAccount(account)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.badRequest().build());
    }

    @PostMapping("/access-codes")
    public ResponseEntity<AccessCode> createToken(@RequestBody Account account) {
        return accountsService.createToken(account)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.badRequest().build());
    }

    @GetMapping("/access-codes/validate")
    public ResponseEntity<Void> validateAccessCode(@RequestParam("code") String accessCode) {
        return accountsService.isAccessCodeValid(new AccessCode(accessCode)) ?
                ResponseEntity.noContent().build() :
                ResponseEntity.notFound().build();
    }



}
