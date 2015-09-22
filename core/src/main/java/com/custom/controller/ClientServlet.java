package com.custom.controller;

import com.custom.exception.BankDAOException;
import com.custom.exception.BankException;
import com.custom.model.Account;
import com.custom.model.Client;
import com.custom.service.ClientService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by olga on 17.09.15.
 */
@WebServlet(name = "clientServlet", urlPatterns = {"/client/*"})
public class ClientServlet extends HttpServlet {
    private ClientService clientService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        ApplicationContext ctx = (ApplicationContext) config.getServletContext()
                .getAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE);
        clientService = ctx.getBean(ClientService.class);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String result;
        Gson gson = new GsonBuilder().create();
        if (isQueryForAccount(req)) {
            Client clientWithAccounts = clientService.findClientWithAccounts(getClientIdFromQuery(req));
            result = gson.toJson(clientWithAccounts);
        } else {
            result = gson.toJson(clientService.findAll());
        }
        PrintWriter out = resp.getWriter();
        out.print(result);
        out.flush();
        out.close();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Gson gson = new GsonBuilder().create();
        try {
            if (isQueryForAccount(req)) {
                Account account = gson.fromJson(req.getReader(), Account.class);
                int clientId = getClientIdFromQuery(req);
                Client client = new Client(clientId);
                account.setClient(client);
                clientService.createAccount(account);
                resp.setStatus(HttpServletResponse.SC_CREATED);
                return;
            } else {
                Client client = gson.fromJson(req.getReader(), Client.class);
                clientService.create(client);
                resp.setStatus(HttpServletResponse.SC_CREATED);
                return;
            }
        } catch (BankDAOException e) {
            PrintWriter out = resp.getWriter();
            out.print(e.getMessage());
            out.close();
            resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return;
        }
    }

    private boolean isQueryForAccount(HttpServletRequest req) {
        return (req.getPathInfo() != null) && (req.getPathInfo().split("/").length > 1);
    }

    private int getClientIdFromQuery(HttpServletRequest req) {
        if (isQueryForAccount(req)) {
            return Integer.valueOf(req.getPathInfo().split("/")[1]);
        }
        throw new BankException("Illegal path exception");
    }


}
