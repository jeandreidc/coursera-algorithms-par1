import edu.princeton.cs.algs4.*;

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
            _root = new PointNode(p, 1, true);
            return;
        }
        kdTreeInsert(_root, p, !_root._isX);
    }

    private PointNode kdTreeInsert(PointNode pn, Point2D p, boolean isX)
    {
        if(pn == null){
           // StdOut.printf("INSERTED Point (%f, %f) %s\n", p.x(), p.y(), isX ? "X" : "Y");
            return new PointNode(p, 1, isX);
        }
        if(pn.compareTo(p) > 0) pn._left = kdTreeInsert(pn._left, p, !pn._isX);
        else if(pn.compareTo(p) < 0) pn._right = kdTreeInsert(pn._right, p, !pn._isX);

        pn._size = size(pn._right) + size(pn._left) + 1;
        return pn;
    }

    private int size(PointNode pn) {
        if (pn == null) return 0;
        return pn._size;
    }

    public boolean contains(Point2D p)   // does the set contain point p?
    {
        return false;
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
        // StdOut.printf("Point (%f, %f) %s\n", p._point.x(), p._point.y(), p._isX ? "X" : "Y");
        p._point.draw();
        traverseDraw(p._right);
    }

    public Iterable<Point2D> range(RectHV rect)    // all points that are inside the rectangle (or on the boundary)
    {
        return null;
    }

    public Point2D nearest(Point2D p)    // a nearest neighbor in the set to point p; null if the set is empty
    {
        return null;
    }

    public static void main(String[] args)// unit testing of the methods (optional)
    {
        RectHV rect = new RectHV(0.0, 0.0, 1.0, 1.0);
        StdDraw.enableDoubleBuffering();
        KdTree kdtree = new KdTree();
        while (true) {
            if (StdDraw.isMousePressed()) {
                double x = StdDraw.mouseX();
                double y = StdDraw.mouseY();
                StdOut.printf("%8.6f %8.6f\n", x, y);
                Point2D p = new Point2D(x, y);
                if (rect.contains(p)) {
                    StdOut.printf("%8.6f %8.6f\n", x, y);
                    kdtree.insert(p);
                    StdDraw.clear();
                    kdtree.draw();
                    StdDraw.show();
                }
            }
            StdDraw.pause(20);
        }
    }

    private class PointNode implements Comparable<Point2D> {
        private Point2D _point;
        private int _size;
        private boolean _isX;
        private PointNode _left, _right;

        private PointNode(Point2D p, int size, boolean isX) {
            _point = p;
            _size = size;
            _isX = isX;
        }

        public int compareTo(Point2D pn) {
            double diff = _isX ? _point.x() - pn.x() : _point.y() - pn.y();

            if (diff != 0.0) return diff > 0.0 ? 1 : -1;
            return 0;
        }
    }
}
