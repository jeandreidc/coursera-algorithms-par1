import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;


public class FastCollinearPoints {
    private static LineSegment[] _segments;
    private static final int COLLINEAR_COUNT = 3;

    public FastCollinearPoints(Point[] points)    // finds all line segments containing 4 points
    {
        if (points == null || Arrays.asList(points).contains(null)) throw new IllegalArgumentException();


        Point[] thePoints = Arrays.copyOf(points, points.length);
        Arrays.sort(thePoints);

        for (int i = 0; i < thePoints.length - 1; i++) {
            if (thePoints[i].compareTo(thePoints[i + 1]) == 0)
                throw new IllegalArgumentException();
        }
        _segments = generateLineSegments(thePoints);
    }

    private LineSegment[] generateLineSegments(Point[] points) {
        int n = points.length;
        ArrayList<LineSegment> lineSegments = new ArrayList<LineSegment>();
        for (int p = 0; p < n; p++) {
            Point[] aux = Arrays.copyOf(points, points.length);
            Point origin = points[p];
            //sort(points, aux, p, n - 1, origin.slopeOrder());
            Arrays.sort(aux, origin.slopeOrder());

            Point start = null;
            int count = 0;

            for (int i = 1; i < n; i++) {
                if (origin.slopeTo(aux[i]) == origin.slopeTo(aux[i - 1])) {
                    if (origin.compareTo(aux[i - 1]) > 0) {
                        break;
                    }

                    if (count == 0) {
                        count++;
                    }
                    count++;
                } else {
                    if (count >= COLLINEAR_COUNT) {
                        lineSegments.add(new LineSegment(origin, aux[i - 1]));
                    }
                    count = 0;
                    start = null;
                }
            }

            if (count >= COLLINEAR_COUNT && origin.compareTo(aux[aux.length - 1]) <= 0) {
                lineSegments.add(new LineSegment(origin, aux[aux.length - 1]));
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


    private static void sort(Point[] a, Point[] aux, int lo, int hi, Comparator<Point> comparator) {
        if (hi <= lo) return;
        int mid = lo + (hi - lo) / 2;
        sort(a, aux, lo, mid, comparator);
        sort(a, aux, mid + 1, hi, comparator);
        //if (comparator.compare(aux[mid - 1], a[mid]) <= 0) return;
        merge(a, aux, lo, mid, hi, comparator);
    }

    private static void merge(Point[] a, Point[] aux, int lo, int mid, int hi, Comparator<Point> comparator) {
        int i = lo;
        int j = mid + 1;

        for (int k = lo; k <= hi; k++) {
            if (i > mid) aux[k] = a[j++];
            else if (j > hi) aux[k] = a[i++];
            else if (comparator.compare(a[j], a[i]) < 0) aux[k] = a[j++];
            else aux[k] = a[i++];
        }
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
        FastCollinearPoints collinear = new FastCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}
