import processing.core.PApplet;
import processing.core.PVector;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import static parameters.Parameters.*;
import static save.SaveUtil.saveSketch;

public class Tangles extends PApplet {

    private float hue;

    public static void main(String[] args) {
        PApplet.main(Tangles.class);
    }

    @Override
    public void settings() {
        size(WIDTH, HEIGHT);
        randomSeed(SEED);
        noiseSeed(floor(random(MAX_INT)));
    }

    @Override
    public void setup() {
        colorMode(COLOR_MODE, 1);
        background(BACKGROUND_COLOR.red(), BACKGROUND_COLOR.green(), BACKGROUND_COLOR.blue());

        hue = random(1);
    }

    @Override
    public void draw() {
        Function<PVector, Float> outerStrokeWeightFormula = (point) ->
                BASE_OUTER_STROKE_WEIGHT + OUTER_STROKE_WEIGHT_VARIANCE
                        * sq(noise(point.x * OUTER_STROKE_WEIGHT_NOISE_SCALE,
                        point.y * OUTER_STROKE_WEIGHT_NOISE_SCALE));
        Function<PVector, Float> innerStrokeWeightFormula = (point) ->
                BASE_INNER_STROKE_WEIGHT + INNER_STROKE_WEIGHT_VARIANCE
                        * sq(noise(point.x * INNER_STROKE_WEIGHT_NOISE_SCALE,
                        point.y * INNER_STROKE_WEIGHT_NOISE_SCALE));

        for (int k = 0; k < NUMBER_OF_TANGLES; k++) {
            PVector p;
            if (coinFlip()) {
                p = new PVector(random(width), coinFlip() ? -MARGIN / 2f : height + MARGIN / 2f);
            } else {
                p = new PVector(coinFlip() ? -MARGIN / 2f : width + MARGIN / 2f, random(height));
            }
            List<PVector> segment = new ArrayList<>();
            do {
                segment.add(p.copy());
                p.add(PVector.fromAngle(sq(TWO_PI) * noise(p.x * NOISE_2D_SCALE,
                        p.y * NOISE_2D_SCALE,
                        frameCount * NOISE_Z_SCALE)));
            } while (p.x >= -MARGIN && p.x <= width + MARGIN && p.y >= -MARGIN && p.y <= height + MARGIN);

            stroke(BACKGROUND_COLOR.red(), BACKGROUND_COLOR.green(), BACKGROUND_COLOR.blue());
            drawSegment(segment, outerStrokeWeightFormula);

            hue = (hue + random(random(HUE_MAX_CHANGE))) % 1;
            stroke(hue, STROKE_SATURATION, STROKE_BRIGHTNESS);
            drawSegment(segment, innerStrokeWeightFormula);
        }

        if (frameCount >= NUMBER_OF_ITERATIONS) {
            noLoop();
            saveSketch(this);
        }
    }

    private boolean coinFlip() {
        return random(1) > .5;
    }

    private void drawSegment(List<PVector> segment, Function<PVector, Float> strokeWeightFormula) {
        segment.forEach(point -> {
            strokeWeight(strokeWeightFormula.apply(point));
            point(point.x, point.y);
        });
    }
}
