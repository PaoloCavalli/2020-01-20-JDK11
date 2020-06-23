package it.polito.tdp.artsmia.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.artsmia.db.ArtsmiaDAO;

public class Model {

	private ArtsmiaDAO dao;
	private Map <Integer, Artist> idMap ;
	 private Graph<Artist, DefaultWeightedEdge> grafo;
	 private List<Adiacenza> adiacenze;
	 
	 //Lista ottima
	 private List<Artist> ottima ;
	 
	public Model() {
		dao = new ArtsmiaDAO();
		idMap = new HashMap<Integer, Artist>();
	}
	
	public List<String> getRuoli(){
		return dao.getRoles();
				
	}
	public void creaGrafo(String role) {
		grafo = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		
		Graphs.addAllVertices(this.grafo, this.dao.getNodi(role, idMap));
		
		 adiacenze = this.dao.getArchi(role, idMap);
		
		for(Adiacenza a: adiacenze) {
			if(this.grafo.containsVertex(a.getA1()) &&  this.grafo.containsVertex(a.getA2())) {
				Graphs.addEdge(this.grafo, a.getA1(), a.getA2(),(int) a.getPeso());
			}
		}
		
		idMap.clear();
	}
	
	public int nVertici() {
		return this.grafo.vertexSet().size();
		}

		public int nArchi() {
		return this.grafo.edgeSet().size();
		}
		public List<Adiacenza> getConnessi(){
		  Collections.sort(adiacenze);	
	      return adiacenze;
		}
		public Artist getArtista(Integer id) {
			Artist sorgente = null;
			for(Artist a: this.grafo.vertexSet()) {
				if(a.getId().equals(id)) {
					sorgente = a;
				}
			}
			return sorgente;
		}
//PUNTO 2
		public List<Artist> trovaPercorso(Integer id){
			Artist sorgente = null;
			for(Artist a: this.grafo.vertexSet()) {
				if(a.getId().equals(id)) {
					sorgente = a;
				}
			}
			List<Artist> parziale = new ArrayList<>();
			this.ottima = new ArrayList<Artist>();
			parziale.add(sorgente);
			ricorsiva (parziale, -1);
			
			return ottima;
			
		}

private void ricorsiva(List<Artist> parziale, int peso) {
	if(parziale.size()>ottima.size()) {
		this.ottima= new ArrayList<>(parziale);
	}
	Artist ultimo = parziale.get(parziale.size()-1);
	List<Artist> vicini = Graphs.neighborListOf(this.grafo, ultimo);
	for(Artist vicino : vicini) {
		if(!parziale.contains(vicino) && peso == -1) {
			parziale.add(vicino);
			ricorsiva(parziale,(int) this.grafo.getEdgeWeight(this.grafo.getEdge(ultimo, vicino)));
			parziale.remove(vicino);
			
		}else {
			if(!parziale.contains(vicino) && this.grafo.getEdgeWeight(this.grafo.getEdge(ultimo, vicino)) == peso) {
				parziale.add(vicino);
				ricorsiva(parziale, peso);
				parziale.remove(vicino);
			}
		}
	}
	
}
}
