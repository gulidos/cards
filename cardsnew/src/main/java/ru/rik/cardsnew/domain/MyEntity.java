package ru.rik.cardsnew.domain;
public abstract class MyEntity {
	public abstract long getId() ;
	public abstract String getName() ;
	public abstract void update(MyEntity e) ;
}
