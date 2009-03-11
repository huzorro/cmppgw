package com.enorbus.sms.gw.cmpp.support;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

/**
 * Sequence IDÉú³ÉÆ÷
 *
 * @author Long Zhi
 * @version $Id: SeqGenerator.java 1984 2009-01-22 06:01:35Z zhi.long $
 */
public class SeqGenerator {
	private static final Logger logger = LoggerFactory.getLogger(SeqGenerator.class);

	private volatile static SeqGenerator instance;
    private DataOutputStream dos;
	private File sequenceFile;
	
	private static final int CACHE_SIZE = 20;
	private static int currentValue = 0;
	private static int nextCachedValue = CACHE_SIZE+1;

	private SeqGenerator() {
		sequenceFile = new File("seq.dat");
		try {
			if (!sequenceFile.exists()) {
				sequenceFile.createNewFile();
				dos = new DataOutputStream(new FileOutputStream(sequenceFile));
				dos.writeInt(nextCachedValue);
				dos.flush();
				dos.close();
				dos = null;
			} else {
				DataInputStream dis = new DataInputStream(new FileInputStream(sequenceFile));
				nextCachedValue = dis.readInt();
				currentValue = nextCachedValue-1;
				dis.close();
			}
		} catch (Exception e) {
			logger.error("Error thrown while creating sequence data file: ", e);
		}
	}

	public static SeqGenerator getInstance() {
		if (instance == null) {
            synchronized (SeqGenerator.class) {
                if (instance == null) instance = new SeqGenerator();     
            }
        }
		return instance;
	}

	public synchronized int getSeq() {
		currentValue += 1;
		if ( currentValue == nextCachedValue ) {
			if ( Integer.MAX_VALUE - nextCachedValue < CACHE_SIZE ) {
				currentValue = 1;
				nextCachedValue = CACHE_SIZE+1;
			} else {
				nextCachedValue += CACHE_SIZE;
			}
		}
        try {
            dos = new DataOutputStream(new FileOutputStream(sequenceFile));
			dos.writeInt(nextCachedValue);
			dos.flush();
			dos.close();

			dos = null;
		} catch (IOException e) {
            logger.error("Exception thrown while retrieving sequence: ", e);
        }

		return currentValue;
	}
}
