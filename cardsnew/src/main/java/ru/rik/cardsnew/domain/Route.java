package ru.rik.cardsnew.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.Builder;
@EqualsAndHashCode @ToString
public class Route {
	@Getter private final long fromd;
	@Getter private final long tod;
	@Getter private final Oper oper;
	@Getter private final int regcode;
	public final static Route NULL_ROUTE = new Route(0, 0, Oper.UNKNOWN,0);

	@Builder
	public Route(long fromd, long tod, Oper oper, int regcode) {
		super();
		this.fromd = fromd;
		this.tod = tod;
		this.oper = oper;
		this.regcode = regcode;
	}
	
	public boolean isIn(Long number) {
		return ((fromd <= number) && (number <= tod));
	}
	

}
