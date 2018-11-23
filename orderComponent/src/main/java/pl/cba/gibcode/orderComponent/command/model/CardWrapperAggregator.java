package pl.cba.gibcode.orderComponent.command.model;

import java.util.HashMap;
import java.util.Map;

public class CardWrapperAggregator {

	private Map<String, CardAggregationEntry> entries;

	public CardWrapperAggregator() {
		this.entries = new HashMap<>();
	}

	public Map<String, CardAggregationEntry> getEntries() {
		return entries;
	}

	public void setEntries(Map<String, CardAggregationEntry> entries) {
		this.entries = entries;
	}

	@Override public String toString() {
		return "CardWrapperAggregator{" +
				"entries=" + entries +
				'}';
	}
}
