import edu.princeton.cs.algs4.*;

import java.util.ArrayList;
public class KdTree {
    private PointNode _root = null;

    public KdTree()    // construct an empty set of points
    {}

    public boolean isEmpty()    // is the set empty?
    {
        return _root == null;
    }

    public int size()       // number of points in the set
    {
        if(_root == null) return 0;
        return _root._size;
    }

    public void insert(Point2D p)     // add the point to the set (if it is not already in the set)
    {
        if(_root == null) {
            _root = new PointNode(p, 1, true, new RectHV(0.0, 0.0, 1.0, 1.0));
            return;
        }
        kdTreeInsert(_root, p, !_root._isX, null);
    }

    private PointNode kdTreeInsert(PointNode pn, Point2D p, boolean isX, RectHV rectangle)
    {
        if(pn == null){
           // StdOut.printf("INSERTED Point (%f, %f) %s\n", p.x(), p.y(), isX ? "X" : "Y");
            return new PointNode(p, 1, isX, rectangle);
        }

        if(pn.compareTo(p) > 0) pn._left = kdTreeInsert(pn._left, p, !pn._isX, computeRectangle(false, pn));
        else if(pn.compareTo(p) < 0) pn._right = kdTreeInsert(pn._right, p, !pn._isX, computeRectangle(true, pn));

        pn._size = size(pn._right) + size(pn._left) + 1;
        return pn;
    }

    private RectHV computeRectangle(boolean isMin, PointNode parent) {
       double xMin = parent._rect.xmin(), yMin = parent._rect.ymin(), xMax = parent._rect.xmax(), yMax = parent._rect.ymax();

        if(isMin) {
            if(parent._isX) xMin = parent._rect.xmin();
            else yMin = parent._rect.ymin();
        } else {
            if(parent._isX) xMax = parent._rect.xmax();
            else yMax = parent._rect.ymax();
        }
        return new RectHV(xMin, yMin, xMax, yMax);
    }

    private int size(PointNode pn) {
        if (pn == null) return 0;
        return pn._size;
    }

    public boolean contains(Point2D p)   // does the set contain point p?
    {
        if (_root == null) return  false;
        return _root._rect.contains(p);
    }

    public void draw()       // draw all points to standard draw
    {
        StdDraw.setPenRadius(0.03);
        traverseDraw(_root);
    }

    private void traverseDraw(PointNode p) {
        if (p == null) return;
        traverseDraw(p._left);

        StdDraw.setPenColor(StdDraw.BLUE);
        if(p._isX) StdDraw.setPenColor(StdDraw.RED);
        p._point.draw();
        traverseDraw(p._right);
    }

    public Iterable<Point2D> range(RectHV rect)    // all points that are inside the rectangle (or on the boundary)
    {
        ArrayList<Point2D> points = new ArrayList<Point2D>();
        checkForPointIntersections(_root, rect, points);
        return points;
    }

    private void checkForPointIntersections(PointNode p, RectHV rect, ArrayList<Point2D> points)
    {
        if (p == null) return;

        if(p._rect.intersects(rect)) {
            checkForPointIntersections(p._left, rect, points);
            checkForPointIntersections(p._right, rect, points);

            if(rect.contains(p._point)) points.add(p._point);
        }
    }

    public Point2D nearest(Point2D p)    // a nearest neighbor in the set to point p; null if the set is empty
    {
        Point2D champion = p;
        recursiveSearch(_root, p, champion);
        return champion;
    }

    private void recursiveSearch(PointNode pn, Point2D p, Point2D champion){
        if (pn == null) return;

        if(p.distanceTo(pn._point) < p.distanceTo(champion)) {
            champion = pn._point;
        }

        recursiveSearch(pn._left, p, champion);
        recursiveSearch(pn._right, p, champion);
    }

    public static void main(String[] args)// unit testing of the methods (optional)
    {
        // initialize the two data structures with point from file
        String filename = args[0];
        In in = new In(filename);
        PointSET brute = new PointSET();
        KdTree kdtree = new KdTree();
        while (!in.isEmpty()) {
            double x = in.readDouble();
            double y = in.readDouble();
            Point2D p = new Point2D(x, y);
            kdtree.insert(p);
            brute.insert(p);
        }

        // process nearest neighbor queries
        StdDraw.enableDoubleBuffering();
        while (true) {

            // the location (x, y) of the mouse
            double x = StdDraw.mouseX();
            double y = StdDraw.mouseY();
            Point2D query = new Point2D(x, y);

            // draw all of the points
            StdDraw.clear();
            StdDraw.setPenColor(StdDraw.BLACK);
            StdDraw.setPenRadius(0.01);
            brute.draw();

            // draw in red the nearest neighbor (using brute-force algorithm)
            StdDraw.setPenRadius(0.03);
            StdDraw.setPenColor(StdDraw.RED);
            brute.nearest(query).draw();
            StdDraw.setPenRadius(0.02);

            // draw in blue the nearest neighbor (using kd-tree algorithm)
            StdDraw.setPenColor(StdDraw.BLUE);
            kdtree.nearest(query).draw();
            StdDraw.show();
            StdDraw.pause(40);
        }
    }

    private class PointNode implements Comparable<Point2D> {
        private Point2D _point;
        private int _size;
        private boolean _isX;
        private PointNode _left, _right;
        private RectHV _rect;

        private PointNode(Point2D p, int size, boolean isX, RectHV rect) {
            _point = p;
            _size = size;
            _isX = isX;
            _rect = rect;
        }

        public int compareTo(Point2D pn) {
            double diff = _isX ? _point.x() - pn.x() : _point.y() - pn.y();

            if (diff != 0.0) return diff > 0.0 ? 1 : -1;
            return 0;
        }
    }
}
