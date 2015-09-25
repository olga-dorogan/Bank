package com.custom.controller;

import com.custom.model.Account;
import com.custom.model.Client;
import com.custom.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

/**
 * Created by olga on 24.09.15.
 */
@RestController
@RequestMapping("/client/{clientId}/account")
public class AccountController {
    @Autowired
    private ClientService clientService;

    @RequestMapping(method = RequestMethod.GET, produces = "application/json")
    public Client findClientWithAccounts(@PathVariable Integer clientId) {
        return clientService.findClientWithAccounts(clientId);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(method = RequestMethod.POST, consumes = "application/json")
    public void save(@PathVariable Integer clientId, @RequestBody Account account) {
        Client client = new Client(clientId);
        account.setClient(client);
        clientService.createAccount(account);
    }
}
