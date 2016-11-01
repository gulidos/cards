package ru.rik.cardsnew.domain;

import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Version;

import org.hibernate.annotations.CacheConcurrencyStrategy;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Builder;

@NamedQuery(name = "findAllLimits", query = "SELECT l FROM Limit l")
@NoArgsConstructor @AllArgsConstructor @Builder
@Entity
@Cacheable 
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Table(name = "_LIMIT")
public class Limit implements MyEntity {
	@Getter	@Setter
	@Id	@Column(name = "id") @GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@Version
	@Getter	@Setter
	protected long version;

	@Getter	@Setter
//	@Column(unique = true)
	private String name;
	
	@Getter	@Setter
	private int f;
	
	@Getter	@Setter
	private int t;

	@Getter	@Setter
	private String descr;
}
