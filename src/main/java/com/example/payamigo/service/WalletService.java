package com.example.payamigo.service;

import com.example.payamigo.model.Wallet;
import com.example.payamigo.repository.WalletRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WalletService {

    private final WalletRepository walletRepository;

    @Autowired
    public WalletService(WalletRepository walletRepository) {
        this.walletRepository = walletRepository;
    }

    public Wallet createWallet(Wallet wallet) {
        return walletRepository.save(wallet);
    }

    public List<Wallet> getAllWallets() {
        return walletRepository.findAll();
    }

    public Wallet getWalletById(Integer walletId) {
        return walletRepository.findById(walletId).get();
    }

    public Wallet updateWallet(Wallet wallet) {
        return walletRepository.save(wallet);
    }

    public void deleteWallet(Integer walletId) {
        walletRepository.deleteById(walletId);
    }

}






