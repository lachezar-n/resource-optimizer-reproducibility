package external;

import org.apache.commons.cli.*;
        import org.apache.commons.configuration2.PropertiesConfiguration;

        import org.apache.sysds.resource.ResourceOptimizer;
        import org.junit.Assert;
        import org.junit.Test;

        import java.io.IOException;

        import java.util.Arrays;
        import java.util.stream.Stream;

public class FullSetTests {
    private static final String RESOURCE_FOLDER = "tests/resources";
    private static final String SCRIPT_L2SVM = RESOURCE_FOLDER + "/scripts/L2SVM.dml";
    private static final String SCRIPT_Linreg = RESOURCE_FOLDER + "/scripts/Linreg.dml";
    private static final String SCRIPT_PCA = RESOURCE_FOLDER + "/scripts/PCA.dml";
    private static final String SCRIPT_PNMF = RESOURCE_FOLDER + "/scripts/PNMF.dml";
    // All tests in the class will use dataset "M"
    private static final String INPUT_X_S = RESOURCE_FOLDER + "/data/SmallDenseX.csv";
    private static final String INPUT_X_M = RESOURCE_FOLDER + "/data/MediumDenseX.csv";
    private static final String INPUT_X_L = RESOURCE_FOLDER + "/data/LargeDenseX.csv";

    private static final String INPUT_Y_S = RESOURCE_FOLDER + "/data/SmallY.csv";
    private static final String INPUT_Y_M = RESOURCE_FOLDER + "/data/MediumY.csv";
    private static final String INPUT_Y_L = RESOURCE_FOLDER + "/data/LargeY.csv";
    private static final String OUTPUT_FOLDER_PREFIX = "comp_opt_func/";
    private static final String ENUMERATION = "grid";
    private static final String OPTIMIZATION_FUNCTION = "costs";

    @Test
    public void test_L2SVM_SizeM() throws ParseException {
        String currentOutputFolder = OUTPUT_FOLDER_PREFIX + "L2SVM_MinPrice";
        runTestL2SVM(currentOutputFolder, 'M');
    }

    @Test
    public void test_Linreg_SizeM() throws ParseException {
        String currentOutputFolder = OUTPUT_FOLDER_PREFIX + "Linreg_MinCosts";
        runTestLinreg(currentOutputFolder, 'M');
    }

    @Test
    public void test_PCA_SizeM() throws ParseException {
        String currentOutputFolder = OUTPUT_FOLDER_PREFIX + "PCA_MinCosts";
        runTestPCA(currentOutputFolder, 'M');
    }

    @Test
    public void test_PNMF_SizeM() throws ParseException {
        String currentOutputFolder = OUTPUT_FOLDER_PREFIX + "PNMF_MinCosts";
        runTestPNMF(currentOutputFolder, 'M');
    }


    // Helpers ---------------------------------------------------------------------------------------------------------
    private static void runTestL2SVM(String outputFolder, char size) throws ParseException {
        String[] nvargs = new String[3];
        String s3X = "s3://input/X.csv";
        nvargs[0] = "fileX="+s3X;
        String s3Y = "s3://input/Y.csv";
        nvargs[1] = "fileY="+s3Y;
        nvargs[2] = "fileW=s3://output/W.csv";
        String[] localInput = new String[2];
        switch (size) {
            case 'S':
                localInput[0] = s3X+"="+INPUT_X_S;
                localInput[1] = s3Y+"="+INPUT_Y_S;
                break;
            case 'M':
                localInput[0] = s3X+"="+INPUT_X_M;
                localInput[1] = s3Y+"="+INPUT_Y_M;
                break;
            case 'L':
                localInput[0] = s3X+"="+INPUT_X_L;
                localInput[1] = s3Y+"="+INPUT_Y_L;
                break;
            default:
                throw new RuntimeException("Invalid Size");
        }

        runTest(SCRIPT_L2SVM, nvargs, outputFolder, localInput);
    }

