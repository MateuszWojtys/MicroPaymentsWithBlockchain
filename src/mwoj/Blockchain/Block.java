package mwoj.Blockchain;

public class Block{

    private int index;
    private String hash;
    private String previousBlockHash;
    private String data;
    private String timestamp;
    private int proofOfWork;

    public Block(int _index, String _hash, String _previousBlockHash, String _data, String _timestamp, int _proofOfWork){
        this.index = _index;
        this.hash = _hash;
        this.previousBlockHash = _previousBlockHash;
        this.data = _data;
        this.timestamp = _timestamp;
        this.proofOfWork = _proofOfWork;
    }


    public int getProofOfWork() {
        return proofOfWork;
    }

    public String getHash() {
        return hash;
    }

    public int getIndex() {
        return index;
    }

    public String getData() {
        return data;
    }

    public String getPreviousBlockHash() {
        return previousBlockHash;
    }

    public String getTimestamp() {
        return timestamp;
    }
}