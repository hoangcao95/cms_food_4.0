package vn.vano.cms.web.controller;

import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import vn.vano.cms.automl.DatasetApi;
import vn.vano.cms.automl.ModelApi;
import vn.vano.cms.automl.PredictionApi;
import vn.vano.cms.bean.DatasetBean;
import vn.vano.cms.bean.LabelBean;
import vn.vano.cms.bean.ModelBean;
import vn.vano.cms.bean.PredictBean;
import vn.vano.cms.common.Common;
import vn.vano.cms.common.Constants;
import vn.vano.cms.common.MessageContants;
import vn.vano.cms.config.AutoMLConfig;
import vn.vano.cms.jpa.AuModel;
import vn.vano.cms.jpa.Dataset;
import vn.vano.cms.jpa.ImageImport;
import vn.vano.cms.jpa.Label;
import vn.vano.cms.service.CommonService;

import java.awt.image.BufferedImage;
import java.io.*;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.imageio.ImageIO;
import javax.validation.Valid;

@Controller
@RequestMapping(value = "/automl")
public class AutoMLController {
    private static final Logger logger = LoggerFactory.getLogger(AutoMLController.class);

    @Autowired
    private CommonService commonService;

    private PredictionApi predictionApi;

    private DatasetApi datasetApi;

    private ModelApi modelApi;

    public AutoMLController() {
        this.predictionApi = new PredictionApi();
        this.datasetApi = new DatasetApi();
        this.modelApi = new ModelApi();
    }


    //    public AutoMLController() {
//        predictionApi = new PredictionApi();
//        datasetApi = new DatasetApi();
//
//    }

    @RequestMapping(value = "/dataset_list.html", method = RequestMethod.GET)
    public String listDataset(Model model, Pageable pageable) {
        logger.info("BEGIN::listDataset");
        Page<Dataset> page = null;
        try {
            List<Dataset> listDataset = getListDataset();
            Pageable pageRequest = new PageRequest(pageable.getPageNumber(), pageable.getPageSize());
            page = new PageImpl<>(listDataset, pageRequest, listDataset.size());

            model.addAttribute("page", page);
        } catch (Exception ex) {
            logger.error("", ex);
        } finally {

        }
        logger.info("END::listDataset");
        return "automl/dataset_list";
    }

    @RequestMapping(value = "/dataset_add.html", method = RequestMethod.GET)
    public String showFormAddDataset(Model model) {
        logger.info("BEGIN::showFormAddDataset");
        try {
            DatasetBean datasetBean = new DatasetBean();
            model.addAttribute("dataset", datasetBean);
        } catch (Exception ex) {
            logger.error("", ex);
        }
        logger.info("END::showFormAddDataset");
        return "automl/dataset_add";
    }

    //Create Dataset
    @RequestMapping(value = "/dataset_add.html", method = RequestMethod.POST)
    public String addDataset(@ModelAttribute(value = "dataset") @Valid DatasetBean datasetBean) {
        logger.info("BEGIN::addDataset");
        int result = 0;
        try {
            datasetApi.createDataset(AutoMLConfig.PROJECTID, AutoMLConfig.COMPUTEREGION, datasetBean.getDatasetName(), false);
            List<DatasetBean> listDatasetBean = datasetApi.listDatasets(AutoMLConfig.PROJECTID, AutoMLConfig.COMPUTEREGION, "");
            Dataset dataset = new Dataset();
            for (DatasetBean bean : listDatasetBean) {
                if (datasetBean.getDatasetName().equals(bean.getDatasetName())) {
                    BeanUtils.copyProperties(bean, dataset);
                    dataset.setCreatedDate(new Timestamp(Calendar.getInstance().getTime().getTime()));
                    dataset.setStatus(Integer.parseInt(AutoMLConfig.Status.ACTIVE));
                    result = commonService.create(null, dataset);
                }
            }
        } catch (Exception ex) {
            logger.error("", ex);
        }
        logger.info("END::addDataset");
        return "redirect:/automl/dataset_list.html";
    }

