package com.example.payamigo;

import com.example.payamigo.model.Wallet;
import com.example.payamigo.repository.TransactionsRepository;
import com.example.payamigo.repository.WalletRepository;
import com.example.payamigo.service.TransactionsService;
import com.example.payamigo.service.WalletService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class WalletTests {
    @Mock
    private WalletRepository walletRepository;

    @InjectMocks
    private WalletService walletService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void createWallet() {
        Wallet walletToCreate = new Wallet();
        when(walletRepository.save(walletToCreate)).thenReturn(walletToCreate);

        Wallet createdWallet = walletService.createWallet(walletToCreate);

        assertNotNull(createdWallet);
        assertEquals(walletToCreate, createdWallet);
    }

    @Test
    public void getAllWallets() {
        Wallet wallet1 = new Wallet();
        Wallet wallet2 = new Wallet();
        when(walletRepository.findAll()).thenReturn(List.of(wallet1, wallet2));

        List<Wallet> wallets = walletService.getAllWallets();

        assertNotNull(wallets);
        assertEquals(2, wallets.size());
    }

    @Test
    public void getWalletById() {
        Wallet walletToFind = new Wallet();
        walletToFind.setId(1);
        when(walletRepository.findById(1)).thenReturn(java.util.Optional.of(walletToFind));

        Wallet foundWallet = walletService.getWalletById(1);

        assertNotNull(foundWallet);
        assertEquals(walletToFind, foundWallet);
    }

    @Test
    public void updateWallet() {
        Wallet walletToUpdate = new Wallet();
        when(walletRepository.save(walletToUpdate)).thenReturn(walletToUpdate);

        Wallet updatedWallet = walletService.updateWallet(walletToUpdate);

        assertNotNull(updatedWallet);
        assertEquals(walletToUpdate, updatedWallet);
    }

    @Test
    public void testDeleteWallet() {
        walletService.deleteWallet(1);

        verify(walletRepository).deleteById(1);
    }
}
