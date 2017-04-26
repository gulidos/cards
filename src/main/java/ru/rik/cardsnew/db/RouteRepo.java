package ru.rik.cardsnew.db;

import ru.rik.cardsnew.domain.Route;

public interface RouteRepo {
	public int load( );
	 public Route find(Long num);
}