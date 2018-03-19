package mwoj;

import mwoj.Blockchain.Block;
import mwoj.Blockchain.Blockchain;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;

public class Main {

    public static void main(String[] args) throws NoSuchAlgorithmException, UnsupportedEncodingException {
	    System.out.println("Start");
		Blockchain blockchain = new Blockchain();

	    Block newBlock = blockchain.generateNewBlock("data2");
    }
}
