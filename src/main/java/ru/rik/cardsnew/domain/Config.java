package ru.rik.cardsnew.domain;

import javax.persistence.Cacheable;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Version;

import org.hibernate.annotations.CacheConcurrencyStrategy;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Builder;

@NoArgsConstructor @AllArgsConstructor @Builder
@Entity
@Cacheable 
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Table(name = "_CONFIG")
public class Config {
	@Id
	@Getter @Setter private String name;
	@Version
	@Getter @Setter private long version;
	@Getter @Setter private String value;
	@Getter @Setter private String descr;
	
	public String getString(String name) {
		return null;
	}
	
	public int getInt(String name) {
		return 0;
	}

}
