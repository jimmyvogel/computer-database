package main.java.com.excilys.cdb.vue;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class permettant de gérer plusieurs pages pour faire de la pagination sur 
 * des données.
 * @author vogel
 *
 * @param <K> Le type de donnée.
 */
public class Pageur<K>{

	//Les différentes pages du pageur.
	private Map<Integer, Page<K>> pages;
	
	//La limite a affiché par page.
	public int limit;
	
	/**
	 * Constructor d'un pageur
	 * @param limit le nombre d'objets a affiché par pages.
	 */
	public Pageur(int limit) {
		this.limit = limit;
		pages = new HashMap<Integer, Page<K>>();
	}
	
	/**
	 * Chargement des données et séparations sur plusieurs pages.
	 * @param objects les objets a séparés.
	 * @param pageDemarrage la numéro de la page de démarrage.
	 */
	public void postDatas(List<K> objects, int pageDemarrage) {
		int nombres = objects.size()/limit;
		List<K> sousList;
		for(int i=0; i<nombres; i++) {
			if(objects.size()<(i*limit)) {
				sousList = objects.subList((0+i)*limit, objects.size()-1);
			}
			sousList = objects.subList(i*limit, (i+1)*limit);
			
			pages.put(pageDemarrage+i, new Page<K>(sousList));
		}
	}
	
	/**
	 * Accéder à la liste des données sur une page.
	 * @param page le numéro de la page.
	 * @return une liste de type List contenant les données.
	 */
	public List<K> getDatas(int page){
		if(!pages.containsKey(page))
			return null;
		
		return pages.get(page).getDatas();
	}
	
	/**
	 * Afficher les données d'une page.
	 * @param page le numero de la page a affiché.
	 * @return un String représentant le résultat.
	 */
	public String display(int page) {
		if(!pages.containsKey(page))
			return null;
		
		List<K> objects = pages.get(page).getDatas();
		
		String res="";
		for(K o: objects)
			res+=o.toString()+"\n";
		
		return res;
	}
	
	/**
	 * Le nombre de pages existantes dans le pageur.
	 * @return
	 */
	public int getSize() {
		return pages.size();
	}
	
}
