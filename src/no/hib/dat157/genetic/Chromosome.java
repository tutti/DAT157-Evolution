package no.hib.dat157.genetic;

import java.util.concurrent.ThreadLocalRandom;

public class Chromosome {
	private int[] genes;
	private int min = Integer.MIN_VALUE;
	private int max = Integer.MAX_VALUE - 1;
	private double quality;
	private boolean qualityIsSet = false;
	
	// Main constructor - copies the argument array and sets the gene limit to intmax.
	public Chromosome(int[] init, int min, int max) {
		qualityIsSet = false;
		genes = new int[init.length];
		for (int i = 0; i < genes.length; ++i) {
			genes[i] = init[i];
		}
		
		this.min = min;
		this.max = max;
	}
	
	// Convenience constructors - basically allow for default values.
	public Chromosome(int size, int min, int max) {
		this(new int[size], min, max);
	}
	public Chromosome(int[] init) {
		this(init, Integer.MIN_VALUE, Integer.MAX_VALUE - 1);
	}
	public Chromosome(int size) {
		this(size, Integer.MIN_VALUE, Integer.MAX_VALUE - 1);
	}
	
	public void randomise() {
		qualityIsSet = false;
		for (int i = 0; i < genes.length; ++i) {
			genes[i] = ThreadLocalRandom.current().nextInt(min, max + 1);
		}
	}
	
	public int size() {
		return genes.length;
	}
	
	public double quality(Quality qf) {
		if (qualityIsSet) return quality;
		quality = qf.qualityOf(genes);
		qualityIsSet = true;
		return quality;
	}
	
	public Chromosome mutate() {
		Chromosome other = new Chromosome(genes, min, max);
		int gene = ThreadLocalRandom.current().nextInt(0, genes.length);
		boolean inc = ThreadLocalRandom.current().nextBoolean();
		if (inc)
			other.genes[gene]++;
		else
			other.genes[gene]--;
		if (other.genes[gene] > max)
			other.genes[gene] -= 2;
		if (other.genes[gene] < min) {
			other.genes[gene] += 2;
		}
		return other;
	}
	
	public void crossover(Chromosome other) {
		qualityIsSet = false;
		int gene = ThreadLocalRandom.current().nextInt(0, genes.length);
		int temp = genes[gene];
		genes[gene] = other.genes[gene];
		other.genes[gene] = temp;
	}
	
	public int[] getValues() {
		int[] ret = new int[genes.length];
		for (int i = 0; i < ret.length; ++i) {
			ret[i] = genes[i];
		}
		return ret;
	}
	
	public boolean equals(Object o) {
		if (!(o instanceof Chromosome)) {
			return false;
		}
		
		Chromosome c = (Chromosome) o;
		
		if (genes.length != c.genes.length) return false;
		if (min != c.min) return false;
		if (max != c.max) return false;
		
		for (int i = 0; i < genes.length; ++i) {
			if (genes[i] != c.genes[i]) {
				return false;
			}
		}
		
		return true;
	}
}
