package com.crawljax.core.state.duplicatedetection;

import net.jpountz.xxhash.XXHash32;
import net.jpountz.xxhash.XXHashFactory;

class XxHashGenerator implements HashGenerator {
	
	private XXHash32 xxhash;

	public XxHashGenerator() {
		xxhash = XXHashFactory.fastestInstance().hash32();
	}

	@Override
	public int generateHash(String input) {
		return xxhash.hash(input.getBytes(), 0, input.length(), 0x9747b28c);
	}

}
