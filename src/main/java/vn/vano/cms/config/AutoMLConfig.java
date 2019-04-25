package vn.vano.cms.config;

public interface AutoMLConfig {
    public static String PROJECTID = "auto-ml-vision-233107";
    public static String COMPUTEREGION = "us-central1";
    public static String SCORETHRESHOLD = "0.7";
    public static String DATASET_NAME = "FOOD_DATASET_AI";
    public static String DATASET_ID = "ICN5650877814654588460";

    public interface Status {
        String ACTIVE = "1";
        String NOT_ACTIVE = "0";
    }

    public interface Image {
        String URL_IMAGE_LOCAL = "C:\\vano\\vano_project\\food_cms\\src\\main\\webapp\\WEB-INF\\resources\\";
        String URL_IMAGE_UPLOAD_CLOUD = "gs://auto-ml-vision-233107-vcm/FOOD_DATASET_AI/upload/";
    }

    public interface CSV {
        // Delimiter used in CSV file
       String COMMA_DELIMITER = ",";
       String NEW_LINE_SEPARATOR = "\n";
       String URL_FILE_CSV_LOCAL = "C:\\vano\\vano_project\\food_cms\\src\\main\\webapp\\WEB-INF\\csv\\";
       String URL_FILE_CSV_CLOUD = "gs://auto-ml-vision-233107-vcm/FOOD_DATASET_AI/csv/";
    }
}
