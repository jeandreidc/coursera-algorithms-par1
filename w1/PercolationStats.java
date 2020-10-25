import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
    private static final double CONFIDENCE_CONSTANT = 1.96;
    private final double[] trialsResults;

    // perform independent trials on an n-by-n grid
    public PercolationStats(int n, int trials) {
        if (n <= 0 || trials <= 0)
            throw new IllegalArgumentException("Size or trials cannot be 0 or less than 0");

        trialsResults = new double[trials];
        for (int i = 0; i < trials; i++) {
            Percolation percolation = new Percolation(n);

            while (!percolation.percolates()) {
                int p = StdRandom.uniform(1, n + 1);
                int q = StdRandom.uniform(1, n + 1);
                percolation.open(p, q);
            }
            double totalNumberOfOpenSites = percolation.numberOfOpenSites();
            trialsResults[i] = totalNumberOfOpenSites / (n * n);
        }
    }

    // sample mean of percolation threshold
    public double mean() {
        return StdStats.mean(trialsResults);
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        return StdStats.stddev(trialsResults);
    }

    // low endpoint of 95% confidence interval
    public double confidenceLo() {
        return mean() - CONFIDENCE_CONSTANT / Math.sqrt(trialsResults.length);
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        return mean() + CONFIDENCE_CONSTANT / Math.sqrt(trialsResults.length);
    }

    // test client (see below)
    public static void main(String[] args) {
        int n = Integer.parseInt(args[0]);
        int trials = Integer.parseInt(args[1]);
        PercolationStats ps = new PercolationStats(n, trials);

        StdOut.println("mean                    = " + ps.mean());
        StdOut.println("stddev                  = " + ps.stddev());
        StdOut.println("95% confidence interval  = [" + ps.confidenceLo() + ", " + ps.confidenceHi() + "]");
    }

}
