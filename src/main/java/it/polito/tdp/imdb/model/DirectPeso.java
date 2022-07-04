package it.polito.tdp.imdb.model;

public class DirectPeso {

	private Director d;
	private double peso;
	public DirectPeso(Director d, double peso) {
		super();
		this.d = d;
		this.peso = peso;
	}
	public Director getD() {
		return d;
	}
	public void setD(Director d) {
		this.d = d;
	}
	public double getPeso() {
		return peso;
	}
	public void setPeso(double peso) {
		this.peso = peso;
	}
	@Override
	public String toString() {
		return this.d.toString() +" - # attori condivisi: "+ this.peso+"\n";
	}
	
	
}