    @RequestMapping(value = "/import_data.html", method = RequestMethod.GET)
    public String showFormImportData(Model model) {
        logger.info("BEGIN::showFormImportData");
        try {
            List<Label> lstLabel = getListLabel();
            model.addAttribute("image", new ImageImport());
            model.addAttribute("lstLabel", lstLabel);
        } catch (Exception ex) {
            logger.error("", ex);
        }
        logger.info("END::showFormImportData");
        return "automl/import_data";
    }

    //Import image to dataset
    @RequestMapping(value = "/import_data.html", method = RequestMethod.POST)
    public String importData(@RequestParam(value = "image") MultipartFile image,
                             @RequestParam(value = "label") String labelId,
                             Model model) {
        logger.info("BEGIN::importData");
        try {
            Date today = new Date();
            String _today = new SimpleDateFormat("yyyy_MM_dd hh:mm:ss").format(today);

            //insert ảnh vào bảng image_import
            ImageImport imageBean = new ImageImport();
            imageBean.setImageName(image.getOriginalFilename());
            imageBean.setLabelId(labelId);
            imageBean.setPath(AutoMLConfig.Image.URL_IMAGE_UPLOAD_CLOUD + image.getOriginalFilename());
            imageBean.setCreatedDate(new Timestamp(Calendar.getInstance().getTime().getTime()));
            imageBean.setStatus(1);
            commonService.create(null, imageBean);

            List<ImageImport> lstImageImports = getListImageImport(image.getOriginalFilename());

            //ghi file csv
            FileWriter fileWriter = null;

            String fileNameCsv = AutoMLConfig.CSV.URL_FILE_CSV_LOCAL + image.getOriginalFilename() + ".csv";
            try {
                fileWriter = new FileWriter(fileNameCsv);
                // Write a new Country object list to the CSV file
                for (ImageImport img : lstImageImports) {
                    fileWriter.append(String.valueOf(img.getPath()));
                    fileWriter.append(AutoMLConfig.CSV.COMMA_DELIMITER);
                    fileWriter.append(img.getLabelId());
                    fileWriter.append(AutoMLConfig.CSV.NEW_LINE_SEPARATOR);
                }

                System.out.println("CSV file was created successfully !!!");

            } catch (Exception e) {
                System.out.println("Error in CsvFileWriter !!!");
                e.printStackTrace();
            } finally {
                try {
                    fileWriter.flush();
                    fileWriter.close();
                } catch (Exception ex) {
                    System.out.println("Error while flushing/closing fileWriter !!!");
                    logger.error("", ex);
                }
            }

            //upload ảnh lên storage cloud
            Process pImg = Runtime.getRuntime().exec("cmd /c cd C:\\Users\\hoangcao\\AppData\\Local\\Google\\Cloud SDK\\ & gsutil cp " + AutoMLConfig.Image.URL_IMAGE_LOCAL + image.getOriginalFilename() + " " + AutoMLConfig.Image.URL_IMAGE_UPLOAD_CLOUD);

            BufferedReader stdInputImg = new BufferedReader(new
                    InputStreamReader(pImg.getInputStream()));

            BufferedReader stdErrorImg = new BufferedReader(new
                    InputStreamReader(pImg.getErrorStream()));

            //upload file csv lên storage cloud
            Process pCsv = Runtime.getRuntime().exec("cmd /c cd C:\\Users\\hoangcao\\AppData\\Local\\Google\\Cloud SDK\\ & gsutil cp " + AutoMLConfig.CSV.URL_FILE_CSV_LOCAL + image.getOriginalFilename() + ".csv" + " " + AutoMLConfig.CSV.URL_FILE_CSV_CLOUD);

            BufferedReader stdInputCsv = new BufferedReader(new
                    InputStreamReader(pCsv.getInputStream()));

            //Operation completed over
            String line = null;
            while ((line = stdErrorImg.readLine()) != null && !line.startsWith("Operation completed over")) {
                System.out.println("XX==" + line);
            }

            BufferedReader stdErrorCsv = new BufferedReader(new
                    InputStreamReader(pCsv.getErrorStream()));
            while ((line = stdErrorCsv.readLine()) != null && !line.startsWith("Operation completed over")) {
                System.out.println("YY==" + line);
            }

            //gọi API import data
            boolean check = datasetApi.importData(AutoMLConfig.PROJECTID, AutoMLConfig.COMPUTEREGION, AutoMLConfig.DATASET_ID, AutoMLConfig.CSV.URL_FILE_CSV_CLOUD + image.getOriginalFilename() + ".csv");

            //update số lượng ảnh mới thêm vào db
            if (check) {
                //lấy dữ liệu từ trong db đổ vào đối tượng vừa khởi tạo
                List<Dataset> lstDataset = getListDataset();
                Dataset dataset = null;
                if (!lstDataset.isEmpty()) {
                    dataset = new Dataset();
                    dataset.setId(lstDataset.get(0).getId());
                    dataset.setDatasetName(lstDataset.get(0).getDatasetName());
                    dataset.setCreatedDate(lstDataset.get(0).getCreatedDate());
                    dataset.setStatus(lstDataset.get(0).getStatus());
                    dataset.setModifyDate(new Timestamp(Calendar.getInstance().getTime().getTime()));
                }
                //call list danh sách lấy từ API
                List<DatasetBean> lstDatasetBeans = datasetApi.listDatasets(AutoMLConfig.PROJECTID, AutoMLConfig.COMPUTEREGION, "");
                //update số lượng ảnh từ trên API vào db
                for (DatasetBean bean : lstDatasetBeans) {
                    if (dataset.getDatasetName().equals(bean.getDatasetName())) {
                        BeanUtils.copyProperties(bean, dataset);
                        commonService.update(null, dataset, null, null, false, true, null);
                    }
                }
                model.addAttribute(MessageContants.Result.MESSAGE_CODE, "Thêm ảnh và gán nhãn ảnh thành công");
            } else {
                model.addAttribute(MessageContants.Result.MESSAGE_CODE, "Thêm ảnh và gán nhãn ảnh thất bại");
            }
        } catch (Exception ex) {
            logger.error("", ex);
            model.addAttribute(MessageContants.Result.MESSAGE_CODE, "Thêm ảnh và gán nhãn ảnh thất bại");
        } finally {
            model.addAttribute("lstLabel", getListLabel());
        }
        logger.info("END::importData");
        return "automl/import_data";
    }

