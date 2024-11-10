package external;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.apache.commons.configuration2.PropertiesConfiguration;
import org.apache.sysds.resource.ResourceOptimizer;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.stream.Stream;

public class LCompareFunctionsTest {
    private static final String RESOURCE_FOLDER = "tests/resources";
    private static final String SCRIPT_L2SVM = RESOURCE_FOLDER + "/scripts/L2SVM.dml";
    private static final String SCRIPT_Linreg = RESOURCE_FOLDER + "/scripts/Linreg.dml";
    private static final String SCRIPT_PCA = RESOURCE_FOLDER + "/scripts/PCA.dml";
    private static final String SCRIPT_PNMF = RESOURCE_FOLDER + "/scripts/PNMF.dml";
    // All tests in the class will use dataset "M"
    private static final String INPUT_X = RESOURCE_FOLDER + "/data/LargeDenseX.csv";
    private static final String INPUT_Y = RESOURCE_FOLDER + "/data/LargeY.csv";
    private static final String INPUT_X_PCA = RESOURCE_FOLDER + "/data/LargeThinX.csv";
    private static final String OUTPUT_FOLDER_PREFIX = "comp_opt_func_l/";
    private static final String ENUMERATION = "grid";

    @Test
    public void testL2SVMMinCosts() throws ParseException {
        String currentOutputFolder = OUTPUT_FOLDER_PREFIX + "L2SVM_MinCosts";
        runTestL2SVM("costs", currentOutputFolder);
    }

    @Test
    public void testL2SVMMinPrice() throws ParseException {
        String currentOutputFolder = OUTPUT_FOLDER_PREFIX + "L2SVM_MinPrice";
        runTestL2SVM("price", currentOutputFolder);
    }

    @Test
    public void testL2SVMMinTime() throws ParseException {
        String currentOutputFolder = OUTPUT_FOLDER_PREFIX + "L2SVM_MinTime";
        runTestL2SVM("time", currentOutputFolder);
    }

    @Test
    public void testLinregMinCosts() throws ParseException {
        String currentOutputFolder = OUTPUT_FOLDER_PREFIX + "Linreg_MinCosts";
        runTestLinreg("costs", currentOutputFolder);
    }

    @Test
    public void testLinregMinPrice() throws ParseException {
        String currentOutputFolder = OUTPUT_FOLDER_PREFIX + "Linreg_MinPrice";
        runTestLinreg("price", currentOutputFolder);
    }

    @Test
    public void testLinregMinTime() throws ParseException {
        String currentOutputFolder = OUTPUT_FOLDER_PREFIX + "Linreg_MinTime";
        runTestLinreg("time", currentOutputFolder);
    }

    @Test
    public void testPCAMinCosts() throws ParseException {
        String currentOutputFolder = OUTPUT_FOLDER_PREFIX + "PCA_MinCosts";
        runTestPCA("costs", currentOutputFolder);
    }

    @Test
    public void testPCAMinPrice() throws ParseException {
        String currentOutputFolder = OUTPUT_FOLDER_PREFIX + "PCA_MinPrice";
        runTestPCA("price", currentOutputFolder);
    }

    @Test
    public void testPCAMinTime() throws ParseException {
        String currentOutputFolder = OUTPUT_FOLDER_PREFIX + "PCA_MinTime";
        runTestPCA("time", currentOutputFolder);
    }

    @Test
    public void testPNMFMinCosts() throws ParseException {
        String currentOutputFolder = OUTPUT_FOLDER_PREFIX + "PNMF_MinCosts";
        runTestPNMF("costs", currentOutputFolder);
    }

    @Test
    public void testPNMFMinPrice() throws ParseException {
        String currentOutputFolder = OUTPUT_FOLDER_PREFIX + "PNMF_MinPrice";
        runTestPNMF("price", currentOutputFolder);
    }

    @Test
    public void testPNMFMinTime() throws ParseException {
        String currentOutputFolder = OUTPUT_FOLDER_PREFIX + "PNMF_MinTime";
        runTestPNMF("time", currentOutputFolder);
    }

    // Helpers ---------------------------------------------------------------------------------------------------------
    private static void runTestL2SVM(String optFunction, String outputFolder) throws ParseException {
        String[] nvargs = new String[3];
        String s3X = "s3://input/X.csv";
        nvargs[0] = "fileX="+s3X;
        String s3Y = "s3://input/Y.csv";
        nvargs[1] = "fileY="+s3Y;
        nvargs[2] = "fileW=s3://output/W.csv";
        String[] localInput = new String[2];
        localInput[0] = s3X+"="+INPUT_X;
        localInput[1] = s3Y+"="+INPUT_Y;

        runTest(
                SCRIPT_L2SVM,
                nvargs,
                optFunction,
                outputFolder,
                localInput
        );
    }

