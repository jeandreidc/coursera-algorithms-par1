import edu.princeton.cs.algs4.*;

import java.util.ArrayList;
import java.util.TreeSet;

public class PointSET {
    private TreeSet<PointNode> _treeSet;

    private class PointNode implements Comparable<PointNode> {
        private Point2D _point;

        private PointNode(Point2D p) {
            _point = p;
        }

        public int compareTo(PointNode pn) {
            double diffX = _point.x() - pn._point.x();
            double diffY = _point.y() - pn._point.y();

            if (diffX != 0.0) return diffX > 0.0 ? 1 : -1;

            if (diffY != 0.0) return diffY > 0.0 ? 1 : -1;

            return 0;
        }
    }

    public PointSET()     // construct an empty set of points
    {
        _treeSet = new TreeSet<PointNode>();
    }

    public boolean isEmpty()     // is the set empty?
    {
        return _treeSet.size() == 0;
    }

    public int size()        // number of points in the set
    {
        return _treeSet.size();
    }

    public void insert(Point2D p)      // add the point to the set (if it is not already in the set)
    {
        if (p == null) throw new IllegalArgumentException();
        PointNode pn = new PointNode(p);
        _treeSet.add(pn);
    }

    public boolean contains(Point2D p)    // does the set contain point p?
    {
        if (p == null) throw new IllegalArgumentException();
        return _treeSet.contains(new PointNode(p));
    }

    public void draw()        // draw all points to standard draw
    {
        int count = 0;
        for (PointNode pn : _treeSet) {
            pn._point.draw();
        }
    }

    public Iterable<Point2D> range(RectHV rect)     // all points that are inside the rectangle (or on the boundary)
    {
        if (rect == null) throw new IllegalArgumentException();
        ArrayList<Point2D> points = new ArrayList<Point2D>();
        for (PointNode pn : _treeSet) {
            if (rect.contains(pn._point)) {
                points.add(pn._point);
            }
        }
        return points;
    }

    public Point2D nearest(Point2D p)     // a nearest neighbor in the set to point p; null if the set is empty
    {
        if (p == null) throw new IllegalArgumentException();
        Point2D champion = null;
        for (PointNode pn : _treeSet) {
            if (champion == null || distance(p, pn._point) < distance(p, champion)) {
                champion = pn._point;
            }
        }
        return champion;
    }

    private double distance(Point2D p1, Point2D p2) {
        return Math.sqrt(Math.pow(p2.x() - p1.x(), 2) + Math.pow(p2.y() - p1.y(), 2));
    }

    public static void main(String[] args)          // unit testing of the methods (optional)
    {
    }
}
