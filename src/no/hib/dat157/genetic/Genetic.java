package no.hib.dat157.genetic;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class Genetic {
	private Quality qualityFunction;
	private int crossovers = 5;
	//private int min = Integer.MIN_VALUE;
	//private int max = Integer.MAX_VALUE - 1;
	private int chromosomeCount = 100;
	
	private List<Chromosome> chromosomes;
	private Comparator<Chromosome> comparator;
	
	public Genetic(Quality qualityFunction, int genes, int min, int max, int[] init) {
		this.qualityFunction = qualityFunction;
		//this.min = min;
		//this.max = max;
		
		// Create the chromosome list and seed it with a random starting chromosome
		chromosomes = new ArrayList<Chromosome>();
		Chromosome first;
		if (init == null) {
			first = new Chromosome(genes, min, max);
			first.randomise();
		} else {
			first = new Chromosome(init, min, max);
		}
		chromosomes.add(first);
		
		comparator = new Comparator<Chromosome>() {
			public int compare(Chromosome a, Chromosome b) {
				// TODO Auto-generated method stub
				if (a.quality(qualityFunction) < b.quality(qualityFunction)) return 1;
				if (a.quality(qualityFunction) > b.quality(qualityFunction)) return -1;
				return 0;
			}
		};
	}
	
	public Genetic(Quality qualityFunction, int genes, int min, int max) {
		this(qualityFunction, genes, min, max, null);
	}
	public Genetic(Quality qualityFunction, int genes) {
		this(qualityFunction, genes, Integer.MIN_VALUE, Integer.MAX_VALUE - 1);
	}
	
	public void setQualityFunction(Quality qual) {
		qualityFunction = qual;
	}
	
	public void setCrossovers(int crossovers) {
		this.crossovers = crossovers;
	}
	
	public void setChromosomeCount(int count) {
		this.chromosomeCount = count;
	}
	
	public void run() {
		List<Chromosome> newList = new ArrayList<Chromosome>(chromosomes);
		
		// Mutate each chromosome and add them to the chromosome list
		for (Chromosome chrom : chromosomes) {
			Chromosome next = chrom.mutate();
			// Avoid duplicates
			//if (newList.contains(next)) continue;
			newList.add(next);
		}
		
		// Perform crossovers
		for (int i = 0; i < crossovers; ++i) {
			int firstChrom = ThreadLocalRandom.current().nextInt(0, newList.size());
			int secondChrom = ThreadLocalRandom.current().nextInt(0, newList.size());
			newList.get(firstChrom).crossover(newList.get(secondChrom));
		}
		
		// Remove duplicates
		List<Chromosome> newList2 = new ArrayList<Chromosome>();
		for (Chromosome c : newList) {
			if (newList2.contains(c)) continue;
			newList2.add(c);
		}
		newList = newList2;
		
		// Pick the chromosomes that survive
		if (newList.size() <= chromosomeCount) {
			// If there are still fewer chromosomes than the limit, just keep them
			chromosomes = newList;
			chromosomes.sort(comparator);
		} else {
			chromosomes = new ArrayList<Chromosome>();
			
			// Sort chromosomes by quality
			newList.sort(comparator);
			
			// Keep the chromosome with the highest quality
			chromosomes.add(newList.remove(0));
			
			// Calculate quality for each chromosome
			double qSum = 0;
			for (int i = 0; i < newList.size(); ++i) {
				qSum += newList.get(i).quality(qualityFunction);
			}
			
			// Calculate fitness
			List<Double> fitness = new ArrayList<Double>();
			for (int i = 0; i < newList.size(); ++i) {
				fitness.add(newList.get(i).quality(qualityFunction) / qSum);
			}
			
			while (chromosomes.size() < chromosomeCount && newList.size() > 0) {
				double rand = ThreadLocalRandom.current().nextDouble();
				int i = 0;
				while (rand > fitness.get(i)) {
					rand -= fitness.get(i++);
					if (i >= fitness.size()) {
						i--;
						break;
					}
				}
				chromosomes.add(newList.remove(i));
				fitness.remove(i);
			}
		}
	}
	
	public void run(int iterations) {
		for (int i = 0; i < iterations; ++i) {
			run();
		}
	}
	
	public void runUntil(double targetQuality) {
		while (chromosomes.get(0).quality(qualityFunction) < targetQuality) {
			run();
		}
	}
	
	public int[] getWinner() {
		return chromosomes.get(0).getValues();
	}
	
	public int[][] getAll() {
		int[][] ret = new int[chromosomes.size()][chromosomes.get(0).size()];
		for (int i = 0; i < chromosomes.size(); ++i) {
			ret[i] = chromosomes.get(i).getValues();
		}
		return ret;
	}
	
	public double getQuality() {
		return chromosomes.get(0).quality(qualityFunction);
	}
	
}
