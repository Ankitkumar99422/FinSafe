package com.finsafe.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Entity
@Table(name = "accounts")
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "account_name")
    private String accountName;

    @Column(name = "current_balance", nullable = false)
    private BigDecimal currentBalance;

    @Column(nullable = false)
    private String currency;

    @Column(name = "last_synced_at", insertable = false, updatable = false)
    private OffsetDateTime lastSyncedAt;

    public Account() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getUserId() { return userId; }
    public void setUserId(Long userId) { this.userId = userId; }

    public String getAccountName() { return accountName; }
    public void setAccountName(String accountName) { this.accountName = accountName; }

    public BigDecimal getCurrentBalance() { return currentBalance; }
    public void setCurrentBalance(BigDecimal currentBalance) { this.currentBalance = currentBalance; }

    public String getCurrency() { return currency; }
    public void setCurrency(String currency) { this.currency = currency; }

    public OffsetDateTime getLastSyncedAt() { return lastSyncedAt; }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private Long id;
        private Long userId;
        private String accountName;
        private BigDecimal currentBalance;
        private String currency;

        public Builder id(Long id) { this.id = id; return this; }
        public Builder userId(Long userId) { this.userId = userId; return this; }
        public Builder accountName(String accountName) { this.accountName = accountName; return this; }
        public Builder currentBalance(BigDecimal currentBalance) { this.currentBalance = currentBalance; return this; }
        public Builder currency(String currency) { this.currency = currency; return this; }

        public Account build() {
            Account account = new Account();
            account.setId(this.id);
            account.setUserId(this.userId);
            account.setAccountName(this.accountName);
            account.setCurrentBalance(this.currentBalance);
            account.setCurrency(this.currency);
            return account;
        }
    }
}
