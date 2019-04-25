package vn.vano.cms.automl;

// Imports the Google Cloud client library

import com.google.cloud.automl.v1beta1.*;
import com.google.cloud.automl.v1beta1.ClassificationProto.ClassificationType;
import com.google.protobuf.Empty;
import net.sourceforge.argparse4j.ArgumentParsers;
import net.sourceforge.argparse4j.inf.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import vn.vano.cms.bean.DatasetBean;
import vn.vano.cms.config.AutoMLConfig;

import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Google Cloud AutoML Vision API sample application. Example usage: mvn package exec:java
 * -Dexec.mainClass ='com.google.cloud.vision.samples.automl.DatasetAPI' -Dexec.args='create_dataset
 * test_dataset'
 */
public class DatasetApi {

    // [START automl_vision_create_dataset]
    /**
     * Demonstrates using the AutoML client to create a dataset
     *
     * @param projectId the Google Cloud Project ID.
     * @param computeRegion the Region name. (e.g., "us-central1")
     * @param datasetName the name of the dataset to be created.
     * @param multiLabel the type of classification problem. Set to FALSE by default. False -
     * MULTICLASS , True - MULTILABEL
     * @throws IOException on Input/Output errors.
     */

    private static final Logger logger = LoggerFactory.getLogger(DatasetApi.class);

    public static void createDataset(
            String projectId, String computeRegion, String datasetName, Boolean multiLabel)
            throws IOException {
        logger.info("BEGIN::createDataset");
        try {
            // Instantiates a client
            AutoMlClient client = AutoMlClient.create();

            // A resource that represents Google Cloud Platform location.
            LocationName projectLocation = LocationName.of(projectId, computeRegion);

            // Classification type assigned based on multiLabel value.
            ClassificationType classificationType =
                    multiLabel ? ClassificationType.MULTILABEL : ClassificationType.MULTICLASS;

            // Specify the image classification type for the dataset.
            ImageClassificationDatasetMetadata imageClassificationDatasetMetadata =
                    ImageClassificationDatasetMetadata.newBuilder()
                            .setClassificationType(classificationType)
                            .build();

            // Set dataset with dataset name and set the dataset metadata.
            Dataset myDataset =
                    Dataset.newBuilder()
                            .setDisplayName(datasetName)
                            .setImageClassificationDatasetMetadata(imageClassificationDatasetMetadata)
                            .build();

            // Create dataset with the dataset metadata in the region.
            Dataset dataset = client.createDataset(projectLocation, myDataset);
        } catch (Exception ex) {
            logger.error("", ex);
        }
        logger.info("END::createDataset");
    }
    // [END automl_vision_create_dataset]

    // [START automl_vision_list_datasets]

    /**
     * Demonstrates using the AutoML client to list all datasets.
     *
     * @param projectId     the Id of the project.
     * @param computeRegion the Region name.
     * @param filter        the Filter expression.
     * @throws IOException on Input/Output errors.
     */
    public List<DatasetBean> listDatasets(String projectId, String computeRegion, String filter) {
        List<DatasetBean> listDataset = new ArrayList<>();
        try {
            // Instantiates a client
            AutoMlClient client = AutoMlClient.create();

            // A resource that represents Google Cloud Platform location.
            LocationName projectLocation = LocationName.of(projectId, computeRegion);

            // Build the List datasets request
            ListDatasetsRequest request =
                    ListDatasetsRequest.newBuilder()
                            .setParent(projectLocation.toString())
                            .setFilter(filter)
                            .build();

            // List all the datasets available in the region by applying the filter.
            //System.out.print("List of datasets:");
            for (Dataset dataset : client.listDatasets(request).iterateAll()) {
                DatasetBean datasetBean = new DatasetBean();
                datasetBean.setDatasetName(dataset.getDisplayName());
                datasetBean.setDatasetId(dataset.getName().split("/")[dataset.getName().split("/").length - 1]);
                datasetBean.setQttyImage(dataset.getExampleCount());
                listDataset.add(datasetBean);
            }
        } catch (Exception ex) {
            logger.error("", ex);
        }
        return listDataset;
    }
    // [END automl_vision_list_datasets]

    // [START automl_vision_get_dataset]

