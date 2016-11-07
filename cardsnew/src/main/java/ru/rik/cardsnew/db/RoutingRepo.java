package ru.rik.cardsnew.db;

import javax.sql.DataSource;

import ru.rik.cardsnew.domain.Route;

public interface RoutingRepo {
	public int load( DataSource ds);
	 public Route find(Long num);
}