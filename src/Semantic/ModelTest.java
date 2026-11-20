
package Semantic;

import ai.onnxruntime.OrtEnvironment;
import ai.onnxruntime.OrtSession;

public class ModelTest {

    public static void main(String[] args) throws Exception {

        String modelPath =
                "models/all-MiniLM-L6-v2/model.onnx";

        // الحصول على ONNX Environment
        OrtEnvironment environment =
                OrtEnvironment.getEnvironment();

        // إعدادات النموذج
        OrtSession.SessionOptions options =
                new OrtSession.SessionOptions();

        // تحميل النموذج
        try (OrtSession session =
                     environment.createSession(modelPath, options)) {

            System.out.println("Model loaded successfully!");

            // عرض أسماء المدخلات
            System.out.println("\nModel Inputs:");
            for (String input : session.getInputNames()) {
                System.out.println(input);
            }

            // عرض أسماء المخرجات
            System.out.println("\nModel Outputs:");
            for (String output : session.getOutputNames()) {
                System.out.println(output);
            }
        }

        options.close();
    }
}
