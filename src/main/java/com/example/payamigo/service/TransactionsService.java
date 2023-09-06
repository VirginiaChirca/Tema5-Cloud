package com.example.payamigo.service;

import com.example.payamigo.model.Transaction;
import com.example.payamigo.repository.TransactionsRepository;
import org.hibernate.TransactionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Date;

@Service
public class TransactionsService {

    private final TransactionsRepository transactionRepository;

    @Autowired
    public TransactionsService(TransactionsRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    public Transaction createTransaction(Transaction transaction) {
        Date currentDate = new Date(System.currentTimeMillis());
        BigDecimal sourceBalance = BigDecimal.valueOf(transaction.getSourceID().getBalance());
        BigDecimal transactionAmount = BigDecimal.valueOf(transaction.getAmount());

        if (transactionAmount.compareTo(sourceBalance) > 0) {
            throw new TransactionException("Transaction IMPOSSIBLE");
        }
        if (transaction.getDestinationID() == null) {
            throw new TransactionException("Destination wallet does not exist.");
        }
        if (transaction.getSourceID() == null) {
            throw new TransactionException("Source wallet does not exist.");
        }

        if (transaction.getSourceID().equals(transaction.getDestinationID())) {
            throw new TransactionException("User cannot pay itself.");
        }
        if (transaction.getAmount() < 0) {
            throw new TransactionException("Transaction amount cannot be negative.");
        }
        if (transaction.getCreatedAt().before(currentDate)) {
            throw new TransactionException("You cannot make a past payment.");
        }

        BigDecimal newSourceBalance = sourceBalance.subtract(transactionAmount);
        transaction.getSourceID().setBalance(newSourceBalance.doubleValue());
        return transactionRepository.save(transaction);
    }

    public void deleteTransaction(Integer transactionId) {
        transactionRepository.deleteById(transactionId);
    }

}