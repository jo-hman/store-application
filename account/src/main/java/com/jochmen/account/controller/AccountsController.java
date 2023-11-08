package com.jochmen.account.controller;


import com.jochmen.account.controller.schema.response.AccessCodeResponse;
import com.jochmen.account.controller.schema.request.AccountCreationRequest;
import com.jochmen.account.controller.schema.response.AccountIdResponse;
import com.jochmen.account.service.AccountsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/accounts")
public class AccountsController {

    private final AccountsService accountsService;

    public AccountsController(AccountsService accountsService) {
        this.accountsService = accountsService;
    }

    @PostMapping
    public ResponseEntity<AccessCodeResponse> createAccount(@RequestBody AccountCreationRequest account) {
        return accountsService.createAccount(account)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.badRequest().build());
    }

    @PostMapping("/access-codes")
    public ResponseEntity<AccessCodeResponse> createToken(@RequestBody AccountCreationRequest account) {
        return accountsService.createToken(account)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.badRequest().build());
    }

    @GetMapping("/access-codes/validate")
    public ResponseEntity<AccountIdResponse> validateAccessCode(@RequestParam("code") String accessCode) {
        return accountsService.isAccessCodeValid(new AccessCodeResponse(accessCode))
                .map(id -> ResponseEntity.ok(new AccountIdResponse(id)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

}
