package mwoj.CoinDistributor;

import mwoj.Blockchain.Hasher;

import java.security.*;
import java.util.ArrayList;

public class CoinDistributor {

    private PrivateKey privateKey;
    private PublicKey publicKey;
    private Coin genesisCoin;
    private Coin lastGeneratedCoin;

    public CoinDistributor() throws NoSuchAlgorithmException {
        generateKeyPair();
        createGenesisCoin();
    }

    private void createGenesisCoin() throws NoSuchAlgorithmException {
        genesisCoin = new Coin();
        genesisCoin.setId(0);
        genesisCoin.setPreviousCoinHash(Hasher.getHashFromString("GenesisCoin"));
        genesisCoin.setSignature(Hasher.createSignatureWithPrivate(privateKey,genesisCoin.getId()+genesisCoin.getPreviousCoinHash()));
        genesisCoin.setHash(Hasher.getHashFromString(genesisCoin.getId()+genesisCoin.getPreviousCoinHash()+genesisCoin.getSignature().toString()));
        lastGeneratedCoin = genesisCoin;
    }

    public ArrayList<Coin> createCoins(int amount, PublicKey owner) throws NoSuchAlgorithmException {
        ArrayList<Coin> coins = new ArrayList<>();
        for(int i=0; i<amount; i++)
        {
            coins.add(createNextCoin());
        }
        //TODO zrobic podpis kluczem publicznym ownera
        return coins;
    }

    private Coin createNextCoin() throws NoSuchAlgorithmException {
        Coin  nextCoin = new Coin();
        nextCoin.setId(lastGeneratedCoin.getId()+1);
        nextCoin.setPreviousCoinHash(lastGeneratedCoin.getHash());
        nextCoin.setSignature(Hasher.createSignatureWithPrivate(privateKey,nextCoin.getId()+nextCoin.getPreviousCoinHash()));
        nextCoin.setHash(Hasher.getHashFromString(nextCoin.getId()+nextCoin.getPreviousCoinHash()+nextCoin.getSignature().toString()));
        lastGeneratedCoin = nextCoin;
        return nextCoin;
    }

    private void generateKeyPair() {
        try {
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
            keyGen.initialize(1024);
            KeyPair keyPair = keyGen.generateKeyPair();

            privateKey = keyPair.getPrivate();
            publicKey = keyPair.getPublic();
        }catch(Exception e) {
            throw new RuntimeException(e);
        }
    }
}
