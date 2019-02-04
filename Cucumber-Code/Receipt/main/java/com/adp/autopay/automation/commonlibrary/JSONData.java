/**
 * 
 */
package com.adp.autopay.automation.commonlibrary;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * @author gadiv
 *
 */
public class JSONData {
	public com.google.gson.JsonArray events;
	public com.google.gson.JsonObject eventsObject;
	public String inputJSONFile;
	public int index;
	/**
	 * @return the events
	 */
	public com.google.gson.JsonArray getEvents() {
		return events;
	}
	/**
	 * @param events the events to set
	 */
	public void setEvents(com.google.gson.JsonArray events) {
		this.events = events;
	}
	/**
	 * @return the eventsObject
	 */
	public com.google.gson.JsonObject getEventsObject() {
		return eventsObject;
	}
	/**
	 * @param eventsObject the eventsObject to set
	 */
	public void setEventsObject(com.google.gson.JsonObject eventsObject) {
		this.eventsObject = eventsObject;
	}
	/**
	 * @return the inputJSONFile
	 */
	public String getInputJSONFile() {
		return inputJSONFile;
	}
	/**
	 * @param inputJSONFile the inputJSONFile to set
	 */
	public void setInputJSONFile(String inputJSONFile) {
		this.inputJSONFile = inputJSONFile;
	}
	/**
	 * @return the index
	 */
	public int getIndex() {
		return index;
	}
	/**
	 * @param index the index to set
	 */
	public void setIndex(int index) {
		this.index = index;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + index;
		return result;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		JSONData other = (JSONData) obj;
		if (index != other.index)
			return false;
		return true;
	}
	
	
}
