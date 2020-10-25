import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Arrays;

public class BruteCollinearPoints {
    private static LineSegment[] _segments;

    public BruteCollinearPoints(Point[] points)    // finds all line segments containing 4 points
    {
        if (points == null || Arrays.asList(points).contains(null)) throw new IllegalArgumentException();
        Point[] thePoints = Arrays.copyOf(points, points.length);

        for (int i = 0; i < thePoints.length - 1; i++) {
            if (thePoints[i].compareTo(thePoints[i + 1]) == 0)
                throw new IllegalArgumentException();
        }

        _segments = generateLineSegments(points);
    }

    private LineSegment[] generateLineSegments(Point[] points) {
        int n = points.length;
        Arrays.sort(points);
        ArrayList<LineSegment> lineSegments = new ArrayList<LineSegment>();
        int currentIdx = 0;
        outerloop:
        for (int p = 0; p < n - 3; p++) {
            for (int q = p + 1; q < n - 2; q++) {
                for (int r = q + 1; r < n - 1; r++) {
                    if (points[p].slopeTo(points[q]) != points[p].slopeTo(points[r]))
                        continue;

                    for (int s = r + 1; s < n; s++) {
                        if (points[p].slopeTo(points[r]) != points[p].slopeTo(points[s]))
                            continue;
                        if (p < s && p < q && p < r) {
                            lineSegments.add(new LineSegment(points[p], points[s]));
                            continue outerloop;
                        }
                    }
                }
            }
        }
        return lineSegments.toArray(new LineSegment[0]);
    }

    public int numberOfSegments()        // the number of line segments
    {
        return _segments.length;
    }

    public LineSegment[] segments()                // the line segments
    {
        return Arrays.copyOf(_segments, _segments.length);
    }

    public static void main(String[] args) {

        // read the n points from a file
        In in = new In(args[0]);
        int n = in.readInt();
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }

        // draw the points
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        for (Point p : points) {
            p.draw();
        }
        StdDraw.show();

        // print and draw the line segments
        BruteCollinearPoints collinear = new BruteCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}
