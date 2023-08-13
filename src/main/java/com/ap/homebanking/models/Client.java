package com.ap.homebanking.models;

import net.minidev.json.annotate.JsonIgnore;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "native")
    @GenericGenerator(name = "native", strategy = "native")
    private long id;

    private String firstName;

    private String lastName;

    private String email;

    @OneToMany(mappedBy = "client", fetch = FetchType.EAGER)
    private Set<Account> accounts = new HashSet<>();

    // Relationship between Client and Loan
    @OneToMany(mappedBy="client", fetch=FetchType.EAGER)
    private Set<ClientLoan> clientLoans = new HashSet<>();

    @OneToMany(mappedBy = "client", fetch = FetchType.EAGER)
    private Set<Card> cards = new HashSet<>();


    public Client() {
    }

    public Client(String firstName, String lastName, String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
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

    public Set<Account> getAccounts() {
        return accounts;
    }

    public Set<Loan> getLoans() {
        return this.clientLoans.stream().map(ClientLoan::getLoan).collect(Collectors.toSet());
    }

    public Set<Card> getCards() {
        return this.cards;
    }

    @JsonIgnore
    public Set<ClientLoan> getClientLoans() { return clientLoans; }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void addAccount(Account account) {
        account.setOwner(this);
        accounts.add(account);
    }

    public void addClientLoan(ClientLoan clientLoan) {
        this.clientLoans.add(clientLoan);
    }

    public void addCard(Card card) {
        card.setClient(this);
        this.cards.add(card);
    }
}
