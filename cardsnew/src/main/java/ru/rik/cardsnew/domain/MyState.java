package ru.rik.cardsnew.domain;

public abstract class MyState implements State {
	private long id;
	private String name;
	
	public MyState() {	}

	@Override public long getId() {return id;}
	@Override public void setId(long id) {this.id = id;	}
	@Override public String getName() {return name;}
	@Override public void setName(String name) {this.name = name;}

	@Override
	public abstract Class<?> getClazz();

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (id ^ (id >>> 32));
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		MyState other = (MyState) obj;
		if (id != other.id)
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}
	
	

}
