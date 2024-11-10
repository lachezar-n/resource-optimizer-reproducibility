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

public class SCompareEnumerationTest {
    private static final String RESOURCE_FOLDER = "tests/resources";
    private static final String SCRIPT_L2SVM = RESOURCE_FOLDER + "/scripts/L2SVM.dml";
    private static final String SCRIPT_Linreg = RESOURCE_FOLDER + "/scripts/Linreg.dml";
    private static final String SCRIPT_PCA = RESOURCE_FOLDER + "/scripts/PCA.dml";
    private static final String SCRIPT_PNMF = RESOURCE_FOLDER + "/scripts/PNMF.dml";
    // All tests in the class will use dataset "M"
    private static final String INPUT_X = RESOURCE_FOLDER + "/data/SmallDenseX.csv";
    private static final String INPUT_Y = RESOURCE_FOLDER + "/data/SmallY.csv";
    private static final String INPUT_X_PCA = RESOURCE_FOLDER + "/data/SmallThinX.csv";
    private static final String OUTPUT_FOLDER_PREFIX = "comp_enum_s/";
    private static final String OPTIMIZED_FOR = "costs";

    @Test
    public void testL2SVMGrid() throws ParseException {
        String currentOutputFolder = OUTPUT_FOLDER_PREFIX + "L2SVM_Grid";
        runTestL2SVM("grid", currentOutputFolder);
    }

    @Test
    public void testL2SVMPrune() throws ParseException {
        String currentOutputFolder = OUTPUT_FOLDER_PREFIX + "L2SVM_Prune";
        runTestL2SVM("prune", currentOutputFolder);
    }

    @Test
    public void testL2SVMMemory() throws ParseException {
        String currentOutputFolder = OUTPUT_FOLDER_PREFIX + "L2SVM_Memory";
        runTestL2SVM("interest", currentOutputFolder);
    }

    @Test
    public void testLinregGrid() throws ParseException {
        String currentOutputFolder = OUTPUT_FOLDER_PREFIX + "Linreg_Grid";
        runTestLinreg("grid", currentOutputFolder);
    }

    @Test
    public void testLinregPrune() throws ParseException {
        String currentOutputFolder = OUTPUT_FOLDER_PREFIX + "Linreg_Prune";
        runTestLinreg("prune", currentOutputFolder);
    }

    @Test
    public void testLinregMemory() throws ParseException {
        String currentOutputFolder = OUTPUT_FOLDER_PREFIX + "Linreg_Memory";
        runTestLinreg("interest", currentOutputFolder);
    }

    @Test
    public void testPCAGrid() throws ParseException {
        String currentOutputFolder = OUTPUT_FOLDER_PREFIX + "PCA_Grid";
        runTestPCA("grid", currentOutputFolder);
    }

    @Test
    public void testPCAPrune() throws ParseException {
        String currentOutputFolder = OUTPUT_FOLDER_PREFIX + "PCA_Prune";
        runTestPCA("prune", currentOutputFolder);
    }

    @Test
    public void testPCAMemory() throws ParseException {
        String currentOutputFolder = OUTPUT_FOLDER_PREFIX + "PCA_Memory";
        runTestPCA("interest", currentOutputFolder);
    }

    @Test
    public void testPNMFGrid() throws ParseException {
        String currentOutputFolder = OUTPUT_FOLDER_PREFIX + "PNMF_Grid";
        runTestPNMF("grid", currentOutputFolder);
    }

    @Test
    public void testPNMFPrune() throws ParseException {
        String currentOutputFolder = OUTPUT_FOLDER_PREFIX + "PNMF_Prune";
        runTestPNMF("prune", currentOutputFolder);
    }

    @Test
    public void testPNMFMemory() throws ParseException {
        String currentOutputFolder = OUTPUT_FOLDER_PREFIX + "PNMF_Memory";
        runTestPNMF("interest", currentOutputFolder);
    }

    // Helpers ---------------------------------------------------------------------------------------------------------
    private static void runTestL2SVM(String enumeration, String outputFolder) throws ParseException {
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
                enumeration,
                outputFolder,
                localInput
        );
    }

    private static void runTestLinreg(String enumeration, String outputFolder) throws ParseException {
        String[] nvargs = new String[] {
                "fileX=s3://input/X.csv",
                "fileY=s3://input/Y.csv",
                "fileW=s3://output/W.csv"
        };
        String[] localInput = new String[] {
                "s3://input/X.csv=" + INPUT_X,
                "s3://input/Y.csv=" + INPUT_Y
        };

        runTest(SCRIPT_Linreg, nvargs, enumeration, outputFolder, localInput);
    }

    private static void runTestPCA(String enumeration, String outputFolder) throws ParseException {
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

        runTest(SCRIPT_PCA, nvargs, enumeration, outputFolder, localInput);
    }

    private static void runTestPNMF(String enumeration, String outputFolder) throws ParseException {
        String[] nvargs = new String[] {
                "fileX=s3://input/X.csv",
                "fileW=s3://output/W.csv",
                "fileH=s3://output/H.csv"
        };
        String[] localInput = new String[] {
                "s3://input/X.csv=" + INPUT_X
        };

        runTest(SCRIPT_PNMF, nvargs, enumeration, outputFolder, localInput);
    }

    private static void runTest(
            String scriptFile,
            String[] nvargs,
            String enumeration,
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
        options.setProperty("ENUMERATION", enumeration);
        options.setProperty("OPTIMIZATION_FUNCTION", OPTIMIZED_FOR);
        options.setProperty("COSTS_WEIGHT", "0.002");
        options.setProperty("MAX_TIME", "1000");
        options.setProperty("MAX_PRICE", "1");
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
