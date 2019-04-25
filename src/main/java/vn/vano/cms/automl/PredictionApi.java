package vn.vano.cms.automl;

// Imports the Google Cloud client library

import com.google.cloud.automl.v1beta1.*;
import com.google.protobuf.ByteString;
import net.sourceforge.argparse4j.ArgumentParsers;
import net.sourceforge.argparse4j.inf.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import vn.vano.cms.bean.PredictBean;
import vn.vano.cms.config.AutoMLConfig;

import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

/**
 * Google Cloud AutoML Vision API sample application. Example usage: mvn package exec:java
 * -Dexec.mainClass ='com.google.cloud.vision.samples.automl.PredictionApi' -Dexec.args='predict
 * [modelId] [path-to-image] [scoreThreshold]'
 */
public class PredictionApi {

    // [START automl_vision_predict]

    /**
     * Demonstrates using the AutoML client to predict an image.
     *
     * @param projectId      the Id of the project.
     * @param computeRegion  the Region name.
     * @param modelId        the Id of the model which will be used for text classification.
     * @param filePath       the Local text file path of the content to be classified.
     * @param scoreThreshold the Confidence score. Only classifications with confidence score above
     *                       scoreThreshold are displayed.
     * @throws IOException on Input/Output errors.
     */

    private static final Logger logger = LoggerFactory.getLogger(PredictionApi.class);

    public PredictBean predict(
            String projectId,
            String computeRegion,
            String modelId,
            String filePath,
            String scoreThreshold)
            throws IOException {
        logger.info("BEGIN::predict");
        PredictBean predictBean = null;
        try {
            // Instantiate client for prediction service.
            PredictionServiceClient predictionClient = PredictionServiceClient.create();

            // Get the full path of the model.
            ModelName name = ModelName.of(projectId, computeRegion, modelId);

            // Read the image and assign to payload.
            ByteString content = ByteString.copyFrom(Files.readAllBytes(Paths.get(filePath)));
            Image image = Image.newBuilder().setImageBytes(content).build();
            ExamplePayload examplePayload = ExamplePayload.newBuilder().setImage(image).build();

            // Additional parameters that can be provided for prediction e.g. Score Threshold
            Map<String, String> params = new HashMap<>();
            if (scoreThreshold != null) {
                params.put("score_threshold", scoreThreshold);
            }
            // Perform the AutoML Prediction request
            PredictResponse response = predictionClient.predict(name, examplePayload, params);
            for (AnnotationPayload annotationPayload : response.getPayloadList()) {
                predictBean = new PredictBean();
                predictBean.setLabel(annotationPayload.getDisplayName());
                predictBean.setScore(annotationPayload.getClassification().getScore());
            }
        } catch (Exception ex) {
            logger.error("", ex);
        }
        logger.info("END::predict");
        return predictBean;
    }
}

