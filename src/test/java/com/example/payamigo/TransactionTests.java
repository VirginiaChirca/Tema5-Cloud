package com.example.payamigo;

import com.example.payamigo.model.Transaction;
import com.example.payamigo.model.User;
import com.example.payamigo.model.Wallet;
import com.example.payamigo.repository.TransactionsRepository;
import com.example.payamigo.repository.UserRepository;
import com.example.payamigo.service.TransactionsService;
import com.example.payamigo.service.UsersService;
import org.hibernate.TransactionException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class TransactionTests {
    @Mock
    private TransactionsRepository transactionRepository;

    @InjectMocks
    private TransactionsService transactionService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void TransactionAmountHigherThanBalance() {
        Wallet sourceWallet = new Wallet();
        sourceWallet.setBalance(100);
        Wallet destinationWallet = new Wallet();
        destinationWallet.setBalance(50);
        Transaction transaction = new Transaction();
        transaction.setSourceID(sourceWallet);
        transaction.setDestinationID(destinationWallet);
        transaction.setAmount(150);

        assertThrows(TransactionException.class, () -> {
            transactionService.createTransaction(transaction);
        });
    }
    @Test
    public void DestinationWalletDoesNotExist() {
        Wallet sourceWallet = new Wallet();
        Wallet destinationWallet = null;
        Transaction transaction = new Transaction();
        transaction.setSourceID(sourceWallet);
        transaction.setDestinationID(destinationWallet);

        assertThrows(TransactionException.class, () -> {
            transactionService.createTransaction(transaction);
        });
    }
    @Test
    public void SourceWalletDoesNotExist() {
        Wallet sourceWallet = null;
        Wallet destinationWallet = new Wallet();
        Transaction transaction = new Transaction();
        transaction.setSourceID(sourceWallet);
        transaction.setDestinationID(destinationWallet);

        assertThrows(NullPointerException.class, () -> {
            transactionService.createTransaction(transaction);
        });
    }

    @Test
    public void WalletNotAssociatedWithUser() {
        User user = new User();
        Wallet wallet = new Wallet();
        wallet.setUser(user);
        Transaction transaction = new Transaction();
        transaction.setSourceID(wallet);

        assertThrows(TransactionException.class, () -> {
            transactionService.createTransaction(transaction);
        });
    }

    @Test
    public void UserCannotPayItself() {
        User user = new User();
        Wallet wallet = new Wallet();
        wallet.setUser(user);
        Transaction transaction = new Transaction();
        transaction.setSourceID(wallet);
        transaction.setDestinationID(wallet);

        assertThrows(TransactionException.class, () -> {
            transactionService.createTransaction(transaction);
        });
    }

    @Test
    public void UserCannotPayNegativeAmount() {
        Wallet sourceWallet = new Wallet();
        Wallet destinationWallet = new Wallet();
        Transaction transaction = new Transaction();
        transaction.setSourceID(sourceWallet);
        transaction.setDestinationID(destinationWallet);
        transaction.setAmount(-10);

        assertThrows(TransactionException.class, () -> {
            transactionService.createTransaction(transaction);
        });
    }

    @Test
    public void PastPayment() {
        Wallet sourceWallet = new Wallet();
        Wallet destinationWallet = new Wallet();
        Transaction transaction = new Transaction();
        transaction.setSourceID(sourceWallet);
        transaction.setDestinationID(destinationWallet);
        transaction.setCreatedAt(new Date(System.currentTimeMillis() - 1000*60));

        assertThrows(TransactionException.class, () -> {
            transactionService.createTransaction(transaction);
        });
    }

    @Test
    public void DoneTransaction() {
        Wallet sourceWallet = new Wallet();
        sourceWallet.setBalance(400.0);
        Wallet destinationWallet = new Wallet();
        destinationWallet.setBalance(10.0);
        Transaction transaction = new Transaction();
        transaction.setSourceID(sourceWallet);
        transaction.setDestinationID(destinationWallet);
        transaction.setAmount(100);

        transaction.setCreatedAt(new Date(System.currentTimeMillis() + 1000 * 60));

        when(transactionRepository.save(transaction)).thenReturn(transaction);

        Transaction result = transactionService.createTransaction(transaction);

        assertNotNull(result);
        assertEquals(transaction, result);
        assertEquals(300.0, sourceWallet.getBalance());
        assertEquals(10.0, destinationWallet.getBalance());
    }

    @Test
    public void CreateTransactionWithZeroBalance() {
        Wallet sourceWallet = new Wallet();
        sourceWallet.setBalance(0.0);
        Wallet destinationWallet = new Wallet();
        destinationWallet.setBalance(10.0);
        Transaction transaction = new Transaction();
        transaction.setSourceID(sourceWallet);
        transaction.setDestinationID(destinationWallet);
        transaction.setAmount(100);

        when(transactionRepository.save(transaction)).thenReturn(transaction);

        assertThrows(TransactionException.class, () -> {
            transactionService.createTransaction(transaction);
        });
    }

    @Test
    public void DeleteTransaction() {

        Transaction transactionToDelete = new Transaction();
        transactionToDelete.setId(1);

        when(transactionRepository.findById(1)).thenReturn(java.util.Optional.of(transactionToDelete));

        transactionService.deleteTransaction(1);

        verify(transactionRepository).deleteById(1);
    }
}
