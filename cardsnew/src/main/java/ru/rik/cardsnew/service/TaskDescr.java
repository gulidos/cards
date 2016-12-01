package ru.rik.cardsnew.service;

import java.util.Date;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.rik.cardsnew.domain.State;


@NoArgsConstructor

public class TaskDescr {
	@Getter @Setter Class<?> clazz;
	@Getter @Setter State state;
	@Getter @Setter Date lastChangeDate;
	@Getter String stage;
	
	public String getName() {return state.getName();}
	public long getId() {return state.getId();}
	
	public TaskDescr(Class<?> clazz, State state, Date startDate) {
		super();
		this.clazz = clazz;
		this.state = state;
		this.lastChangeDate = startDate;
	}
	
	public void setStage(String str) {
		this.stage = str;
		this.lastChangeDate = new Date();
	}
	@Override
	public String toString() {
		return "TaskDescr [clazz=" + clazz + ", state=" + state.getName() + ", lastChangeDate=" + lastChangeDate + ", stage="
				+ stage + "]";
	}
	
}
