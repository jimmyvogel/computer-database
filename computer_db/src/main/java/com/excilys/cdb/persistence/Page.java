package com.excilys.cdb.persistence;

import java.util.ArrayList;
import java.util.List;

public class Page<T> {

	private List<T> objects;
	private int pageCourante;
	private int taille;
	private int limit;
	
	public Page(int limit) {
		objects = new ArrayList<T>();
		this.limit = limit;
	}
	
	public int offset(int numeroPage) {
		this.pageCourante = numeroPage;
		return (numeroPage-1)*limit;
	}
	
	public void charge(List<T> objects) {
		if(objects.size()<=limit) {
			taille = objects.size();
			this.objects = objects;
		}else {
			this.objects = objects.subList(0, limit);
		}
	}
	
	public int getLimit() {
		return limit;
	}
	public void setLimit(int limit) {
		this.limit = limit;
	}

	public int getPageCourante() {
		return pageCourante;
	}

	public int getTaille() {
		return taille;
	}

	public List<T> getObjects() {
		return objects;
	}
	
	public String toString() {
		String res = "";
		for(T t : objects) {
			res += t.toString()+"\n";
		}
		return res;
	}
	
	
	
}
