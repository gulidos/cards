package ru.rik.cardsnew.domain;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Version;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Builder;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name="BOX")
public class Box {
  @Id   @Column(name="id")   @GeneratedValue(strategy=GenerationType.IDENTITY)
    private long id;
  	
  	@Version
	@Getter @Setter
	protected long version;

  	@Getter @Setter
  	 @Column (unique=true)
	private String disposition;
  	
  	@Getter @Setter
  	@Column (unique=true)
	private String ip;
	
  	@Getter @Setter
	private int capacity;
  	
  	@Getter @Setter
	private String descr;
  	
  	@Getter @Setter
    @OneToMany( mappedBy = "box")
    Set<Channel> channels = new HashSet<>();


}
