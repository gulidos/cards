package ru.rik.cardsnew.domain;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.persistence.Version;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.cache.annotation.Cacheable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.Builder;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString(exclude={"channels"})
@EqualsAndHashCode(exclude={"channels"})
@Entity
@Table(name="BOX")
@Cacheable
@org.hibernate.annotations.Cache(  usage = CacheConcurrencyStrategy.READ_WRITE	)
public class Box {
	
	@Transient public static final String DEF_USER = "voip";
	@Transient public static final String DEF_PASSWORD = "1234";
	
  @Id   @Column(name="id")   @GeneratedValue(strategy=GenerationType.IDENTITY)
    private long id;
  	
  	@Version
	@Getter @Setter
	protected long version;

  	@Getter @Setter
  	 @Column (unique=true)
	private String disposition;
  	
  	@Getter @Setter
  	@Column (unique=true, nullable = false)
	private String ip;
	
  	@Getter @Setter
	private int capacity;
  	
  	@Getter @Setter
	private String descr;
  	
  	@Getter @Setter
    @OneToMany( mappedBy = "box", fetch=FetchType.LAZY)
  	@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    Set<Channel> channels = new HashSet<>();

	public String getName() {
		return getIp();
	}
}
