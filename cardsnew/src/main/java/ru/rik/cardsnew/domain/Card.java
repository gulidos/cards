package ru.rik.cardsnew.domain;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.persistence.Version;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.cache.annotation.Cacheable;

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
@EqualsAndHashCode(exclude = { "group", "bank"},callSuper = false)
@ToString(exclude = {"group", "bank"})
@Entity
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Table(name="_CARD", uniqueConstraints=@UniqueConstraint(columnNames={"bank_id", "place"}))
public class Card implements MyEntity {
    @Id @Getter @Setter
    @Column(name="id")
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private long id;
    
    @Version @Getter @Setter
    protected long version;
    
    @Column(unique=true, nullable = false)
    @NotNull(message = "{error.card.name.null}")
	@NotEmpty(message = "{error.card.name.empty}")
	@Size(min = 3, max = 20, message = "{error.card.name.size}")
//    @NaturalId(mutable = true)
    @Getter @Setter
	private String name;
    
    @Column(unique=true)
    @Size(min = 10, max = 10, message = "number has to be equal to 10 digits")
    @Getter @Setter
	private String number;
	
    @Getter @Setter
	private Place place;
    
	@NotNull(message = "{error.card.im.null}")
	@NotEmpty(message = "{error.card.im.empty}")
	@Size(min = 3, max = 20, message = "sernumber has to be more than 3 and less 10 digits")
    @Column(unique=true, nullable = false)
	@Getter @Setter
	private String sernumber;
    
    @ManyToOne 
    @Getter @Setter
	private Grp group;
    
    @ManyToOne
    @Getter @Setter
	private Bank bank;
    
//    try this: http://stackoverflow.com/questions/6068374/hibernate-cache-for-mappedby-object
    @Getter @Setter
//	@OneToOne(mappedBy="card", fetch = FetchType.LAZY)
	private long channelId;
	
	public String toStringAll() {
		return toString() 
				+ " group: " + ( group != null ? group.getId() : "none") 
				+ " bank: "  + (bank != null ? bank.getIp() : "none"); 
//				+ " ch: "    + (channel != null ? channel.getName() : "none");
	}
	

}
