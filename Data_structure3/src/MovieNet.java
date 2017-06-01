
/*
 * Six Degrees of Kevin Bacon
 *
 * Bongki Moon (bkmoon@snu.ac.kr), Oct/23/2014.
*/

import java.lang.*;
import java.util.*;
import java.io.*;

class Actormap {
	String myname;
	HashSet<String> neighbors;
	ArrayList<String> movies;

	public Actormap(String name) {
		myname = name;
		neighbors = new HashSet<String>();
		movies = new ArrayList<String>();
	}
	public void put_neighbor(String t){
		if(t.equals(myname)){
			return;
		}
		else{
			neighbors.add(t);
		}
	}
}

public class MovieNet {

	static final String KevinBacon = "Bacon, Kevin";
	ArrayList<String[]> movieline;
	ArrayList<Actormap> actor_graph;
	HashMap<String, Integer> actor_location = new HashMap<String, Integer>(); // index of actor in actor_graph(arraylist)

	public void actor_graph_setter(String[] cc) { // actors in each movies given by lines
		String movieName = cc[0];
		int key = 0;
		while (key < cc.length) {
			if (actor_location.containsKey(cc[key])) {  //there are already vertex. only add neighbors
				Actormap exist_actor = actor_graph.get(actor_location.get(cc[key]));
				exist_actor.movies.add(movieName);
				for(int i=1 ; i<cc.length; i++){
					exist_actor.put_neighbor(cc[i]);
				}
			} else { // new actor! add vertex and add neighbors.
				actor_location.put(cc[key], actor_graph.size());
				Actormap new_actor = new Actormap(cc[key]);
				new_actor.movies.add(movieName);
				for(int i=1 ; i<cc.length; i++){
					new_actor.put_neighbor(cc[i]);
				}
				actor_graph.add(new_actor);
			}
			key++;
		}
	}
	// Each instance of movielines is String[] such that
	// String[0] = title of movie
	// String[1..n-1] = list of actors
	public MovieNet(LinkedList<String[]> movielines) {
		movieline = new ArrayList<String[]>(movielines);
		actor_graph = new ArrayList<Actormap>();
		for (int i = 0; i < movieline.size(); i++) {
			actor_graph_setter(movieline.get(i));
		}
	} // Constructor

	/*
	 * =========================================================================
	 * ===
	 */

	//O [Q1]
	public String[] moviesby(String[] actors) {
		String[] results;
		String[] movie;
		ArrayList<String> movies = new ArrayList<String>(100);
		ArrayList<String> costar_list = new ArrayList<String>(Arrays.asList(actors));
		
		if(actors.length == 0) return null;
		
		for (int i = 0; i < movieline.size(); i++) {
			movie = movieline.get(i);
			ArrayList<String> thismovie = new ArrayList<String>(Arrays.asList(movie));
			if(!thismovie.contains(actors[0]) && !thismovie.contains(actors.length-1)){
				continue;
			}
			if(thismovie.containsAll(costar_list)){
				movies.add(movie[0]);
			}
		}
		
		if (movies.size() != 0) {
			results = new String[movies.size()];
			for (int i = 0; i < movies.size(); i++) {
				results[i] = movies.get(i);
			}
		} else
			results = null;
		return results;
	}

	// O[Q2]
	public String[] castin(String[] titles) {
		String[] results = null;
		ArrayList<String> casting_list=new ArrayList<String>();
		for(int i=0; i<movieline.size(); i++){
			if(movieline.get(i)[0].equals(titles[0])){
				Collections.addAll(casting_list, movieline.get(i));
				casting_list.remove(0);
				break;
			}
		}
		for (int i = 1; i < titles.length ; i++) {
			ArrayList<String> temp_list = new ArrayList<String>();
			for(int j=0; j<movieline.size(); j++){
				if(movieline.get(j)[0].equals(titles[i])){
					Collections.addAll(temp_list, movieline.get(j));
					temp_list.remove(0);
					break;
				}
			}
			for (int j = 0; j < casting_list.size(); j++) {
				if (!temp_list.contains(casting_list.get(j))) {
					casting_list.remove(j);
					j--;
				}
			}
		}
		if (!casting_list.isEmpty()) {
			results = new String[casting_list.size()];
			for (int i = 0; i < casting_list.size() ; i++) {
				results[i] = casting_list.get(i);
			}
		} else
			results = null;
		return results;
	}

