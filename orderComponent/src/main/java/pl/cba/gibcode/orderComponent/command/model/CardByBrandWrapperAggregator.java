package pl.cba.gibcode.orderComponent.command.model;

import java.util.HashMap;
import java.util.Map;

public class CardByBrandWrapperAggregator {
	private Map<String, CardByBrandAggregationEntry> entries;

	public CardByBrandWrapperAggregator() {
		entries = new HashMap<>();
	}

	public Map<String, CardByBrandAggregationEntry> getEntries() {
		return entries;
	}

	public void setEntries(Map<String, CardByBrandAggregationEntry> entries) {
		this.entries = entries;
	}

	@Override public String toString() {
		return "CardByBrandWrapperAggregator{" +
				"entries=" + entries +
				'}';
	}
}
