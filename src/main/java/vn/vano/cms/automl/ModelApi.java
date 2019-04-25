package vn.vano.cms.automl;

// Imports the Google Cloud client library

import com.google.api.gax.longrunning.OperationFuture;
import com.google.cloud.automl.v1beta1.*;
import com.google.cloud.automl.v1beta1.ClassificationProto.ClassificationEvaluationMetrics;
import com.google.cloud.automl.v1beta1.ClassificationProto.ClassificationEvaluationMetrics.ConfidenceMetricsEntry;
import com.google.longrunning.Operation;
import com.google.protobuf.Empty;
import net.sourceforge.argparse4j.ArgumentParsers;
import net.sourceforge.argparse4j.inf.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import vn.vano.cms.bean.DatasetBean;
import vn.vano.cms.bean.ModelBean;
import vn.vano.cms.config.AutoMLConfig;

import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Google Cloud AutoML Vision API sample application. Example usage: mvn package exec:java
 * -Dexec.mainClass ='com.google.cloud.vision.samples.automl.ModelApi' -Dexec.args='create_model
 * [datasetId] test_model'
 */
public class ModelApi {

    // [START automl_vision_create_model]
    /**
     * Demonstrates using the AutoML client to create a model.
     *
     * @param projectId the Id of the project.
     * @param computeRegion the Region name.
     * @param dataSetId the Id of the dataset to which model is created.
     * @param modelName the Name of the model.
     * @param trainBudget the Budget for training the model.
     * @throws Exception on AutoML Client errors
     */

    private static final Logger logger = LoggerFactory.getLogger(ModelApi.class);

    public boolean createModel(String projectId,
                                   String computeRegion,
                                   String dataSetId,
                                   String modelName,
                                   String trainBudget) {
        logger.info("BEGIN::createModel");
        boolean checkModel = false;
        try {
            // Instantiates a client
            AutoMlClient client = AutoMlClient.create();

            // A resource that represents Google Cloud Platform location.
            LocationName projectLocation = LocationName.of(projectId, computeRegion);

            // Set model metadata.
            ImageClassificationModelMetadata imageClassificationModelMetadata =
                    Long.valueOf(trainBudget) == 0
                            ? ImageClassificationModelMetadata.newBuilder().build()
                            : ImageClassificationModelMetadata.newBuilder()
                            .setTrainBudget(Long.valueOf(trainBudget))
                            .build();

            // Set model name and model metadata for the image dataset.
            Model myModel =
                    Model.newBuilder()
                            .setDisplayName(modelName)
                            .setDatasetId(dataSetId)
                            .setImageClassificationModelMetadata(imageClassificationModelMetadata)
                            .build();

            // Create a model with the model metadata in the region.
            OperationFuture<Model, OperationMetadata> response =
                    client.createModelAsync(projectLocation, myModel);
            checkModel = true;
        }catch (Exception ex) {
            logger.error("", ex);
            checkModel = false;
        }
        logger.info("END::createModel");
        return checkModel;
    }
    // [END automl_vision_create_model]

    // [START automl_vision_get_operation_status]
    /**
     * Demonstrates using the AutoML client to get operation status.
     *
     * @param operationFullId the complete name of a operation. For example, the name of your
     *     operation is projects/[projectId]/locations/us-central1/operations/[operationId].
     * @throws IOException on Input/Output errors.
     */
    public static void getOperationStatus(String operationFullId) throws IOException {
        AutoMlClient client = AutoMlClient.create();

        // Get the latest state of a long-running operation.
        Operation response = client.getOperationsClient().getOperation(operationFullId);

        System.out.println(String.format("Operation status: %s", response));
    }
    // [END automl_vision_get_operation_status]

