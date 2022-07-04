package it.polito.tdp.imdb.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.imdb.db.ImdbDAO;

public class Model {
	
	private ImdbDAO dao;
	private Graph <Director, DefaultWeightedEdge> grafo;
	private Map<Integer, Director> idMap;
	private List<Director> vertici;
	private List<Director> best;
	private double bestPeso;
	
	public Model() {
		dao= new ImdbDAO();
		this.idMap= new HashMap<Integer, Director>();
	}
	
	public List<Director> popolacmb() {
		
		return this.vertici;
		
	}
	
	public void creaGrafo(Integer anno) {
		
		this.grafo=new SimpleWeightedGraph<Director, DefaultWeightedEdge> (DefaultWeightedEdge.class);
		this.vertici=new ArrayList<Director> (dao.getAllVertici(anno, idMap));
		Graphs.addAllVertices(this.grafo, this.vertici);
		
		List<Arco> archi= new ArrayList<Arco>(this.dao.getAdiacenze(idMap, anno));
		for(Arco a: archi) {
			Graphs.addEdgeWithVertices(this.grafo, a.getD1(), a.getD2(), a.getPeso());
		}
				
	}
	
	public String nVertici() {
		return "Grafo creato!"+"\n"+"#verici: "+ this.grafo.vertexSet().size()+"\n";
	}
	
	public String nArchi() {
		return "#archi: "+ this.grafo.edgeSet().size()+"\n";
	}
	
	public List<DirectPeso> getAdiacenze (Director d){
		List<DirectPeso> adiacenze= new ArrayList<DirectPeso>();
		Director d1=null;
		
		for(DefaultWeightedEdge e: this.grafo.outgoingEdgesOf(d)) {
			d1=Graphs.getOppositeVertex(this.grafo, e, d);
			
			DefaultWeightedEdge ee= this.grafo.getEdge(d, d1);
			
			double peso= this.grafo.getEdgeWeight(ee);
			
			DirectPeso dp= new DirectPeso(d1, peso);
			adiacenze.add(dp);
			
		}
		
		Collections.sort(adiacenze, new ComparatoreDiAdiacenze());
		
		return adiacenze;
	}
	
	public String registriAffini(Director dir, double c) {
		String s="";
		
		this.best= new ArrayList<Director>();
		List<Director> parziale= new ArrayList<Director>();

		for(DirectPeso d: this.getAdiacenze(dir)) {
			parziale.add(d.getD());
			this.bestPeso+= d.getPeso();
		}
		double peso=bestPeso;
		cerca(c,parziale, peso);
		
		for(Director di: best) {
			s+=di+"\n";
		}
		
		return s+"Il peso massimo è: "+ this.bestPeso;
	}

	private void cerca(double c, List<Director> parziale, double peso) {
		
		if(parziale.size()>best.size()) {
			this.best= new ArrayList<Director>(parziale);
			this.bestPeso=peso;
		}
		
		for(Director di: Graphs.neighborListOf(this.grafo, parziale.get(parziale.size()-1))) {
			
			DefaultWeightedEdge ee= this.grafo.getEdge(parziale.get(parziale.size()-1), di);
			peso+= this.grafo.getEdgeWeight(ee);
			if(peso<=c && !(parziale.contains(di))) {
				parziale.add(di);
			
				parziale.remove(parziale.size()-1);
				peso-=this.grafo.getEdgeWeight(ee);
			}
			else return;
			
		}
	}
}
