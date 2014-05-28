package com.crawljax.core.state.duplicatedetection;

public class XxHashGeneratorFactory implements HashGeneratorFactory {

	@Override
	public HashGenerator getInstance() {
		return new XxHashGenerator();
	}

}
