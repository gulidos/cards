package ru.rik.cardsnew.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Version;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Builder;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode (exclude = {"box", "trunk", "group", "card"})
@ToString (exclude = {"box", "trunk", "group", "card"})
@Entity
@Table(name="CHANNEL")
public class Channel {
    @Id   @Column(name="id")   @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Getter @Setter
    private long id;
	
    @Version
 	@Getter @Setter
	protected long version;

	@Setter @Getter
	@Column(unique=true)
	private String name;
	
	@Setter @Getter
	private Line line;
	
	@Setter @Getter
	@ManyToOne
	private Box box;
	
	@Setter	@Getter
	@ManyToOne
	private Grp group;

	@Getter	@Setter
	@ManyToOne
	private Trunk trunk;
	
	@Getter	@Setter
	@OneToOne
	private Card card;
}
