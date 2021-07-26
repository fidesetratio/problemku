package com.app.utils;

public class PolicyAlterationCounter {
	private Integer counter;
	public PolicyAlterationCounter() {
		counter = 0;
	}
	
	public void add(Integer increment) {
		this.counter = this.counter + increment;
	}
	
	public void addOne() {
		this.add(1);
	}
	
	public Integer getCounter() {
		return counter;
	}

}
