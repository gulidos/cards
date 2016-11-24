package ru.rik.cardsnew.service;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.rik.cardsnew.domain.State;

@AllArgsConstructor
@NoArgsConstructor
public class TaskDescriptor {
	@Getter @Setter Class<?> clazz;
	@Getter @Setter State state;
	@Getter @Setter  Date startDate;
	
	public String getName() {return state.getName();}
	public long getId() {return state.getId();}
}
