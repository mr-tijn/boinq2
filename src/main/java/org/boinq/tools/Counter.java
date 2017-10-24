package org.boinq.tools;

public class Counter {
	public Counter() {
		this(0);
	}
	public Counter(Integer init) {
		this.current = init;
	}
	private Integer current;
	public Integer next() {
		return current++;
	}
}
