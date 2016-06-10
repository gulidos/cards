package ru.rik.cardsnew.domain;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Version;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Builder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity


public class Oper {
	@Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private long id;
	
	 @Version
	 @Getter @Setter
	 protected long version;
	
//	@Column(nullable = false, unique=true)
	String name;
	
    @OneToMany(mappedBy = "oper")
    Set<Card> cards;
	
}
