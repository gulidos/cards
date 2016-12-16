package ru.rik.cardsnew.db;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import ru.rik.cardsnew.domain.Balance;
import ru.rik.cardsnew.domain.Card;

public interface BalanceRepo extends JpaRepository<Balance, Long> {

	List<Balance> findByCard(Card c);
	Page<Balance> findByCard(Pageable p, Card c);
}