    // [START automl_vision_list_models]
    /**
     * Demonstrates using the AutoML client to list all models.
     *
     * @param projectId the Id of the project.
     * @param computeRegion the Region name.
     * @param filter - Filter expression.
     * @throws IOException on Input/Output errors.
     */
    public List<ModelBean> listModels(String projectId, String computeRegion, String filter) {
        logger.info("BEGIN::listModels");
        List<ModelBean> listModel = new ArrayList<>();
        try {
            AutoMlClient client = AutoMlClient.create();

            // A resource that represents Google Cloud Platform location.
            LocationName projectLocation = LocationName.of(projectId, computeRegion);

            // Create list models request
            ListModelsRequest listModelsRequest =
                    ListModelsRequest.newBuilder()
                            .setParent(projectLocation.toString())
                            .setFilter(filter)
                            .build();

            for (Model model : client.listModels(listModelsRequest).iterateAll()) {
                // Display the model information.
                ModelBean modelBean = new ModelBean();
                modelBean.setModelName(model.getDisplayName());
                modelBean.setModelId(model.getName().split("/")[model.getName().split("/").length - 1]);
                DatasetApi datasetApi = new DatasetApi();
                DatasetBean datasetBean = datasetApi.getDataset(AutoMLConfig.PROJECTID, AutoMLConfig.COMPUTEREGION, model.getDatasetId());
                modelBean.setDatasetName(datasetBean.getDatasetName());
                modelBean.setQttyImage(datasetBean.getQttyImage());
                listModel.add(modelBean);
            }
        } catch (Exception ex) {
            logger.error("", ex);
        }
        logger.info("END::listModels");
        return listModel;
    }
    // [END automl_vision_list_models]

    // [START automl_vision_get_model]
    /**
     * Demonstrates using the AutoML client to get model details.
     *
     * @param projectId the Id of the project.
     * @param computeRegion the Region name.
     * @param modelId the Id of the model.
     * @throws IOException on Input/Output errors.
     */
    public static void getModel(String projectId, String computeRegion, String modelId)
            throws IOException {
        AutoMlClient client = AutoMlClient.create();

        // Get the full path of the model.
        ModelName modelFullId = ModelName.of(projectId, computeRegion, modelId);

        // Get complete detail of the model.
        Model model = client.getModel(modelFullId);

        // Display the model information.
        System.out.println(String.format("AuModel name: %s", model.getName()));
        System.out.println(
                String.format(
                        "AuModel id: %s", model.getName().split("/")[model.getName().split("/").length - 1]));
        System.out.println(String.format("AuModel display name: %s", model.getDisplayName()));
        System.out.println("Image classification model metadata:");
        System.out.println(
                "Tranning budget: " + model.getImageClassificationModelMetadata().getTrainBudget());
        System.out.println(
                "Tranning cost:" + model.getImageClassificationModelMetadata().getTrainCost());
        System.out.println(
                String.format(
                        "Stop reason: %s", model.getImageClassificationModelMetadata().getStopReason()));
        System.out.println(
                String.format(
                        "Base model id: %s", model.getImageClassificationModelMetadata().getBaseModelId()));
        System.out.println("AuModel create time:");
        System.out.println(String.format("\tseconds: %s", model.getCreateTime().getSeconds()));
        System.out.println(String.format("\tnanos: %s", model.getCreateTime().getNanos()));
        System.out.println(String.format("AuModel deployment state: %s", model.getDeploymentState()));
    }
    // [END automl_vision_get_model]

    // [START automl_vision_list_model_evaluations]
    /**
     * Demonstrates using the AutoML client to list model evaluations.
     *
     * @param projectId the Id of the project.
     * @param computeRegion the Region name.
     * @param modelId the Id of the model.
     * @param filter the Filter expression.
     * @throws IOException on Input/Output errors.
     */
    public static void listModelEvaluations(
            String projectId, String computeRegion, String modelId, String filter) throws IOException {
        AutoMlClient client = AutoMlClient.create();

        // Get the full path of the model.
        ModelName modelFullId = ModelName.of(projectId, computeRegion, modelId);

        // Create list model evaluations request
        ListModelEvaluationsRequest modelEvaluationsrequest =
                ListModelEvaluationsRequest.newBuilder()
                        .setParent(modelFullId.toString())
                        .setFilter(filter)
                        .build();

        System.out.println("List of model evaluations:");
        // List all the model evaluations in the model by applying filter.
        for (ModelEvaluation element :
                client.listModelEvaluations(modelEvaluationsrequest).iterateAll()) {
            System.out.println(element);
        }
    }
    // [END automl_vision_list_model_evaluations]

