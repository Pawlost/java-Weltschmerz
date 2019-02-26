package com.ritualsoftheold.weltschmerz.landmass.fortune.nodes;

import com.ritualsoftheold.weltschmerz.landmass.PrecisionMath;
import com.ritualsoftheold.weltschmerz.landmass.fortune.geometry.VoronoiBorder;

public class BorderNode extends Node {

    public VoronoiBorder Edge;
    private boolean Flipped;

    public BorderNode(VoronoiBorder E, boolean Flipped, Node left, Node right) {
        super(left, right);
        Edge = E;
        this.Flipped = Flipped;
    }


    public double Cut(double ys, double x) {
         double l0 = Edge.LeftData.getX();
         double l1 = Edge.LeftData.getY();
         double r0 = Edge.RightData.getX();
         double r1 = Edge.RightData.getY();

        double delta;
        if (!Flipped)
            delta = ParabolicCut(l0, l1, r0, r1, ys);
        else
            delta = ParabolicCut(r0, r1, l0, l1, ys);

        return PrecisionMath.round(x - delta);
    }


    private static double ParabolicCut(double x1, double y1, double x2, double y2,
                                       double ys) {
        if (PrecisionMath.eq(x1, x2) && PrecisionMath.eq(y1, y2))
            throw new IllegalArgumentException(
                    "Identical datapoints are not allowed!");

        if (PrecisionMath.eq(y1, ys) && PrecisionMath.eq(y2, ys))
            return (x1 + x2) / 2;
        if (PrecisionMath.eq(y1, ys))
            return x1;
        if (PrecisionMath.eq(y2, ys))
            return x2;
        final double a1 = 1 / (2 * (y1 - ys));
        final double a2 = 1 / (2 * (y2 - ys));
        if (PrecisionMath.eq(a1, a2))
            return (x1 + x2) / 2;
        double xs1 = 0.5
                / (2 * a1 - 2 * a2)
                * (4 * a1 * x1 - 4 * a2 * x2 + 2 * Math.sqrt(-8 * a1 * x1 * a2
                * x2 - 2 * a1 * y1 + 2 * a1 * y2 + 4 * a1 * a2 * x2
                * x2 + 2 * a2 * y1 + 4 * a2 * a1 * x1 * x1 - 2 * a2
                * y2));
        double xs2 = 0.5
                / (2 * a1 - 2 * a2)
                * (4 * a1 * x1 - 4 * a2 * x2 - 2 * Math.sqrt(-8 * a1 * x1 * a2
                * x2 - 2 * a1 * y1 + 2 * a1 * y2 + 4 * a1 * a2 * x2
                * x2 + 2 * a2 * y1 + 4 * a2 * a1 * x1 * x1 - 2 * a2
                * y2));
        xs1 = PrecisionMath.round(xs1);
        xs2 = PrecisionMath.round(xs2);
        if (xs1 > xs2) {
            final double h = xs1;
            xs1 = xs2;
            xs2 = h;
        }
        if (y1 >= y2)
            return xs2;
        return xs1;
    }
}