    private static void runTestLinreg(String outputFolder, char size) throws ParseException {
        String[] nvargs = new String[3];
        String s3X = "s3://input/X.csv";
        nvargs[0] = "fileX="+s3X;
        String s3Y = "s3://input/Y.csv";
        nvargs[1] = "fileY="+s3Y;
        nvargs[2] = "fileW=s3://output/W.csv";
        String[] localInput = new String[2];
        switch (size) {
            case 'S':
                localInput[0] = s3X+"="+INPUT_X_S;
                localInput[1] = s3Y+"="+INPUT_Y_S;
                break;
            case 'M':
                localInput[0] = s3X+"="+INPUT_X_M;
                localInput[1] = s3Y+"="+INPUT_Y_M;
                break;
            case 'L':
                localInput[0] = s3X+"="+INPUT_X_L;
                localInput[1] = s3Y+"="+INPUT_Y_L;
                break;
            default:
                throw new RuntimeException("Invalid Size");
        }

        runTest(SCRIPT_Linreg, nvargs, outputFolder, localInput);
    }

    private static void runTestPCA(String outputFolder, char size) throws ParseException {
        String[] nvargs = new String[4];
        String s3X = "s3://input/X.csv";
        nvargs[0] = "fileX="+s3X;
        nvargs[1] = "fileC=s3://output/C.csv";
        nvargs[2] = "fileC2=s3://output/C2.csv";
        nvargs[3] = "fileS2=s3://output/S2.csv";
        String[] localInput = new String[1];
        switch (size) {
            case 'S':
                localInput[0] = s3X+"="+INPUT_X_S;
                break;
            case 'M':
                localInput[0] = s3X+"="+INPUT_X_M;
                break;
            case 'L':
                localInput[0] = s3X+"="+INPUT_X_L;
                break;
            default:
                throw new RuntimeException("Invalid Size");
        }

        runTest(SCRIPT_PCA, nvargs, outputFolder, localInput);
    }

    private static void runTestPNMF(String outputFolder, char size) throws ParseException {
        String[] nvargs = new String[3];
        String s3X = "s3://input/X.csv";
        nvargs[0] = "fileX="+s3X;
        nvargs[1] = "fileW=s3://output/W.csv";
        nvargs[2] = "fileH=s3://output/H.csv";
        String[] localInput = new String[1];
        switch (size) {
            case 'S':
                localInput[0] = s3X+"="+INPUT_X_S;
                break;
            case 'M':
                localInput[0] = s3X+"="+INPUT_X_M;
                break;
            case 'L':
                localInput[0] = s3X+"="+INPUT_X_L;
                break;
            default:
                throw new RuntimeException("Invalid Size");
        }

        runTest(SCRIPT_PNMF, nvargs, outputFolder, localInput);
    }

    private static void runTest(
            String scriptFile,
            String[] nvargs,
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
        options.setProperty("INFO_TABLE", "systemds/scripts/resource/ec2_stats.csv");
        options.setProperty("REGION_TABLE", "systemds/scripts/resource/aws_regional_prices.csv");
        options.setProperty("OUTPUT_FOLDER", outputFolder);
        options.setProperty(
                "LOCAL_INPUTS", local_input_builder.toString());
        options.setProperty("ENUMERATION", ENUMERATION);
        options.setProperty("OPTIMIZATION_FUNCTION", OPTIMIZATION_FUNCTION);
        options.setProperty("COSTS_WEIGHT", "0.002");
        options.setProperty("MAX_TIME", "2000");
        options.setProperty("MAX_PRICE", "2");
        options.setProperty("CPU_QUOTA", "300");
        options.setProperty("MIN_EXECUTORS", "");
        options.setProperty("MAX_EXECUTORS", "");
        options.setProperty("INSTANCE_FAMILIES", "");
        options.setProperty("INSTANCE_SIZES", "");
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