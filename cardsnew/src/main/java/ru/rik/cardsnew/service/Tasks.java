package ru.rik.cardsnew.service;

import java.util.concurrent.CompletionService;

import org.springframework.beans.factory.annotation.Autowired;

import ru.rik.cardsnew.service.http.HttpHelper;

public class Tasks {
	@Autowired CompletionService<Futurable> completionService;
	@Autowired HttpHelper httpHelper;


	public Tasks() {	}
	
	public void addTask() {
		
	}

}
