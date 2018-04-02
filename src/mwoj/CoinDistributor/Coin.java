package mwoj.CoinDistributor;

public class Coin {

    private int id;
    private String hash;
    private String previousCoinHash;
    byte[] signature;


    public Coin()
    {

    }

    public int getId() {
        return id;
    }

    public String getHash() {
        return hash;
    }

    public String getPreviousCoinHash() {
        return previousCoinHash;
    }

    public byte[] getSignature() {
        return signature;
    }


    public void setHash(String hash) {
        this.hash = hash;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setPreviousCoinHash(String previousCoinHash) {
        this.previousCoinHash = previousCoinHash;
    }

    public void setSignature(byte[] signature) {
        this.signature = signature;
    }

}
