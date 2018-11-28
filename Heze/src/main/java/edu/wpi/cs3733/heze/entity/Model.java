package edu.wpi.cs3733.heze.entity;

import java.util.Iterator;
import java.util.List;

public class Model {
	List<Schedule> schedules;
	public Model(List<Schedule> schedules) {
		super();
		this.schedules = schedules;
	}
	
	public Iterator<Schedule> schedulesIt() {
		return this.schedules.iterator();
	}
}