	// O[Q3]
	public String[] pairmost(String[] actors) {
		String[] result = new String[2];
		int[] featured = new int[actors.length-1];
		String[] co_actor = new String[actors.length-1];
		for(int i = 0; i<actors.length-1; i++){
			featured[i] = 0;
		}
		
		ArrayList<ArrayList<String>> movieset = new ArrayList<ArrayList<String>>();
		for(int p = 0; p<actors.length; p++){
			movieset.add(actor_graph.get(actor_location.get(actors[p])).movies);
		}
		
		for(int i=0; i<actors.length-1; i++){
			String co_name = null;
			int max_num = -1;
			for(int j=i+1; j< actors.length; j++){
				int c = 0;
				for(int k = 0 ; k<movieset.get(j).size(); k++){
					if(movieset.get(i).contains(movieset.get(j).get(k))){
						c++;
					}
				}
				if(c>max_num){
					max_num = c;
					co_name = actors[j];
				}
			}
			co_actor[i] = co_name;
			featured[i] = max_num;
		}
		
		int max = -1, index = -1;
		for(int i=0; i<actors.length-1; i++){
			if(featured[i] > 0 && featured[i] > max){
				index = i;
				max = featured[i];
			}
		}
		if(index == -1){
			result = null;
		}
		else {
			result[0] = actors[index];
			result[1] = co_actor[index];
		}		
		return result;
	}
	// O[Q4]
	public int Bacon(String actor) {
		ArrayList<Actormap> BaconQ = new ArrayList<Actormap>(100);
		HashMap<String, Integer> nameD = new HashMap<String, Integer>(100);
		
		if(actor_location.containsKey(KevinBacon)){
			BaconQ.add(actor_graph.get(actor_location.get(KevinBacon)));
			nameD.put(actor_graph.get(actor_location.get(KevinBacon)).myname, 0);
		}
		else return -1;
		
		while (!BaconQ.isEmpty()) {
			ArrayList<String> t = new ArrayList<String>(BaconQ.get(0).neighbors);
			int baconN = nameD.get(BaconQ.get(0).myname);
			baconN++;
			BaconQ.remove(0); //deQueue
			for (int i = 0; i < t.size(); i++) {
				if (!nameD.keySet().contains(t.get(i))) {
					if (t.get(i).equals(actor)) {
						return baconN;
					} 
					else {
						BaconQ.add(actor_graph.get(actor_location.get(t.get(i))));
						nameD.put(actor_graph.get(actor_location.get(t.get(i))).myname, baconN);	
					}
				}
			}
		}
		return -1;
	}

	// O[Q5] 
	public int distance(String src, String dst) {
		ArrayList<Actormap> srcQ = new ArrayList<Actormap>(100);
		HashMap<String, Integer> nameD = new HashMap<String, Integer>(100);
		
		if(!actor_location.containsKey(src) || !actor_location.containsKey(dst)) return -1;	
		srcQ.add(actor_graph.get(actor_location.get(src)));
		nameD.put(actor_graph.get(actor_location.get(src)).myname, 0);
		
		while (!srcQ.isEmpty()) {
			ArrayList<String> t = new ArrayList<String>(srcQ.get(0).neighbors);
			int srcN = nameD.get(srcQ.get(0).myname);
			srcN++;
			srcQ.remove(0); //deQueue
			for (int i = 0; i < t.size(); i++) {
				if (!nameD.keySet().contains(t.get(i))) {
					if (t.get(i).equals(dst)) {
						return srcN;
					} 
					else {
						srcQ.add(actor_graph.get(actor_location.get(t.get(i))));
						nameD.put(actor_graph.get(actor_location.get(t.get(i))).myname, srcN);	
					}
				}
			}
		}
		return -1;
	}
	//O[Q6]
	public int npath(String src, String dst) {
		Actormap temp = null;
		HashSet<String> upper_linked_actors = new HashSet<String>();
		HashSet<String> linked_actors = new HashSet<String>(); //Queue
		HashMap<String, Integer> actors_Count = new HashMap<String, Integer>();
		
		if(!actor_location.containsKey(src) || !actor_location.containsKey(dst)) return 0;
		
		temp = actor_graph.get(actor_location.get(src));
		upper_linked_actors.add(temp.myname);
		Iterator<String> it = temp.neighbors.iterator();
		while(it.hasNext()){
			String named = it.next();
			linked_actors.add(named);
			actors_Count.put(named, 1);
		}
		
		int max_iterating_num = distance(src, dst);
		int Index = 1;
		while(Index<max_iterating_num){
			HashSet<String> temp_actors = new HashSet<String>();
			//Iterator<String> iter = actors_Count.keySet().iterator();
			Iterator<String> iterator = linked_actors.iterator();
			while(iterator.hasNext()){
				Actormap ap = actor_graph.get(actor_location.get(iterator.next()));
				String ances_name = ap.myname;
				int ances_pathnum = actors_Count.get(ances_name);
				Iterator<String> itap = ap.neighbors.iterator();
				while(itap.hasNext()){
					String temp_name = itap.next();
					if(!linked_actors.contains(temp_name) && !upper_linked_actors.contains(temp_name)){
						temp_actors.add(temp_name);
						if(actors_Count.containsKey(temp_name))	actors_Count.put(temp_name, actors_Count.get(temp_name)+ances_pathnum);
						else {
							actors_Count.put(temp_name, ances_pathnum);
						}
					}
				}
			}
			upper_linked_actors = linked_actors;
			linked_actors = temp_actors;
			Index++;
		}		
		if(actors_Count.containsKey(dst)) return actors_Count.get(dst);
		else return 0;
	}
	
