package com.lexxiconstudios.vestibule.core.exceptions;

import java.util.ArrayList;
import java.util.List;

public class MultiException extends RuntimeException {
	private static final long serialVersionUID = -6217308089283956786L;
	private final List<Throwable> exceptions = new ArrayList<Throwable>();
	
	public MultiException(List<Throwable> exceptions) {
		super();
		this.exceptions.addAll(exceptions);
	}

	public List<Throwable> getExceptions() {
		return exceptions;
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (Throwable t : exceptions) {
			if (sb.length() > 0) sb.append("\n");
			sb.append(t.getMessage());
		}
		return sb.toString();
	}
}