    @RequestMapping(value = "label_list.html", method = RequestMethod.GET)
    public String listLabel(Model model,
                            Pageable pageable) {
        logger.info("BEGIN::listLabel");
        try {
            Pageable _pageable = new PageRequest(pageable.getPageNumber(), pageable.getPageSize(), null);
            List<Label> lstLabel = getListLabel();
            Page<Label> pageLabel = new PageImpl<>(lstLabel, _pageable, lstLabel.size());
            model.addAttribute("page", pageLabel);
        } catch (Exception ex) {
            logger.error("", ex);
        }
        logger.info("END::listLabel");
        return "automl/label_list";
    }

    @RequestMapping(value = "label_add.html", method = RequestMethod.GET)
    public String showFormAddLabel(Model model) {
        logger.info("BEGIN::showFormAddLabel");
        try {
            model.addAttribute("labelObj", new LabelBean());
        } catch (Exception ex) {
            logger.error("", ex);
        }
        logger.info("END::showFormAddLabel");
        return "automl/label_add";
    }

    //Add label
    @RequestMapping(value = "label_add.html", method = RequestMethod.POST)
    public String labelAdd(@ModelAttribute(value = "labelObj") LabelBean labelBean,
                           Model model) {
        logger.info("BEGIN::labelAdd");
        int result = 0;
        try {
            Label label = new Label();
            BeanUtils.copyProperties(labelBean, label);
            label.setLabelName(label.getLabelName());
            label.setLabelId(label.getLabelId());
            label.setCreatedDate(new Date());
            label.setStatus(Integer.parseInt(Constants.Label.STATUS_ACTIVE));
            result = commonService.create(null, label);
        } catch (Exception ex) {
            logger.error("", ex);
        } finally {
            if (result > 0) {
                model.addAttribute("labelObj", new LabelBean());
                model.addAttribute(MessageContants.Result.MESSAGE_CODE, MessageContants.AutoML.ADDNEW_SUCCESS);
            } else {
                model.addAttribute(MessageContants.Result.MESSAGE_CODE, MessageContants.AutoML.ADDNEW_ERROR);
            }
        }
        logger.info("END::labelAdd");
        return "automl/label_add";
    }

