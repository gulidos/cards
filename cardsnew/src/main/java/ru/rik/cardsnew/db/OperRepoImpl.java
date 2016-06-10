package ru.rik.cardsnew.db;

import org.springframework.stereotype.Repository;

import ru.rik.cardsnew.domain.Oper;

@Repository
public class OperRepoImpl extends GenericRepoImpl<Oper, Long>  {
	private static final long serialVersionUID = 1L;


	public OperRepoImpl() {
		super(Oper.class);	}
}
