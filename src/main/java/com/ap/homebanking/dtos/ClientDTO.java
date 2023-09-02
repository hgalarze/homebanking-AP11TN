package com.ap.homebanking.dtos;

import com.ap.homebanking.models.Client;

import java.util.HashSet;
import java.util.Set;

import static java.util.stream.Collectors.toSet;

public class ClientDTO {

    private final long id;
    private final String firstName;
    private final String lastName;
    private final String email;
    private Set<AccountDTO> accounts = new HashSet<>();
    private Set<ClientLoanDTO> loans = new HashSet<>();
    private Set<CardDTO> cards = new HashSet<>();

    public ClientDTO(Client client) {

        this.id = client.getId();
        this.firstName = client.getFirstName();
        this.lastName = client.getLastName();
        this.email = client.getEmail();
        this.accounts = client.getAccounts().stream().map(AccountDTO::new).collect(toSet());
        this.loans = client.getClientLoans().stream().map(ClientLoanDTO::new).collect(toSet());
        this.cards = client.getCards().stream().map(CardDTO::new).collect(toSet());
    }

    public long getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    public Set<AccountDTO> getAccounts() {
        return accounts;
    }

    public Set<ClientLoanDTO> getLoans() { return loans; }

    public Set<CardDTO> getCards() { return cards; }
}