    //List AuModel
    @RequestMapping(value = "/model_list.html", method = RequestMethod.GET)
    public String modelList(Model model, Pageable pageable) {
        logger.info("BEGIN::modelList");
        Page page = null;
        try {
            List<AuModel> listModel = getListModel(AutoMLConfig.DATASET_NAME);
            Pageable pageRequest = new PageRequest(pageable.getPageNumber(), pageable.getPageSize());
            page = new PageImpl<>(listModel, pageRequest, listModel.size());
            model.addAttribute("page", page);
        } catch (Exception ex) {
            logger.error("", ex);
        }
        logger.info("END::modelList");
        return "automl/model_list";
    }

    @RequestMapping(value = "/model_add.html", method = RequestMethod.GET)
    public String showFormAddModel(Model model) {
        logger.info("BEGIN::showFormAddModel");
        try {
            ModelBean modelBean = new ModelBean();
            model.addAttribute("model", modelBean);
        } catch (Exception ex) {
            logger.error("", ex);
        }
        logger.info("END::showFormAddModel");
        return "automl/model_add";
    }

    //Thêm mới Model và tranning Dataset
    @RequestMapping(value = "/model_add.html", method = RequestMethod.POST)
    public String tranning(@ModelAttribute(value = "model") @Valid ModelBean modelBean,
                           Model model) {
        logger.info("BEGIN::tranning");
        try {
            List<AuModel> lstAuModel = getListModelByModelName(modelBean.getModelName());
            if (!lstAuModel.isEmpty() && lstAuModel != null) {
                model.addAttribute(MessageContants.Result.MESSAGE_CODE, "Tên Model đã tồn tại");
                return "automl/model_add";
            } else {
                model.addAttribute(MessageContants.Result.MESSAGE_CODE, "Đang trainning xin vui lòng đợi");
            }
            new Thread() {
                @Override
                public void run() {

                    //gọi API tạo Model và Tranning
                    boolean check = modelApi.createModel(AutoMLConfig.PROJECTID, AutoMLConfig.COMPUTEREGION, AutoMLConfig.DATASET_ID, modelBean.getModelName(), "1");
                    //insert model vào db
                    AuModel autoModel = new AuModel();
                    BeanUtils.copyProperties(modelBean, autoModel);
                    autoModel.setDatasetName(AutoMLConfig.DATASET_NAME);
                    autoModel.setTrainningDate(new Timestamp(Calendar.getInstance().getTime().getTime()));
                    autoModel.setStatus(Integer.parseInt(AutoMLConfig.Status.NOT_ACTIVE));
                    commonService.create(null, autoModel);

                    if (check) {
                        List<ModelBean> listModelBean = modelApi.listModels(AutoMLConfig.PROJECTID, AutoMLConfig.COMPUTEREGION, "");
                        List<ModelBean> listModelBean1 = null;
                        int lstSize = listModelBean.size();
                        boolean flag = true;
                        //chờ Tranning
                        while (flag) {
                            listModelBean1 = modelApi.listModels(AutoMLConfig.PROJECTID, AutoMLConfig.COMPUTEREGION, "");

//                            listModelBean1 = new ArrayList<>();
//                            ModelBean modelBean1 = new ModelBean("doctor_model", "", "", "", 0);
//                            ModelBean modelBean2 = new ModelBean("doctor_model1", "", "", "", 0);
//                            ModelBean modelBean3 = new ModelBean("FOOD_MODEL_01", "abc", "", "FOOD_V01", 200);
//                            ModelBean modelBean4 = new ModelBean("FOOD_MODEL_02", "aaa", "", "FOOD_V01", 200);
//                            listModelBean1.add(modelBean1);
//                            listModelBean1.add(modelBean2);
//                            listModelBean1.add(modelBean3);
//                            listModelBean1.add(modelBean4);

                            if (listModelBean1.size() > lstSize) {
                                flag = false;
                            }
                        }

                        List<AuModel> lstBean = getListModelByModelName(modelBean.getModelName());
                        if (!lstBean.isEmpty()) {
                            autoModel.setId(lstBean.get(0).getId());
                            autoModel.setCompletedDate(new Timestamp(Calendar.getInstance().getTime().getTime()));
                            autoModel.setStatus(Integer.parseInt(AutoMLConfig.Status.ACTIVE));
                        }
                        //update dữ liệu từ trên API google cloud vào db
                        for (ModelBean bean : listModelBean1) {
                            if (modelBean.getModelName().equals(bean.getModelName())) {
                                BeanUtils.copyProperties(bean, autoModel);
                                commonService.update(null, autoModel, null, null, false, true, null);
                            }
                        }

                    }
                }
            }.start();
        } catch (Exception ex) {
            logger.error("", ex);
            model.addAttribute(MessageContants.Result.MESSAGE_CODE, MessageContants.AutoML.TRANNING_ERROR);
            return "automl/model_add";
        }
        logger.info("END::tranning");
        return "automl/model_add";
    }

