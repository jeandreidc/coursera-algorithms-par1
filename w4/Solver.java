import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Comparator;

public class Solver {
    private int _numberOfTurns = 0;
    private ArrayList<Board> _solution;

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        if (initial == null) throw new IllegalArgumentException();
        solve(initial);
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

    private void solve(Board initial) {
        MinPQ<BoardNode> minPQ = new MinPQ<BoardNode>(new BoardComparatorManhattan());
        MinPQ<BoardNode> minPQTwin = new MinPQ<BoardNode>(new BoardComparatorManhattan());
        minPQ.insert(new BoardNode(initial, null, 0));
        minPQTwin.insert(new BoardNode(initial.twin(), null, 0));

        BoardNode currentNode = null;
        BoardNode currentNodeTwin = null;
        _solution = new ArrayList<Board>();
        while (true) {
            currentNode = minPQ.delMin();
            _solution.add(currentNode.Board);
            if (currentNode.Board.isGoal()) break;
            minPQ = new MinPQ<BoardNode>(new BoardComparatorManhattan());

            for (Board neighbor : currentNode.Board.neighbors()) {
                BoardNode neighborNode = new BoardNode(neighbor, currentNode, currentNode.Moves + 1);
                if (neighborNode.PreviousNode != null && !currentNode.Board.equals(neighborNode.Board)) {
                    minPQ.insert(neighborNode);
                }
            }

            currentNodeTwin = minPQTwin.delMin();
            _solution.add(currentNodeTwin.Board);
            if (currentNodeTwin.Board.isGoal()) {
                _solution = null;
                _numberOfTurns = -1;
                return;
            }
            minPQTwin = new MinPQ<BoardNode>(new BoardComparatorManhattan());

            for (Board neighbor : currentNodeTwin.Board.neighbors()) {
                BoardNode neighborNode = new BoardNode(neighbor, currentNodeTwin, currentNodeTwin.Moves + 1);
                if (neighborNode.PreviousNode != null && !neighborNode.PreviousNode.Board.equals(neighborNode.Board)) {
                    minPQTwin.insert(neighborNode);
                }
            }
        }

        _numberOfTurns = currentNode.Moves;
    }

    private class BoardNode {
        public int Moves;
        public int Manhattan;
        public Board Board;
        public BoardNode PreviousNode;

        public BoardNode(Board board, BoardNode previousNode, int moves) {
            this.Board = board;
            this.Manhattan = board.manhattan();
            this.PreviousNode = previousNode;
            this.Moves = moves;
        }
    }

    private class BoardComparatorManhattan implements Comparator<BoardNode> {
        public int compare(BoardNode b1, BoardNode b2) {
            return (b1.Manhattan + b1.Moves) - (b2.Manhattan + b2.Moves);
        }
    }

    // test client (see below)(
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