    // [START automl_vision_get_model_evaluation]
    /**
     * Demonstrates using the AutoML client to get model evaluations.
     *
     * @param projectId the Id of the project.
     * @param computeRegion the Region name.
     * @param modelId the Id of the model.
     * @param modelEvaluationId the Id of your model evaluation.
     * @throws IOException on Input/Output errors.
     */
    public static void getModelEvaluation(
            String projectId, String computeRegion, String modelId, String modelEvaluationId)
            throws IOException {
        AutoMlClient client = AutoMlClient.create();

        // Get the full path of the model evaluation.
        ModelEvaluationName modelEvaluationFullId =
                ModelEvaluationName.of(projectId, computeRegion, modelId, modelEvaluationId);
        // Perform the AutoML AuModel request to get AuModel Evaluation information
        ModelEvaluation response = client.getModelEvaluation(modelEvaluationFullId);

        System.out.println(response);
    }
    // [END automl_vision_get_model_evaluation]

    // [START automl_vision_display_evaluation]
    /**
     * Demonstrates using the AutoML client to display model evaluation.
     *
     * @param projectId the Id of the project.
     * @param computeRegion the Region name.
     * @param modelId the Id of the model.
     * @param filter the filter expression.
     * @throws IOException on Input/Output errors.
     */
    public static void displayEvaluation(
            String projectId, String computeRegion, String modelId, String filter) throws IOException {
        AutoMlClient client = AutoMlClient.create();

        // Get the full path of the model.
        ModelName modelFullId = ModelName.of(projectId, computeRegion, modelId);

        // List all the model evaluations in the model by applying filter.
        ListModelEvaluationsRequest modelEvaluationsrequest =
                ListModelEvaluationsRequest.newBuilder()
                        .setParent(modelFullId.toString())
                        .setFilter(filter)
                        .build();

        // Iterate through the results.
        String modelEvaluationId = "";
        for (ModelEvaluation element :
                client.listModelEvaluations(modelEvaluationsrequest).iterateAll()) {
            if (element.getAnnotationSpecId() != null) {
                modelEvaluationId = element.getName().split("/")[element.getName().split("/").length - 1];
            }
        }

        // Resource name for the model evaluation.
        ModelEvaluationName modelEvaluationFullId =
                ModelEvaluationName.of(projectId, computeRegion, modelId, modelEvaluationId);

        // Get a model evaluation.
        ModelEvaluation modelEvaluation = client.getModelEvaluation(modelEvaluationFullId);

        ClassificationEvaluationMetrics classMetrics =
                modelEvaluation.getClassificationEvaluationMetrics();
        List<ConfidenceMetricsEntry> confidenceMetricsEntries =
                classMetrics.getConfidenceMetricsEntryList();

        // Showing model score based on threshold of 0.5
        for (ConfidenceMetricsEntry confidenceMetricsEntry : confidenceMetricsEntries) {
            if (confidenceMetricsEntry.getConfidenceThreshold() == 0.5) {
                System.out.println("Precision and recall are based on a score threshold of 0.5");
                System.out.println(
                        String.format("AuModel Precision: %.2f ", confidenceMetricsEntry.getPrecision() * 100)
                                + '%');
                System.out.println(
                        String.format("AuModel Recall: %.2f ", confidenceMetricsEntry.getRecall() * 100) + '%');
                System.out.println(
                        String.format("AuModel F1 score: %.2f ", confidenceMetricsEntry.getF1Score() * 100)
                                + '%');
                System.out.println(
                        String.format(
                                "AuModel Precision@1: %.2f ", confidenceMetricsEntry.getPrecisionAt1() * 100)
                                + '%');
                System.out.println(
                        String.format("AuModel Recall@1: %.2f ", confidenceMetricsEntry.getRecallAt1() * 100)
                                + '%');
                System.out.println(
                        String.format("AuModel F1 score@1: %.2f ", confidenceMetricsEntry.getF1ScoreAt1() * 100)
                                + '%');
            }
        }
    }
    // [END automl_vision_display_evaluation]

    // [START automl_vision_delete_model]
    /**
     * Demonstrates using the AutoML client to delete a model.
     *
     * @param projectId the Id of the project.
     * @param computeRegion the Region name.
     * @param modelId the Id of the model.
     * @throws Exception on AutoML Client errors
     */
    public void deleteModel(String projectId, String computeRegion, String modelId) {
        logger.info("BEGIN::deleteModel");
        try {
            AutoMlClient client = AutoMlClient.create();

            // Get the full path of the model.
            ModelName modelFullId = ModelName.of(projectId, computeRegion, modelId);

            // Delete a model.
            Empty response = client.deleteModelAsync(modelFullId).get();
        } catch (Exception ex) {
            logger.error("", ex);
        }
        logger.info("END::deleteModel");
    }
    // [END automl_vision_delete_model]
}
