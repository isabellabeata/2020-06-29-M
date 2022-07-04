package it.polito.tdp.imdb.model;

import java.util.Comparator;

public class ComparatoreDiAdiacenze implements Comparator<DirectPeso> {

	@Override
	public int compare(DirectPeso o1, DirectPeso o2) {
		// TODO Auto-generated method stub
		return -(int) (o1.getPeso()-o2.getPeso());
	}

}
