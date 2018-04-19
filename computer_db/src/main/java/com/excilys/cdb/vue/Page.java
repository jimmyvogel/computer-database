package main.java.com.excilys.cdb.vue;

import java.util.List;

/**
 * Class permettant d'instancier une sous-liste de donnée avec un numéro de page.
 * @author vogel
 *
 * @param <K> Le type d'objet a instancié.
 */
public class Page<K>{

	private List<K> objects;
	
	private int numeroPage;
	
	public Page(List<K> objects){
		this.objects = objects;
	}
	
	public List<K> getDatas(){
		return objects;
	}
	
}
