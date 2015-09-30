package com.custom.app.controller;

import com.custom.app.dto.Client;
import com.custom.app.service.ClientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by olga on 24.09.15.
 */
@RestController
@RequestMapping("/client")
public class ClientController {
    @Autowired
    private ClientService clientService;

    @RequestMapping(method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public List<Client> findAll() {
        return clientService.findAll();
    }

    @RequestMapping(value = "/{clientId}", method = RequestMethod.GET, produces = "application/json")
    @ResponseBody
    public Client findById(@PathVariable int clientId) {
        return clientService.findById(clientId);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(method = RequestMethod.POST, consumes = "application/json")
    public void save(@RequestBody Client client){
        clientService.create(client);
    }
}