    //Delete AuModel
    @RequestMapping(value = "/modelDelete.html/{modelId}", method = RequestMethod.GET)
    public String deleteModel(@PathVariable(value = "modelId") String modelId,
                              RedirectAttributes redirectAttributes) {
        logger.info("BEGIN::deleteModel");
        try {
            modelApi.deleteModel(AutoMLConfig.PROJECTID, AutoMLConfig.COMPUTEREGION, modelId);
            redirectAttributes.addFlashAttribute(MessageContants.Result.MESSAGE_CODE, MessageContants.AutoML.DELETE_SUCCESS);
        } catch (Exception ex) {
            logger.error("", ex);
            redirectAttributes.addFlashAttribute(MessageContants.Result.MESSAGE_CODE, MessageContants.AutoML.DELETE_ERROR);
        }
        logger.info("END::deleteModel");
        return "redirect:/automl/model_list.html";
    }

    @RequestMapping(value = "/predict.html", method = RequestMethod.GET)
    public String showFormUPredict(Model model) {
        logger.info("BEGIN::showFormUPredict");
        try {
            List<AuModel> listAuModel = getListModelCompletedTrainning();
            model.addAttribute("listModel", listAuModel);
        } catch (Exception ex) {
            logger.error("", ex);
        }
        logger.info("END::showFormUPredict");
        return "automl/predict_upload";
    }

    @RequestMapping(value = "/predict.html", method = RequestMethod.POST)
    public String predictResult(Model model,
                                @RequestParam(value = "file") MultipartFile file,
                                @RequestParam(value = "model") String modelId) {
        try {
            //lưu ảnh
            Date today = new Date();
            String _today = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss").format(today);

            String fileName = _today + "_" + file.getOriginalFilename();

            //tạo file ảnh trong đường dẫn muốn lưu
            File path = new File(AutoMLConfig.Image.URL_IMAGE_LOCAL + fileName);

            //đối tượng để chứa ảnh
            BufferedImage img = ImageIO.read(new ByteArrayInputStream(file.getBytes()));
            //ghi ảnh vào file vừa tạo
            ImageIO.write(img, "jpg", path);

            //gọi API nhận diện ảnh
            PredictBean predictBean = predictionApi.predict(AutoMLConfig.PROJECTID, AutoMLConfig.COMPUTEREGION, modelId, path.getPath(), AutoMLConfig.SCORETHRESHOLD);
            if (predictBean != null) {
                List<Label> lstLabel = getListLabel();
                for (Label label: lstLabel) {
                    if (predictBean.getLabel().equalsIgnoreCase(label.getLabelId())) {
                        predictBean.setLabel(label.getLabelName());
                    }
                }
            } else {
                predictBean = new PredictBean();
                predictBean.setLabel("Khác");
                predictBean.setScore(0.0f);
            }
            model.addAttribute("predict", predictBean);
        } catch (Exception ex) {
            logger.error("", ex);
        }
        return "automl/predict_result";
    }

    private List<Label> getListLabel() {
        logger.info("BEGIN::getListLabel");
        List<Label> lstLabel = new ArrayList<>();
        try {
            List<Object[]> lstLabelObj = commonService.findBySQL(null, "SELECT * FROM image_label WHERE status = 1 ",
                    null);
            if (lstLabelObj != null && !lstLabelObj.isEmpty()) {
                lstLabelObj.stream().forEach(item -> {
                    Label label = new Label();
                    label = Common.convertToBean(item, label);
                    lstLabel.add(label);
                });
            }
        } catch (Exception ex) {
            logger.error("", ex);
        }
        logger.info("END::getListLabel");
        return lstLabel;
    }

