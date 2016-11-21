import no.hib.dat157.genetic.Genetic;
import no.hib.dat157.genetic.Quality;

public class Main {
	
	private static int[][] cookieTerrain = {
		{1, 2, 3, 4, 5, 4, 3, 2, 1},
		{2, 3, 4, 5, 6, 5, 4, 3, 2},
		{3, 4, 5, 6, 7, 6, 5, 4, 3},
		{4, 5, 6, 7, 8, 7, 6, 5, 4},
		{5, 6, 7, 8, 9, 8, 7, 6, 5},
		{4, 5, 6, 7, 8, 7, 6, 5, 4},
		{3, 4, 5, 6, 7, 6, 5, 4, 3},
		{2, 3, 4, 5, 6, 5, 4, 3, 2},
		{1, 2, 3, 4, 5, 4, 3, 2, 1}
	};
	
	private static int[][] graveTerrain = {
		{1, 2, 3, 4, 5, 4, 3, 2, 1},
		{2, 0, 0, 0, 0, 0, 0, 0, 2},
		{3, 0, 0, 0, 0, 0, 0, 0, 3},
		{4, 0, 0, 7, 8, 7, 0, 0, 4},
		{5, 0, 0, 8, 9, 8, 0, 0, 5},
		{4, 0, 0, 7, 8, 7, 0, 0, 4},
		{3, 0, 0, 0, 0, 0, 0, 0, 3},
		{2, 0, 0, 0, 0, 0, 0, 0, 2},
		{1, 2, 3, 4, 5, 4, 3, 2, 1}
	};

	public static void main(String[] args) {
		Quality qualityFunc = new Quality() {
			public double qualityOf(int[] genes) {
				return graveTerrain[genes[0]][genes[1]];
			}
		};
		
		Genetic cookies = new Genetic(qualityFunc, 2, 1, 9, new int[]{0, 0});
		cookies.setCrossovers(1);
		cookies.setChromosomeCount(4);
		double winner = 0;
		//cookies.run(9);
		
		System.out.println("Starting configuration:");
		System.out.println("0, 0");
		System.out.println();
		
		for (int i = 1; winner < 9; ++i) {
			cookies.run();
			int[][] out = cookies.getAll();
			System.out.println("Iteration " + i + ":");
			for (int j = 0; j < out.length; ++j) {
				System.out.println(out[j][0] + ", " + out[j][1]);
			}
			System.out.println();
			winner = qualityFunc.qualityOf(out[0]);
		}
	}

}