	// [Q7]
	public String[] apath(String src, String dst) {
		ArrayList<Actormap> srcQ = new ArrayList<Actormap>(3);
		HashMap<String, ArrayList<String>> nameD = new HashMap<String, ArrayList<String>>();
		ArrayList<String> first_path = new ArrayList<String>();
		
		if(!actor_location.containsKey(src) || !actor_location.containsKey(dst)) return null;		
		
		srcQ.add(actor_graph.get(actor_location.get(src)));
		first_path.add(actor_graph.get(actor_location.get(src)).myname);
		nameD.put(actor_graph.get(actor_location.get(src)).myname, first_path);
		////////////////////	
		//int max_iter_num = distance(src, dst), Index = 0;
		while (!srcQ.isEmpty()) {
			Actormap actor = srcQ.get(0);
			HashSet<String> t = actor.neighbors;
			ArrayList<String> path = nameD.get(actor.myname);
			srcQ.remove(0); //deQueue
			
			Iterator<String> iter = t.iterator();
			while(iter.hasNext()) {
				String p = iter.next();
				if (!nameD.keySet().contains(p)) {
					ArrayList<String> temp_path = new ArrayList<String>(path);
					temp_path.add(p);
					if (p.equals(dst)) {
						return (String[])temp_path.toArray(new String[temp_path.size()]);
					} 
					else {
						srcQ.add(actor_graph.get(actor_location.get(p)));
						nameD.put(actor_graph.get(actor_location.get(p)).myname, temp_path);
							
					}
				}
			}
		}
		return null;
	}

	//O [Q8]
	public int eccentricity(String actor) { 
		ArrayList<Actormap> actorQ = new ArrayList<Actormap>();
		HashMap<String, Integer> actorD = new HashMap<String, Integer>();
		
		actorQ.add(actor_graph.get(actor_location.get(actor)));
		actorD.put(actor_graph.get(actor_location.get(actor)).myname, 0);

		int maximum = 0;
		if(actor_graph.get(actor_location.get(actor)).neighbors.isEmpty())  return 0;
		
		while (!actorQ.isEmpty()) {
			HashSet<String> t = actorQ.get(0).neighbors;
			int baconN = actorD.get(actorQ.get(0).myname);
			if(baconN > maximum) maximum = baconN;
			baconN++;
			actorQ.remove(0); //deQueue
			Iterator<String> iter = t.iterator();
			while(iter.hasNext()) {
				String p = iter.next();
				if (!actorD.keySet().contains(p)) {
					actorQ.add(actor_graph.get(actor_location.get(p)));
					actorD.put(actor_graph.get(actor_location.get(p)).myname, baconN);
				}
			}
		}

		return maximum;
	}

	//O [Q9]
	public float closeness(String actor) {
		ArrayList<Actormap> actorQ = new ArrayList<Actormap>();
		HashMap<String, Integer> actorD = new HashMap<String, Integer>();
		
		actorQ.add(actor_graph.get(actor_location.get(actor)));
		actorD.put(actor_graph.get(actor_location.get(actor)).myname, 0);
		if(actor_graph.get(actor_location.get(actor)).neighbors.isEmpty())  return 0;
		
		while(!actorQ.isEmpty()) {
			HashSet<String> t = actorQ.get(0).neighbors;
			int baconN = actorD.get(actorQ.get(0).myname);
			baconN++;
			actorQ.remove(0); //deQueue
			Iterator<String> iter = t.iterator();
			while(iter.hasNext()) {
				String p = iter.next();
				if (!actorD.keySet().contains(p)) {
					actorQ.add(actor_graph.get(actor_location.get(p)));
					actorD.put(actor_graph.get(actor_location.get(p)).myname, baconN);
				}
			}
		}
		actorD.remove(actor);
		float result = 0;
		Iterator<Integer> close_iter = actorD.values().iterator();
		while(close_iter.hasNext()){
			result += 1/((float)Math.pow(2, close_iter.next()));
		}
		return result;
	}

	/*
	 * =================================================='=======================
	 * ===
	 */

}
