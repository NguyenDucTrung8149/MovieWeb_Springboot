package EVC.Movie.services;

import org.springframework.stereotype.Service;
import weka.classifiers.Classifier;
import weka.classifiers.functions.LinearRegression;
import weka.classifiers.trees.J48;
import weka.classifiers.trees.RandomForest;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;

import java.io.File;

@Service
public class WekaModelService {
    private Classifier classifier;

    public void loadModel() throws Exception {
        File modelFile = new File("data/recommendation.model");
        if (modelFile.exists()) {
            classifier = (Classifier) weka.core.SerializationHelper.read(modelFile.getAbsolutePath());
        } else {
            throw new RuntimeException("Model file not found!");
        }
    }

    public void trainModel(String arffFilePath) throws Exception {
        DataSource source = new DataSource(arffFilePath);
        Instances data = source.getDataSet();
        data.setClassIndex(data.numAttributes() - 1);


        RandomForest randomForest = new RandomForest();
        randomForest.setNumIterations(150);// Số cây trong rừng
        randomForest.setMaxDepth(15);// Độ sâu tối đa của mỗi cây
        randomForest.setNumFeatures(2);// Số đặc trưng ngẫu nhiên được chọn khi phân nhánh
        // Ngẫu nhiên hóa dữ liệu khi huấn luyện
        randomForest.setSeed(123); // Đảm bảo tái hiện được kết quả
        randomForest.setBagSizePercent(70);// Sử dụng 70% dữ liệu để huấn luyện từng cây
        randomForest.buildClassifier(data);

        weka.core.SerializationHelper.write("data/recommendation.model", randomForest);
        classifier = randomForest;
    }

    public double predict(Instance instance, Instances dataset) throws Exception {
        if (classifier == null) {
            loadModel();
        }
        double predictedValue = classifier.distributionForInstance(instance)[0];
        System.out.println("Instance: " + instance + " | Predicted class index: " + predictedValue + "|instance value "+instance.value(instance.numAttributes() - 1));
        return predictedValue;
    }
}

