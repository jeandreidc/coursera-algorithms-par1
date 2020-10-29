import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Comparator;

public class Solver {
    private final Board _board;
    private int _numberOfTurns = 0;
    private MinPQ<BoardNode> _minPQ;
    private ArrayList<Board> _solution;

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        _board = initial;
        _minPQ = new MinPQ<BoardNode>(new BoardComparatorManhattan());
        _minPQ.insert(new BoardNode(initial, null, 0));
        solve();
    }

    // is the initial board solvable? (see below)
    public boolean isSolvable() {
        return _solution != null;
    }

    // min number of moves to solve initial board; -1 if unsolvable
    public int moves() {
        return _numberOfTurns;
    }

    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution() {
        return _solution;
    }

    private void solve() {
        BoardNode currentNode = null;
        _solution = new ArrayList<Board>();
        while (true) {
            currentNode = _minPQ.delMin();
            _solution.add(currentNode.Board);
            if (currentNode.Manhattan == 0) break;
            int countBeforeNeighbors = _minPQ.size();
            for (Board neighbor : currentNode.Board.neighbors()) {
                BoardNode neighborNode = new BoardNode(neighbor, currentNode, currentNode.Moves + 1);
                if (neighborNode.PreviousNode != null && neighborNode.PreviousNode.Board != neighborNode.Board) {
                    _minPQ.insert(neighborNode);
                }
            }
            if (countBeforeNeighbors == _minPQ.size()) {
                _solution = null;
                _numberOfTurns = -1;
                return;
            }
        }

        _numberOfTurns = currentNode.Moves;
    }

    private class BoardNode {
        public int Moves;
        public Board Board;
        public int Manhattan;
        public int Hamming;
        public BoardNode PreviousNode;

        public BoardNode(Board board, BoardNode previousNode, int moves) {
            this.Board = board;
            this.PreviousNode = previousNode;
            this.Moves = moves;
            this.Manhattan = board.manhattan();
            this.Hamming = board.hamming();
        }
    }

    private class BoardComparatorManhattan implements Comparator<BoardNode> {
        public int compare(BoardNode b1, BoardNode b2) {
            return (b1.Manhattan + b1.Moves) - (b2.Manhattan + b2.Moves);
        }
    }

    // test client (see below)
    public static void main(String[] args) {
        if (args == null) throw new IllegalArgumentException();

        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                tiles[i][j] = in.readInt();

        Board initial = new Board(tiles);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }

}
