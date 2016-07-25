package com.zachpuls.ocrann;

import org.apache.commons.math3.stat.correlation.PearsonsCorrelation;
import org.apache.commons.math3.transform.FastHadamardTransformer;

import java.awt.image.BufferedImage;

/**
 * Created by zachpuls on 7/24/2016.
 */

public class ClassificationParameters {

    private static int sumCm(final double c, final int m, final int n, final BufferedImage character) {
        int sum = 0;

        for (int p = 1; p < n; ++p) {
            if (character.getRGB((int)(c * m), p) == 1) {
                ++sum;
            }
        }

        return sum;
    }

    public static int h30(final BufferedImage character) {
        return sumCm(0.3, 42, 24, character);
    }

    public static int h50(final BufferedImage character) {
        return sumCm(0.5, 42, 24, character);
    }

    public static int h80(final BufferedImage character) {
        return sumCm(0.8, 42, 24, character);
    }

    private static int sumCn(final double c, final int m, final int n, final BufferedImage character) {
        int sum = 0;

        for (int p = 1; p < m; ++p) {
            if (character.getRGB(p,(int)(c * n)) == 1) {
                ++sum;
            }
        }

        return sum;
    }

    public static int v30(final BufferedImage character) {
        return sumCn(0.3, 42, 24, character);
    }

    public static int v50(final BufferedImage character) {
        return sumCn(0.3, 42, 24, character);
    }

    public static int v80(final BufferedImage character) {
        return sumCn(0.3, 42, 24, character);
    }

    // Creates a line down the center of the image, then copies everything on the
    // left side over to the right side. I'm probably using a really stupid algorithm
    // for this, but I'm tired and can't be bothered to do something better right now.
    private static BufferedImage copyLeftSideToRight(final BufferedImage srcImage) {
        BufferedImage newImage = new BufferedImage(srcImage.getWidth(), srcImage.getHeight(), BufferedImage.TYPE_BYTE_BINARY);
        int symmetryPoint = (srcImage.getWidth() / 2) - 1;
        int xSrc = 0;

        for (int x = 0; x < srcImage.getWidth(); ++x) {
            for (int y = 0; y < srcImage.getHeight(); ++y) {
                int srcImageValue = srcImage.getRGB(xSrc, y);
                newImage.setRGB(x, y, srcImageValue);
            }
            if (x > symmetryPoint) {
                --xSrc;
            } else if (x < symmetryPoint) {
                ++xSrc;
            }
        }

        return newImage;
    }

    private static final int INDEX2D(int x, int y, int w) {
        return y * w + x;
    }

    private static double[] imageToVector(final BufferedImage srcImage) {
        double vector[] = new double[srcImage.getWidth() * srcImage.getHeight()];

        for (int x = 0; x < srcImage.getWidth(); ++x) {
            for (int y = 0; y < srcImage.getHeight(); ++y) {
                vector[INDEX2D(x, y, srcImage.getWidth())] = (double)srcImage.getRGB(x, y);
            }
        }

        return vector;
    }

    public static double hSymmetry(final BufferedImage character) {
        BufferedImage mirroredCharacter = copyLeftSideToRight(character);
        return new PearsonsCorrelation().correlation(imageToVector(character), imageToVector(mirroredCharacter));
    }

    private static BufferedImage copyTopToBottom(final BufferedImage srcImage) {
        BufferedImage newImage = new BufferedImage(srcImage.getWidth(), srcImage.getHeight(), BufferedImage.TYPE_BYTE_BINARY);
        int symmetryPoint = (srcImage.getHeight() / 2) - 1;

        for (int x = 0; x < srcImage.getWidth(); ++x) {
            int ySrc = 0;
            for (int y = 0; y < srcImage.getHeight(); ++y) {
                int srcImageValue = srcImage.getRGB(x, ySrc);
                newImage.setRGB(x, y, srcImageValue);
                if (y > symmetryPoint) {
                    --ySrc;
                } else if (y < symmetryPoint) {
                    ++ySrc;
                }
            }
        }

        return newImage;
    }

    public static double vSymmetry(final BufferedImage character) {
        BufferedImage mirroredCharacter = copyTopToBottom(character);
        return new PearsonsCorrelation().correlation(imageToVector(character), imageToVector(mirroredCharacter));
    }

    public static double whtPos(final BufferedImage character) {
        return 0.0;
    }
}
