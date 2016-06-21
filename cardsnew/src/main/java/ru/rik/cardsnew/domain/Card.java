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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.NotEmpty;

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
@Table(name="CARD")
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
    
	private Place place;
    
	@NotNull(message = "{error.card.im.null}")
	@NotEmpty(message = "{error.card.im.empty}")
	@Size(min = 3, max = 20, message = "{error.card.im.size}")
    @Column(unique=true)
	private String sernumber;
    
	@Column
	private Oper oper;
    
    @ManyToOne
	private Grp group;
    
    @ManyToOne
	private Bank bank;
    
	private String number;
	
	@OneToOne(fetch = FetchType.EAGER)
	private Channel channel;
	
	public String toStringAll() {
		return toString() 
				+ " group: " + ( group != null ? group.getId() : "none") 
				+ " bank: "  + (bank != null ? bank.getIp() : "none") 
				+ " ch: "    + (channel != null ? channel.getName() : "none");
	}
}
