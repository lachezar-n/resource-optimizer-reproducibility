package external;

import org.apache.sysds.conf.CompilerConfig;
import org.apache.sysds.conf.ConfigurationManager;
import org.apache.sysds.resource.CloudInstance;
import org.apache.sysds.resource.CloudUtils;
import org.apache.sysds.resource.enumeration.EnumerationUtils;
import org.apache.sysds.resource.enumeration.Enumerator;
import org.apache.sysds.resource.enumeration.GridBasedEnumerator;
import org.apache.sysds.resource.enumeration.PruneBasedEnumerator;
import org.apache.sysds.runtime.controlprogram.Program;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.util.HashMap;

import static external.ResourceTestUtils.DEFAULT_INSTANCE_INFO_TABLE;

/**
 * This class provides extensive tests for the process
 * of resource optimization for the Prune-based Enumerator
 * in order to validate its target functionality:
 * the enumeration delivers the same results as for the
 * Grid-based enumeration mechanism.
 */
public class PuneBasedEnumerationValidationTests {
    static {
        ConfigurationManager.getCompilerConfig().set(CompilerConfig.ConfigType.RESOURCE_OPTIMIZATION, true);
    }
    private static final String TEST_DIR = "./tests/";
    private static final String SCRIPTS_DIR = TEST_DIR + "resources/scripts/";
    private static final String DATA_DIR = TEST_DIR + "resources/data/";
    private static HashMap<String, CloudInstance> allInstances;

    // Further relevant enumeration setting
    private static final Enumerator.OptimizationStrategy objectiveFunction = Enumerator.OptimizationStrategy.MinPrice;
    private static final int maxNumberExecutors = -1; // -1 fro setting no limit

