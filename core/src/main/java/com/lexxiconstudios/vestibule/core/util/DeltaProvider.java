package com.lexxiconstudios.vestibule.core.util;

public class DeltaProvider {
	@FunctionalInterface
	public static interface DeltaLambda {
		public float get();
	}

	private final DeltaLambda provider;

	public DeltaProvider(DeltaLambda provider) {
		this.provider = provider;
	}

	public float getDelta() {
		return provider.get();

	}
}
