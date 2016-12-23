package ru.rik.cardsnew.db;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import ru.rik.cardsnew.domain.Card;
import ru.rik.cardsnew.domain.Event;
@NoRepositoryBean
public interface EventBaseRepo<T extends Event> extends JpaRepository<T , Long> {

	List<T> findByCard(Card c);
	Page<T> findByCard(Pageable p, Card c);
}
