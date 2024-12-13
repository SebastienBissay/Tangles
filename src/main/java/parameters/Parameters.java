package parameters;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static processing.core.PConstants.HSB;

public final class Parameters {
    public static final long SEED = 20240421;
    public static final int WIDTH = 1000;
    public static final int HEIGHT = 1000;
    public static final int MARGIN = 50;
    public static final int NUMBER_OF_TANGLES = 100;
    public static final int NUMBER_OF_ITERATIONS = 50;
    public static final float NOISE_2D_SCALE = .001f;
    public static final float NOISE_Z_SCALE = .02f;
    public static final float BASE_OUTER_STROKE_WEIGHT = 10f;
    public static final float OUTER_STROKE_WEIGHT_VARIANCE = 90f;
    public static final float OUTER_STROKE_WEIGHT_NOISE_SCALE = .01f;
    public static final float BASE_INNER_STROKE_WEIGHT = 0f;
    public static final float INNER_STROKE_WEIGHT_VARIANCE = 50f;
    public static final float INNER_STROKE_WEIGHT_NOISE_SCALE = .01f;
    public static final Color BACKGROUND_COLOR = new Color(0);
    public static final int COLOR_MODE = HSB;
    public static final float HUE_MAX_CHANGE = .0005f;
    public static final float STROKE_SATURATION = .35f;
    public static final float STROKE_BRIGHTNESS = .75f;

    /**
     * Helper method to extract the constants in order to save them to a json file
     *
     * @return a Map of the constants (name -> value)
     */
    public static Map<String, ?> toJsonMap() throws IllegalAccessException {
        Map<String, Object> map = new HashMap<>();

        Field[] declaredFields = Parameters.class.getDeclaredFields();
        for (Field field : declaredFields) {
            field.setAccessible(true);
            map.put(field.getName(), field.get(Parameters.class));
        }

        return Collections.singletonMap(Parameters.class.getSimpleName(), map);
    }

    public record Color(float red, float green, float blue, float alpha) {
        public Color(float red, float green, float blue) {
            this(red, green, blue, 255);
        }

        public Color(float grayscale, float alpha) {
            this(grayscale, grayscale, grayscale, alpha);
        }

        public Color(float grayscale) {
            this(grayscale, 255);
        }

        public Color(String hexCode) {
            this(decode(hexCode));
        }

        public Color(Color color) {
            this(color.red, color.green, color.blue, color.alpha);
        }

        public static Color decode(String hexCode) {
            return switch (hexCode.length()) {
                case 2 -> new Color(Integer.valueOf(hexCode, 16));
                case 4 -> new Color(Integer.valueOf(hexCode.substring(0, 2), 16),
                        Integer.valueOf(hexCode.substring(2, 4), 16));
                case 6 -> new Color(Integer.valueOf(hexCode.substring(0, 2), 16),
                        Integer.valueOf(hexCode.substring(2, 4), 16),
                        Integer.valueOf(hexCode.substring(4, 6), 16));
                case 8 -> new Color(Integer.valueOf(hexCode.substring(0, 2), 16),
                        Integer.valueOf(hexCode.substring(2, 4), 16),
                        Integer.valueOf(hexCode.substring(4, 6), 16),
                        Integer.valueOf(hexCode.substring(6, 8), 16));
                default -> throw new IllegalArgumentException();
            };
        }
    }
}
