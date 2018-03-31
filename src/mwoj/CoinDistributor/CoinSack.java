package mwoj.CoinDistributor;

import java.security.PublicKey;
import java.util.ArrayList;

public class CoinSack {

    private ArrayList<Coin> coins;
    private byte[] encryption;

    public CoinSack(ArrayList<Coin> _coins, PublicKey owner)
    {
        coins = _coins;

        //signature = Hasher.createSignatureWithPrivate(owner)
    }
}