    private List<ImageImport> getListImageImport(String imageName) {
        logger.info("BEGIN::getListLabel");
        List<ImageImport> lstImageImport = new ArrayList<>();
        try {
            List<Object[]> lstImageImportObj = commonService.findBySQL(null, "SELECT * FROM image_import WHERE status = 1 AND (image_name = ? OR ? = '') ",
                    new String[]{imageName, imageName});
            if (lstImageImportObj != null && !lstImageImportObj.isEmpty()) {
                lstImageImportObj.stream().forEach(item -> {
                    ImageImport imageBean = new ImageImport();
                    imageBean = Common.convertToBean(item, imageBean);
                    lstImageImport.add(imageBean);
                });
            }
        } catch (Exception ex) {
            logger.error("", ex);
        }
        logger.info("END::getListLabel");
        return lstImageImport;
    }

    private List<AuModel> getListModel(String datasetName) {
        logger.info("BEGIN::getListModel");
        List<AuModel> lstAuModel = new ArrayList<>();
        try {
            List<Object[]> lstAuModelObj = commonService.findBySQL(null, "SELECT * FROM automl_model WHERE dataset_name = ? OR ? = '' ",
                    new String[]{datasetName, datasetName});
            if (lstAuModelObj != null && !lstAuModelObj.isEmpty()) {
                lstAuModelObj.stream().forEach(item -> {
                    AuModel modelBean = new AuModel();
                    modelBean = Common.convertToBean(item, modelBean);
                    lstAuModel.add(modelBean);
                });
            }
        } catch (Exception ex) {
            logger.error("", ex);
        }
        logger.info("END::getListModel");
        return lstAuModel;
    }

    private List<AuModel> getListModelByModelName(String modelName) {
        logger.info("BEGIN::getListModel");
        List<AuModel> lstAuModel = new ArrayList<>();
        try {
            List<Object[]> lstAuModelObj = commonService.findBySQL(null, "SELECT * FROM automl_model WHERE model_name = ? OR ? = '' ",
                    new String[]{modelName, modelName});
            if (lstAuModelObj != null && !lstAuModelObj.isEmpty()) {
                lstAuModelObj.stream().forEach(item -> {
                    AuModel modelBean = new AuModel();
                    modelBean = Common.convertToBean(item, modelBean);
                    lstAuModel.add(modelBean);
                });
            }
        } catch (Exception ex) {
            logger.error("", ex);
        }
        logger.info("END::getListModel");
        return lstAuModel;
    }

    private List<AuModel> getListModelCompletedTrainning() {
        logger.info("BEGIN::getListModel");
        List<AuModel> lstAuModel = new ArrayList<>();
        try {
            List<Object[]> lstAuModelObj = commonService.findBySQL(null, "SELECT * FROM automl_model WHERE dataset_name = 'FOOD_DATASET_AI' AND status = 1 ORDER BY id DESC ",
                    null);
            if (lstAuModelObj != null && !lstAuModelObj.isEmpty()) {
                lstAuModelObj.stream().forEach(item -> {
                    AuModel modelBean = new AuModel();
                    modelBean = Common.convertToBean(item, modelBean);
                    lstAuModel.add(modelBean);
                });
            }
        } catch (Exception ex) {
            logger.error("", ex);
        }
        logger.info("END::getListModel");
        return lstAuModel;
    }

    private List<Dataset> getListDataset() {
        logger.info("BEGIN::getListModel");
        List<Dataset> lstDataset = new ArrayList<>();
        try {
            List<Object[]> lstDatasetObj = commonService.findBySQL(null, "SELECT * FROM automl_dataset WHERE dataset_name = 'FOOD_DATASET_AI' AND status = 1 ",
                    null);
            if (lstDatasetObj != null && !lstDatasetObj.isEmpty()) {
                lstDatasetObj.stream().forEach(item -> {
                    Dataset datasetBean = new Dataset();
                    datasetBean = Common.convertToBean(item, datasetBean);
                    lstDataset.add(datasetBean);
                });
            }
        } catch (Exception ex) {
            logger.error("", ex);
        }
        logger.info("END::getListModel");
        return lstDataset;
    }

}