    /**
     * Demonstrates using the AutoML client to get a dataset by ID.
     *
     * @param projectId     the Id of the project.
     * @param computeRegion the Region name.
     * @param datasetId     the Id of the dataset.
     * @throws IOException on Input/Output errors.
     */
    public DatasetBean getDataset(String projectId, String computeRegion, String datasetId) {
        logger.info("BEGIN::getDataset");
        DatasetBean datasetBean = null;
        try {
            // Instantiates a client
            AutoMlClient client = AutoMlClient.create();

            // Get the complete path of the dataset.
            DatasetName datasetFullId = DatasetName.of(projectId, computeRegion, datasetId);

            // Get all the information about a given dataset.
            Dataset dataset = client.getDataset(datasetFullId);

            // Display the dataset information.
            datasetBean = new DatasetBean();
            datasetBean.setDatasetName(dataset.getDisplayName());
            datasetBean.setDatasetId(dataset.getName().split("/")[dataset.getName().split("/").length - 1]);
            datasetBean.setQttyImage(dataset.getExampleCount());
        } catch (Exception ex) {
            logger.error("", ex);
        }
        logger.info("END::getDataset");
        return datasetBean;
    }
    // [END automl_vision_get_dataset]

    // [START automl_vision_import_data]

    /**
     * Demonstrates using the AutoML client to import labeled images.
     *
     * @param projectId     the Id of the project.
     * @param computeRegion the Region name.
     * @param datasetId     the Id of the dataset to which the training data will be imported.
     * @param path          the Google Cloud Storage URIs. Target files must be in AutoML vision CSV format.
     * @throws Exception on AutoML Client errors
     */
    public boolean importData(String projectId, String computeRegion, String datasetId, String path) throws Exception {
        logger.info("BEGIN::importData");
        boolean check = false;
        try {
            // Instantiates a client
            AutoMlClient client = AutoMlClient.create();

            // Get the complete path of the dataset.
            DatasetName datasetFullId = DatasetName.of(projectId, computeRegion, datasetId);

            GcsSource.Builder gcsSource = GcsSource.newBuilder();

            // Get multiple training data files to be imported
            String[] inputUris = path.split(",");
            for (String inputUri : inputUris) {
                gcsSource.addInputUris(inputUri);
            }
            InputConfig inputConfig = InputConfig.newBuilder().setGcsSource(gcsSource).build();
            //Processing import
            Empty response = client.importDataAsync(datasetFullId.toString(), inputConfig).get();
            check = true;
        } catch (Exception ex) {
            logger.error("", ex);
            check = false;
        }
        logger.info("BEGIN::importData");
        return check;
    }
    // [END automl_vision_import_data]

    // [START automl_vision_export_data]

    /**
     * Demonstrates using the AutoML client to export a dataset to a Google Cloud Storage bucket.
     *
     * @param projectId     the Id of the project.
     * @param computeRegion the Region name.
     * @param datasetId     the Id of the dataset.
     * @param gcsUri        the Destination URI (Google Cloud Storage)
     * @throws Exception on AutoML Client errors
     */
    public static void exportData(
            String projectId, String computeRegion, String datasetId, String gcsUri) throws Exception {
        // Instantiates a client
        AutoMlClient client = AutoMlClient.create();

        // Get the complete path of the dataset.
        DatasetName datasetFullId = DatasetName.of(projectId, computeRegion, datasetId);

        // Set the output URI
        GcsDestination gcsDestination = GcsDestination.newBuilder().setOutputUriPrefix(gcsUri).build();

        // Export the dataset to the output URI.
        OutputConfig outputConfig = OutputConfig.newBuilder().setGcsDestination(gcsDestination).build();
        System.out.println("Processing export...");

        Empty response = client.exportDataAsync(datasetFullId, outputConfig).get();
        System.out.println(String.format("Dataset exported. %s", response));
    }
    // [END automl_vision_export_data]

    // [START automl_vision_delete_dataset]

    /**
     * Delete a dataset.
     *
     * @param projectId     the Id of the project.
     * @param computeRegion the Region name.
     * @param datasetId     the Id of the dataset.
     * @throws Exception on AutoML Client errors
     */
    public static void deleteDataset(String projectId, String computeRegion, String datasetId)
            throws Exception {
        // Instantiates a client
        AutoMlClient client = AutoMlClient.create();

        // Get the complete path of the dataset.
        DatasetName datasetFullId = DatasetName.of(projectId, computeRegion, datasetId);

        // Delete a dataset.
        Empty response = client.deleteDatasetAsync(datasetFullId).get();

        System.out.println(String.format("Dataset deleted. %s", response));
    }
    // [END automl_vision_delete_dataset]
}

