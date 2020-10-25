public class Percolation {
    private final int topIdx, bottomIdx;
    private final int[] grid, gridSz;
    private final int size;
    private int openCount = 0;

    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n) {
        if (n <= 0)
            throw new IllegalArgumentException("Size cannot be 0 or less than 0");

        size = n;
        int totalSize = n * n;
        grid = new int[totalSize + 2];

        for (int i = 0; i < totalSize + 2; i++) {
            grid[i] = -1;
        }

        gridSz = new int[totalSize + 2];

        topIdx = totalSize;
        grid[topIdx] = topIdx;

        bottomIdx = totalSize + 1;
        grid[bottomIdx] = bottomIdx;
    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col) {
        if (row < 1 || row > size || col < 1 || col > size) {
            throw new IllegalArgumentException("row or col shouldn't be less than 1 or more than dimension");
        }
        int idx = getArrayIndex(row, col);

        if (grid[idx] == -1) {
            openCount++;
            grid[idx] = idx;

            if (idx / size != 0) {
                union(idx, idx - size);
            } else {
                union(idx, topIdx);

            }
            if (idx % size > 0) union(idx, idx - 1); // left
            if (idx % size < size - 1) union(idx, idx + 1); // right
            if (idx / size < size - 1) {
                union(idx, idx + size); // down
            } else {
                union(idx, bottomIdx);
            }
        }
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        if (row < 1 || row > size || col < 1 || col > size) {
            throw new IllegalArgumentException("row or col shouldn't be less than 1 or more than dimension");
        }

        return grid[getArrayIndex(row, col)] > -1;
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        if (row < 1 || row > size || col < 1 || col > size) {
            throw new IllegalArgumentException("row or col shouldn't be less than 1 or more than dimension");
        }

        return find(topIdx, getArrayIndex(row, col));
    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        return openCount;
    }

    // does the system percolate?
    public boolean percolates() {
        return find(topIdx, bottomIdx);
    }

    private boolean find(int p, int q) {
        if (grid[p] < 0 || grid[q] < 0) return false;

        return getRoot(p) == getRoot(q);
    }

    private void union(int p, int q) {
        if (grid[p] == -1 || grid[q] == -1) return;

        int rootP = getRoot(p);
        int rootQ = getRoot(q);

        if (rootP == rootQ)
            return;

        if (gridSz[rootP] < gridSz[rootQ]) {
            gridSz[rootQ] += gridSz[rootP];
            grid[rootP] = rootQ;
        } else {
            gridSz[rootP] += gridSz[rootQ];
            grid[rootQ] = rootP;
        }
    }

    private int getRoot(int idx) {
        int runner = idx;
        while (runner != grid[runner]) {
            int prevNode = runner;
            runner = grid[grid[runner]];
            grid[prevNode] = runner;
        }
        return runner;
    }

    private int getArrayIndex(int row, int col) {
        if (row > size || col > size) {
            throw new IllegalArgumentException("Neither row or col should be greater than the grid size");
        }
        return ((row - 1) * size) + col - 1;
    }
}