    private static void runTestLinreg(String optFunction, String outputFolder) throws ParseException {
        String[] nvargs = new String[] {
                "fileX=s3://input/X.csv",
                "fileY=s3://input/Y.csv",
                "fileW=s3://output/W.csv"
        };
        String[] localInput = new String[] {
                "s3://input/X.csv=" + INPUT_X,
                "s3://input/Y.csv=" + INPUT_Y
        };

        runTest(SCRIPT_Linreg, nvargs, optFunction, outputFolder, localInput);
    }

    private static void runTestPCA(String optFunction, String outputFolder) throws ParseException {
        String[] nvargs = new String[] {
                "fileX=s3://input/X.csv",
                "fileXReduced=s3://output/XReduced.csv",
                "fileC=s3://output/C.csv",
                "fileC2=s3://output/C2.csv",
                "fileS2=s3://output/S2.csv"
        };
        String[] localInput = new String[] {
                "s3://input/X.csv=" + INPUT_X_PCA
        };

        runTest(SCRIPT_PCA, nvargs, optFunction, outputFolder, localInput);
    }

    private static void runTestPNMF(String optFunction, String outputFolder) throws ParseException {
        String[] nvargs = new String[] {
                "fileX=s3://input/X.csv",
                "fileW=s3://output/W.csv",
                "fileH=s3://output/H.csv"
        };
        String[] localInput = new String[] {
                "s3://input/X.csv=" + INPUT_X
        };

        runTest(SCRIPT_PNMF, nvargs, optFunction, outputFolder, localInput);
    }

    private static void runTest(
            String scriptFile,
            String[] nvargs,
            String optimizationFunction,
            String outputFolder,
            String[] localInput
            ) throws ParseException {
        CommandLineParser clParser = new PosixParser();
        String[] init_args = new String[] {"-f" , scriptFile, "-nvargs", };
        String[] args = Stream.concat(Arrays.stream(init_args), Arrays.stream(nvargs)).toArray(String[]::new);
        CommandLine line = clParser.parse(ResourceOptimizer.createOptions(), args);

        StringBuilder local_input_builder = new StringBuilder();
        for (String s : localInput) local_input_builder.append(s).append(",");
        local_input_builder.deleteCharAt(local_input_builder.length() - 1);

        PropertiesConfiguration options = new PropertiesConfiguration();
        options.setProperty("REGION", "us-east-1");
        options.setProperty("INFO_TABLE", "ec2_stats.csv");
        options.setProperty("REGION_TABLE", "systemds/scripts/resource/aws_regional_prices.csv");
        options.setProperty("OUTPUT_FOLDER", outputFolder);
        options.setProperty(
                "LOCAL_INPUTS", local_input_builder.toString());
        options.setProperty("ENUMERATION", ENUMERATION);
        options.setProperty("OPTIMIZATION_FUNCTION", optimizationFunction);
        options.setProperty("COSTS_WEIGHT", "0.002");
        options.setProperty("MAX_TIME", "4000");
        options.setProperty("MAX_PRICE", "4");
        options.setProperty("CPU_QUOTA", "256");
        options.setProperty("MIN_EXECUTORS", "");
        options.setProperty("MAX_EXECUTORS", "20");
        options.setProperty("INSTANCE_FAMILIES", "");
        options.setProperty("INSTANCE_SIZES", "xlarge,2xlarge,3xlarge,4xlarge,6xlarge,8xlarge,9xlarge,12xlarge,16xlarge,18xlarge"); //xlarge,2xlarge,3xlarge,4xlarge,6xlarge,8xlarge,9xlarge
        options.setProperty("STEP_SIZE", "");
        options.setProperty("EXPONENTIAL_BASE", "");
        options.setProperty("USE_LARGEST_ESTIMATE", "");
        options.setProperty("USE_CP_ESTIMATES", "");
        options.setProperty("USE_BROADCASTS", "");
        options.setProperty("USE_OUTPUTS", "");

        try {
            ResourceOptimizer.execute(line, options);
        } catch (IOException e) {
            Assert.fail(e.getMessage());
        }
    }
}
