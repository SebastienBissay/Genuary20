package genuary._2025;

import processing.core.PApplet;
import processing.core.PVector;

import static genuary._2025.parameters.Parameters.*;
import static processing.core.PApplet.*;

class Segment {

    public PVector a, b;
    public float lengthSq;

    public Segment(PVector a, PVector b) {
        this.a = a;
        this.b = b;
        this.lengthSq = sq(a.x - b.x) + sq(a.y - b.y);
    }

    public PVector getIntersectionPoint(Segment segment) {
        float uA = ((segment.b.x - segment.a.x) * (a.y - segment.a.y)
                - (segment.b.y - segment.a.y) * (a.x - segment.a.x))
                / ((segment.b.y - segment.a.y) * (b.x - a.x)
                - (segment.b.x - segment.a.x) * (b.y - a.y));
        float uB = ((b.x - a.x) * (a.y - segment.a.y)
                - (b.y - a.y) * (a.x - segment.a.x))
                / ((segment.b.y - segment.a.y) * (b.x - a.x)
                - (segment.b.x - segment.a.x) * (b.y - a.y));

        if (uA >= 0 && uA <= 1 && uB >= 0 && uB <= 1) {
            return new PVector(a.x + uA * (b.x - a.x), a.y + uA * (b.y - a.y));
        }
        return null;
    }

    public void render(PApplet pApplet) {
        pApplet.stroke(STROKE_COLOR.red(), STROKE_COLOR.green(), STROKE_COLOR.blue(), STROKE_COLOR.alpha());
        float dist = dist(a.x, a.y, b.x, b.y);
        pApplet.strokeWeight(map(dist, 0, .8f * min(WIDTH, HEIGHT), MIN_STROKE_WEIGHT, MAX_STROKE_WEIGHT));
        pApplet.line(a.x, a.y, b.x, b.y);

        Color color = FILL_PALETTE[floor(pApplet.random(FILL_PALETTE.length))];
        pApplet.stroke(color.red(), color.green(), color.blue(), color.alpha());
        pApplet.strokeWeight(FILL_STROKE_WEIGHT);
        PVector normal = PVector.fromAngle(PVector.sub(a, b).heading() - HALF_PI);
        float lineLength = pApplet.random(FILL_MIN_LINE_LENGTH_FACTOR, FILL_MAX_LINE_LENGTH_FACTOR) * dist;
        for (int i = 0; i < dist; i++) {
            PVector p = PVector.add(a, PVector.mult(PVector.sub(b, a), i / dist));
            PVector q = PVector.add(p, PVector.mult(normal, lineLength));
            pApplet.line(p.x, p.y, q.x, q.y);
            lineLength += pApplet.randomGaussian();
        }
    }
}