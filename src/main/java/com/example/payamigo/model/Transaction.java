package com.example.payamigo.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Currency;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @ManyToOne
    @JoinColumn(name = "source_id", referencedColumnName = "id", nullable = false)
    private Wallet sourceID;
    @ManyToOne
    @JoinColumn(name = "destination_id", referencedColumnName = "id", nullable = false)
    private Wallet destinationID;
    @Column(nullable = false)
    private float amount;
    @Column(nullable = false)
    private float commissionPercent;
    @Column(nullable = false)
    private float commissionAmount;
    @Column(nullable = false)
    private Currency currency;
    @Column(nullable = false)
    private Date createdAt;
}
