package ru.rik.cardsnew.web;

import org.springframework.stereotype.Component;

import lombok.Data;

@Component
@Data
public class CardFilter {
	private long groupId;
	public CardFilter() {	}

}