    @BeforeClass
    public static void setUpBeforeClass() {
        try {
            allInstances = CloudUtils.loadInstanceInfoTable(DEFAULT_INSTANCE_INFO_TABLE, 0.25, 0.08);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // L2SVM

    @Test
    public void L2SSVMDefaultTest() {
        HashMap<String, String> nvargs = new HashMap<>();
        nvargs.put("$fileX", DATA_DIR+"DefaultX.csv");
        nvargs.put("$fileY", DATA_DIR+"DefaultY.csv");
        nvargs.put("$fileW", DATA_DIR+"DummyOutputW.csv");
        runTest("L2SVM.dml", nvargs);
    }

    @Test
    public void L2SSVMSmallInputTest() {
        HashMap<String, String> nvargs = new HashMap<>();
        nvargs.put("$fileX", DATA_DIR+"SmallDenseX.csv");
        nvargs.put("$fileY", DATA_DIR+"SmallY.csv");
        nvargs.put("$fileW", DATA_DIR+"DummyOutputW.csv");
        runTest("L2SVM.dml", nvargs);
    }

    @Test
    public void L2SSVMMediumInputTest() {
        HashMap<String, String> nvargs = new HashMap<>();
        nvargs.put("$fileX", DATA_DIR+"MediumDenseX.csv");
        nvargs.put("$fileY", DATA_DIR+"MediumY.csv");
        nvargs.put("$fileW", DATA_DIR+"DummyOutputW.csv");
        runTest("L2SVM.dml", nvargs);
    }

    @Test
    public void L2SSVMLargeInputTest() {
        HashMap<String, String> nvargs = new HashMap<>();
        nvargs.put("$fileX", DATA_DIR+"LargeDenseX.csv");
        nvargs.put("$fileY", DATA_DIR+"LargeY.csv");
        nvargs.put("$fileW", DATA_DIR+"DummyOutputW.csv");
        runTest("L2SVM.dml", nvargs);
    }

    //Linreg

    @Test
    public void LinregDefaultTest() {
        HashMap<String, String> nvargs = new HashMap<>();
        nvargs.put("$fileX", DATA_DIR+"DefaultX.csv");
        nvargs.put("$fileY", DATA_DIR+"DefaultY.csv");
        nvargs.put("$fileW", DATA_DIR+"DummyOutputW.csv");
        runTest("Linreg.dml", nvargs);
    }

    @Test
    public void LinregSmallDenseTest() {
        HashMap<String, String> nvargs = new HashMap<>();
        nvargs.put("$fileX", DATA_DIR+"SmallDenseX.csv");
        nvargs.put("$fileY", DATA_DIR+"SmallY.csv");
        nvargs.put("$fileW", DATA_DIR+"DummyOutputW.csv");
        runTest("Linreg.dml", nvargs);
    }

    @Test
    public void LinregMediumTest() {
        HashMap<String, String> nvargs = new HashMap<>();
        nvargs.put("$fileX", DATA_DIR+"MediumDenseX.csv");
        nvargs.put("$fileY", DATA_DIR+"MediumY.csv");
        nvargs.put("$fileW", DATA_DIR+"DummyOutputW.csv");
        runTest("Linreg.dml", nvargs);
    }

    @Test
    public void LinregLargeInputTest() {
        HashMap<String, String> nvargs = new HashMap<>();
        nvargs.put("$fileX", DATA_DIR+"LargeDenseX.csv");
        nvargs.put("$fileY", DATA_DIR+"LargeY.csv");
        nvargs.put("$fileW", DATA_DIR+"DummyOutputW.csv");
        runTest("Linreg.dml", nvargs);
    }

    // PCA
    @Test
    public void PCADefaultTest() {
        HashMap<String, String> nvargs = new HashMap<>();
        nvargs.put("$fileX", DATA_DIR+"DefaultX.csv");
        nvargs.put("$fileXReduced", DATA_DIR+"DummyOutputX.csv");
        nvargs.put("$fileC", DATA_DIR+"DummyOutputC.csv");
        nvargs.put("$fileC2", DATA_DIR+"DummyOutputC2.csv");
        nvargs.put("$fileS2", DATA_DIR+"DummyOutputS2.csv");
        runTest("PCA.dml", nvargs);
    }

    @Test
    public void PCASmallTest() {
        HashMap<String, String> nvargs = new HashMap<>();
        nvargs.put("$fileX", DATA_DIR+"SmallDenseX.csv");
        nvargs.put("$fileXReduced", DATA_DIR+"DummyOutputX.csv");
        nvargs.put("$fileC", DATA_DIR+"DummyOutputC.csv");
        nvargs.put("$fileC2", DATA_DIR+"DummyOutputC2.csv");
        nvargs.put("$fileS2", DATA_DIR+"DummyOutputS2.csv");
        runTest("PCA.dml", nvargs);
    }

    @Test
    public void PCAMediumTest() {
        HashMap<String, String> nvargs = new HashMap<>();
        nvargs.put("$fileX", DATA_DIR+"MediumDenseX.csv");
        nvargs.put("$fileXReduced", DATA_DIR+"DummyOutputX.csv");
        nvargs.put("$fileC", DATA_DIR+"DummyOutputC.csv");
        nvargs.put("$fileC2", DATA_DIR+"DummyOutputC2.csv");
        nvargs.put("$fileS2", DATA_DIR+"DummyOutputS2.csv");
        runTest("PCA.dml", nvargs);
    }

    @Test
    public void PCALargeInputTest() {
        // input of 100GB
        HashMap<String, String> nvargs = new HashMap<>();
        nvargs.put("$fileX", DATA_DIR+"LargeDenseX.csv");
        nvargs.put("$fileXReduced", DATA_DIR+"DummyOutputX.csv");
        nvargs.put("$fileC", DATA_DIR+"DummyOutputC.csv");
        nvargs.put("$fileC2", DATA_DIR+"DummyOutputC2.csv");
        nvargs.put("$fileS2", DATA_DIR+"DummyOutputS2.csv");
        runTest("PCA.dml", nvargs);
    }

    // PNMF

    @Test
    public void PNMFDefaultTest() {
        HashMap<String, String> nvargs = new HashMap<>();
        nvargs.put("$fileX", DATA_DIR+"DefaultX.csv");
        nvargs.put("$fileW", DATA_DIR+"DummyOutputW.csv");
        nvargs.put("$fileH", DATA_DIR+"DummyOutputH.csv");
        runTest("PNMF.dml", nvargs);
    }

    @Test
    public void PNMFSmallTest() {
        HashMap<String, String> nvargs = new HashMap<>();
        nvargs.put("$fileX", DATA_DIR+"SmallDenseX.csv");
        nvargs.put("$fileW", DATA_DIR+"DummyOutputW.csv");
        nvargs.put("$fileH", DATA_DIR+"DummyOutputH.csv");
        runTest("PNMF.dml", nvargs);
    }

    @Test
    public void PNMFMediumTest() {
        HashMap<String, String> nvargs = new HashMap<>();
        nvargs.put("$fileX", DATA_DIR+"MediumDenseX.csv");
        nvargs.put("$fileW", DATA_DIR+"DummyOutputW.csv");
        nvargs.put("$fileH", DATA_DIR+"DummyOutputH.csv");
        runTest("PNMF.dml", nvargs);
    }

    @Test
    public void PNMFLargeInputTest() {
        // input of 100GB
        HashMap<String, String> nvargs = new HashMap<>();
        nvargs.put("$fileX", DATA_DIR+"LargeDenseX.csv");
        nvargs.put("$fileW", DATA_DIR+"DummyOutputW.csv");
        nvargs.put("$fileH", DATA_DIR+"DummyOutputH.csv");
        runTest("PNMF.dml", nvargs);
    }

    // Helpers ---------------------------------------------------------------------------------------------------------

    private void runTest(String scriptFilename, HashMap<String, String> args) {
        System.out.println("Comparing Grid- vs Pune-based Enumeration strategies with optimization for "
                + objectiveFunction);
        long startTime, endTime;
        Program program = ResourceTestUtils.compileProgramWithNvargs(SCRIPTS_DIR + scriptFilename, args);
        Enumerator.setMinTime(100000);
        Enumerator.setMinTime(1000);
        // grid-based enumerator for expected value generation
        GridBasedEnumerator gridEnumerator = (GridBasedEnumerator) (new Enumerator.Builder())
                .withRuntimeProgram(program)
                .withAvailableInstances(allInstances)
                .withEnumerationStrategy(Enumerator.EnumerationStrategy.GridBased)
                .withOptimizationStrategy(objectiveFunction)
                .withNumberExecutorsRange(0, maxNumberExecutors)
                .build();
        System.out.println("Launching the Grid-Based Enumerator for expected solution");
        startTime = System.currentTimeMillis();
        gridEnumerator.preprocessing();
        gridEnumerator.processing();
        EnumerationUtils.SolutionPoint expectedSolution = gridEnumerator.postprocessing();
        endTime = System.currentTimeMillis();
        System.out.println("Grid-Based Enumerator finished for "+(endTime-startTime)/1000+"s");

        // prune-based enumerator for actual value generation
        PruneBasedEnumerator pruningEnumerator = (PruneBasedEnumerator) (new Enumerator.Builder())
                .withRuntimeProgram(program)
                .withAvailableInstances(allInstances)
                .withEnumerationStrategy(Enumerator.EnumerationStrategy.PruneBased)
                .withOptimizationStrategy(objectiveFunction)
                .withNumberExecutorsRange(0, maxNumberExecutors)
                .build();
        System.out.println("Launching the Prune-Based Enumerator for testing solution");
        startTime = System.currentTimeMillis();
        pruningEnumerator.preprocessing();
        pruningEnumerator.processing();
        EnumerationUtils.SolutionPoint actualSolution = pruningEnumerator.postprocessing();
        endTime = System.currentTimeMillis();
        System.out.println("Prune-Based Enumerator finished for "+(endTime-startTime)/1000+"s");

        // compare solution
        ResourceTestUtils.assertEqualSolutionPoints(expectedSolution, actualSolution);
    }
}
