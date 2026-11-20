
package Semantic;

public class CosineSimilarity {

    public static double calculate(
            float[] vectorA,
            float[] vectorB) {

        if (vectorA.length != vectorB.length) {
            throw new IllegalArgumentException(
                    "Vectors must have the same size."
            );
        }

        double dotProduct = 0.0;

        for (int i = 0; i < vectorA.length; i++) {

            dotProduct += vectorA[i] * vectorB[i];
        }

        return dotProduct;
    }
}
