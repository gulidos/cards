package ru.rik.cardsnew.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Version;
import javax.validation.constraints.Size;

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
@EqualsAndHashCode (exclude = {"box", "trunk", "group", "card"}, callSuper = false)
@ToString (exclude = {"box", "trunk", "group"})
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
	@Column(unique=true, nullable=false)
	@Size(min = 3, max = 20, message = "Bad value")
	private String name;
	
	@Setter @Getter
	private Line line;
	
	@Setter @Getter
	@ManyToOne(optional=false)
	private Box box;
	
	@Setter	@Getter
	@ManyToOne
	private Grp group;

	@Getter	@Setter
	@ManyToOne
	private Trunk trunk;
	
	@Getter	@Setter
	@OneToOne(fetch = FetchType.EAGER)
	private Card card;
}
