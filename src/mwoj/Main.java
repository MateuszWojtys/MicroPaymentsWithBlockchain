package mwoj;

import mwoj.Blockchain.Block;
import mwoj.Blockchain.Blockchain;
import mwoj.Blockchain.Hasher;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;

public class Main {

    public static void main(String[] args) throws NoSuchAlgorithmException, UnsupportedEncodingException {
	    System.out.println("Start");
		Blockchain blockchain = new Blockchain();

	    String hash = Hasher.getHash("startBlock");
	    Block startBlock = new Block(0, hash, "", "Data", new Timestamp(System.currentTimeMillis()).toString() );

		blockchain.generateNewBlock(startBlock, "data2");
    }
}
