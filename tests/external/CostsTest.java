package external;

import org.apache.sysds.conf.CompilerConfig;
import org.apache.sysds.conf.ConfigurationManager;
import org.apache.sysds.resource.CloudInstance;
import org.apache.sysds.resource.CloudUtils;
import org.apache.sysds.resource.ResourceCompiler;
import org.apache.sysds.resource.cost.CostEstimationException;
import org.apache.sysds.resource.cost.CostEstimator;
import org.apache.sysds.resource.enumeration.EnumerationUtils;
import org.apache.sysds.runtime.compress.colgroup.ColGroupUtils;
import org.apache.sysds.runtime.controlprogram.Program;
import org.apache.sysds.utils.Explain;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static external.ResourceTestUtils.DEFAULT_INSTANCE_INFO_TABLE;
import static external.ResourceTestUtils.compileProgramWithNvargs;

public class CostsTest {
    static {
        ConfigurationManager.getCompilerConfig().set(CompilerConfig.ConfigType.RESOURCE_OPTIMIZATION, true);
    }
    private static final String RESOURCE_FOLDER = "tests/resources";
    private static final String SCRIPT_L2SVM = RESOURCE_FOLDER + "/scripts/L2SVM.dml";
    private static final String SCRIPT_Linreg = RESOURCE_FOLDER + "/scripts/Linreg.dml";
    private static final String SCRIPT_PCA = RESOURCE_FOLDER + "/scripts/PCA.dml";
    private static final String SCRIPT_PNMF = RESOURCE_FOLDER + "/scripts/PNMF.dml";
    // All tests in the class will use dataset "M"
    private static final String INPUT_X = RESOURCE_FOLDER + "/data/MediumDenseX.csv";
    private static final String INPUT_Y = RESOURCE_FOLDER + "/data/MediumY.csv";
    private static final String INPUT_X_PCA = RESOURCE_FOLDER + "/data/MediumThinX.csv";
    private static HashMap<String, CloudInstance> allInstances;
    @BeforeClass
    public static void setUpBeforeClass() {
        try {
            allInstances = CloudUtils.loadInstanceInfoTable(DEFAULT_INSTANCE_INFO_TABLE, 0.25, 0.08);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void getPriceForSingle() {
        double time = 874.119;
        CloudInstance driver = allInstances.get("r6g.16xlarge");

        EnumerationUtils.ConfigurationPoint configs =
                new EnumerationUtils.ConfigurationPoint(driver, null, 0);
        double ret = CloudUtils.calculateClusterPrice(configs, 300+time, CloudUtils.CloudProvider.AWS);
        System.out.println("Calculated price: "+ret);
    }


    @Test
    public void getPriceFor() {
        double time = 1188;
        CloudInstance driver = allInstances.get("m6g.4xlarge");
        int numberExecutors = 4;
        CloudInstance executor = allInstances.get("m6g.4xlarge");

        EnumerationUtils.ConfigurationPoint configs =
                new EnumerationUtils.ConfigurationPoint(driver, executor, numberExecutors);
        double ret = CloudUtils.calculateClusterPrice(configs, 300+time, CloudUtils.CloudProvider.AWS);
        System.out.println("Calculated price: "+ret);
    }

    @Test
    public void testSingleNodeL2SVM() throws CostEstimationException, IOException {
        CloudInstance driver = allInstances.get("m7g.12xlarge");

        runTestL2SVM(driver, 0, null);
    }

    @Test
    public void testClusterL2SVM() throws CostEstimationException, IOException {
        CloudInstance driver = allInstances.get("r6g.xlarge");
        CloudInstance executor = allInstances.get("c7gn.4xlarge");

        runTestL2SVM(driver, 6, executor);
    }

    @Test
    public void testSingleNodeLinreg() throws CostEstimationException, IOException {
        CloudInstance driver = allInstances.get("r6g.8xlarge");

        runTestLinreg(driver, 0, null);
    }

    @Test
    public void testClusterLinreg() throws CostEstimationException, IOException {
        CloudInstance driver = allInstances.get("r6g.xlarge");
        CloudInstance executor = allInstances.get("c7g.8xlarge");

        runTestLinreg(driver, 8, executor);
    }

    @Test
    public void testSingleNodePCA() throws CostEstimationException, IOException {
        CloudInstance driver = allInstances.get("m6g.8xlarge");

        runTestPCA(driver, 0, null);
    }

    @Test
    public void testClusterPCA() throws CostEstimationException, IOException {
        CloudInstance driver = allInstances.get("c5.xlarge");
        CloudInstance executor = allInstances.get("c7g.8xlarge");

        runTestPCA(driver, 4, executor);
    }

    @Test
    public void testSingleNodePNMF() throws CostEstimationException, IOException {
        CloudInstance driver = allInstances.get("c5a.24xlarge");

        runTestPNMF(driver, 0, null);
    }

    @Test
    public void testClusterPNMF() throws CostEstimationException, IOException {
        CloudInstance driver = allInstances.get("c5.xlarge");
        CloudInstance executor = allInstances.get("c7g.8xlarge");

        runTestPNMF(driver, 4, executor);
    }

    private static void runTestL2SVM(CloudInstance driver, int numExecutors, CloudInstance executor) throws IOException, CostEstimationException {
        if (numExecutors == 0 || executor == null) {
            ResourceCompiler.setSingleNodeResourceConfigs(driver.getMemory(), driver.getVCPUs());
        } else {
            ResourceCompiler.setSparkClusterResourceConfigs(driver.getMemory(), driver.getVCPUs(), numExecutors, executor.getMemory(), executor.getVCPUs());
        }
        String s3X = "s3://input/X.csv";
        String s3Y = "s3://input/Y.csv";
        HashMap<String, String> args = new HashMap<>();
        args.put("$fileX", INPUT_X);
        args.put("$fileY", INPUT_Y);
        args.put("$fileW", "s3://data/W.csv");
        HashMap<String, String> localInputs = new HashMap<>();
        localInputs.put(INPUT_X, s3X);
        localInputs.put(INPUT_Y, s3Y);

        Program program = ResourceCompiler.compile(SCRIPT_L2SVM, args, localInputs);
        System.out.println(Explain.explain(program));
        double timeEst = CostEstimator.estimateExecutionTime(program, driver, executor);
        System.out.println(timeEst);
    }

    private static void runTestLinreg(CloudInstance driver, int numExecutors, CloudInstance executor) throws IOException, CostEstimationException {
        if (numExecutors == 0 || executor == null) {
            ResourceCompiler.setSingleNodeResourceConfigs(driver.getMemory(), driver.getVCPUs());
        } else {
            ResourceCompiler.setSparkClusterResourceConfigs(driver.getMemory(), driver.getVCPUs(), numExecutors, executor.getMemory(), executor.getVCPUs());
        }
        String s3X = "s3://input/X.csv";
        String s3Y = "s3://input/Y.csv";
        HashMap<String, String> args = new HashMap<>();
        args.put("$fileX", INPUT_X);
        args.put("$fileY", INPUT_Y);
        args.put("$fileW", "s3://data/W.csv");
        HashMap<String, String> localInputs = new HashMap<>();
        localInputs.put(INPUT_X, s3X);
        localInputs.put(INPUT_Y, s3Y);

        Program program = ResourceCompiler.compile(SCRIPT_Linreg, args, localInputs);
        System.out.println(Explain.explain(program));
        double timeEst = CostEstimator.estimateExecutionTime(program, driver, executor);
        System.out.println(timeEst);
    }

    private static void runTestPCA(CloudInstance driver, int numExecutors, CloudInstance executor) throws IOException, CostEstimationException {
        if (numExecutors == 0 || executor == null) {
            ResourceCompiler.setSingleNodeResourceConfigs(driver.getMemory(), driver.getVCPUs());
        } else {
            ResourceCompiler.setSparkClusterResourceConfigs(driver.getMemory(), driver.getVCPUs(), numExecutors, executor.getMemory(), executor.getVCPUs());
        }
        String s3X = "s3://input/X.csv";
        HashMap<String, String> args = new HashMap<>();
        args.put("$fileX", INPUT_X_PCA);
        args.put("$fileXReduced", "s3://data/XReduced.csv");
        args.put("$fileC", "s3://data/C.csv");
        args.put("$fileC2", "s3://data/C2.csv");
        args.put("$fileS2", "s3://data/S2.csv");
        HashMap<String, String> localInputs = new HashMap<>();
        localInputs.put(INPUT_X_PCA, s3X);

        Program program = ResourceCompiler.compile(SCRIPT_PCA, args, localInputs);
        System.out.println(Explain.explain(program));
        double timeEst = CostEstimator.estimateExecutionTime(program, driver, executor);
        System.out.println(timeEst);
    }

    private static void runTestPNMF(CloudInstance driver, int numExecutors, CloudInstance executor) throws IOException, CostEstimationException {
        if (numExecutors == 0 || executor == null) {
            ResourceCompiler.setSingleNodeResourceConfigs(driver.getMemory(), driver.getVCPUs());
        } else {
            ResourceCompiler.setSparkClusterResourceConfigs(driver.getMemory(), driver.getVCPUs(), numExecutors, executor.getMemory(), executor.getVCPUs());
        }
        String s3X = "s3://input/X.csv";
        HashMap<String, String> args = new HashMap<>();
        args.put("$fileX", INPUT_X);
        args.put("$fileW", "s3://data/W.csv");
        args.put("$fileH", "s3://data/H.csv");
        HashMap<String, String> localInputs = new HashMap<>();
        localInputs.put(INPUT_X, s3X);

        Program program = ResourceCompiler.compile(SCRIPT_PNMF, args, localInputs);
        System.out.println(Explain.explain(program));
        double timeEst = CostEstimator.estimateExecutionTime(program, driver, executor);
        System.out.println("Est. time: "+timeEst);
        EnumerationUtils.ConfigurationPoint configs = new EnumerationUtils.ConfigurationPoint(driver, executor, numExecutors);
        double priceEst = CloudUtils.calculateClusterPrice(configs, timeEst, CloudUtils.CloudProvider.AWS);
        System.out.println("Est. price: "+priceEst);
    }
}
