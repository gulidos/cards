package ru.rik.cardsnew.domain;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.cache.annotation.Cacheable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Builder;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(exclude = { "group", "bank","channel"},callSuper = false)
@ToString(exclude = {"group", "bank","channel"})
@Entity
@Cacheable
@org.hibernate.annotations.Cache(
	    usage = CacheConcurrencyStrategy.READ_WRITE
	)
@Table(name="CARD", uniqueConstraints=@UniqueConstraint(columnNames={"bank_id", "place"}))
public class Card {
    @Id
    @Column(name="id")
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private long id;
    
    @Version
    protected long version;
    
    @Column(unique=true, nullable = false)
    @NotNull(message = "{error.card.name.null}")
	@NotEmpty(message = "{error.card.name.empty}")
	@Size(min = 3, max = 20, message = "{error.card.name.size}")
	private String name;
    
    @Column(unique=true)
    @Size(min = 10, max = 10, message = "number has to be equal to 10 digits")
	private String number;
	
	private Place place;
    
	@NotNull(message = "{error.card.im.null}")
	@NotEmpty(message = "{error.card.im.empty}")
	@Size(min = 3, max = 20, message = "sernumber has to be more than 3 and less 10 digits")
    @Column(unique=true, nullable = false)
	private String sernumber;
    
	@Column
	private Oper oper;
    
    @ManyToOne
	private Grp group;
    
    @ManyToOne
	private Bank bank;
    
	@OneToOne(fetch = FetchType.EAGER )
	@JoinColumn(name = "channel", unique=true)
	private Channel channel;
	
	public String toStringAll() {
		return toString() 
				+ " group: " + ( group != null ? group.getId() : "none") 
				+ " bank: "  + (bank != null ? bank.getIp() : "none") 
				+ " ch: "    + (channel != null ? channel.getName() : "none");
	}
}
