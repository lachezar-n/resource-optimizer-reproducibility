package external;

import org.apache.sysds.conf.CompilerConfig;
import org.apache.sysds.conf.ConfigurationManager;
import org.apache.sysds.conf.DMLConfig;
import org.apache.sysds.resource.CloudInstance;
import org.apache.sysds.resource.CloudUtils;
import org.apache.sysds.resource.enumeration.EnumerationUtils;
import org.apache.sysds.resource.enumeration.Enumerator;
import org.apache.sysds.runtime.controlprogram.Program;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.util.HashMap;

import static external.ResourceTestUtils.DEFAULT_INSTANCE_INFO_TABLE;

/**
 * This class provides extensive tests for the all Enumerators
 * targeting the algorithms used to the experiments on AWS
 * in order ot validate the functionality before launching any cloud resources.
 */
public class EnumeratorsValidationTests {
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

    // L2SVM Tests
    @Test
    public void GridBasedEnumerationSmallL2SVMTest() {
        HashMap<String, String> nvargs = new HashMap<>();
        nvargs.put("$fileX", DATA_DIR+"SmallDenseX.csv");
        nvargs.put("$fileY", DATA_DIR+"SmallY.csv");
        nvargs.put("$fileW", DATA_DIR+"DummyOutputW.csv");
        runEnumerationOnFullSetInstances("L2SVM.dml", nvargs, Enumerator.EnumerationStrategy.GridBased);
    }

    @Test
    public void GridBasedEnumerationMediumL2SVMTest() {
        HashMap<String, String> nvargs = new HashMap<>();
        nvargs.put("$fileX", DATA_DIR+"MediumDenseX.csv");
        nvargs.put("$fileY", DATA_DIR+"MediumY.csv");
        nvargs.put("$fileW", DATA_DIR+"DummyOutputW.csv");
        runEnumerationOnFullSetInstances("L2SVM.dml", nvargs, Enumerator.EnumerationStrategy.GridBased);
    }

    @Test
    public void GridBasedEnumerationLargeL2SVMTest() {
        HashMap<String, String> nvargs = new HashMap<>();
        nvargs.put("$fileX", DATA_DIR+"LargeDenseX.csv");
        nvargs.put("$fileY", DATA_DIR+"LargeY.csv");
        nvargs.put("$fileW", DATA_DIR+"DummyOutputW.csv");
        runEnumerationOnFullSetInstances("L2SVM.dml", nvargs, Enumerator.EnumerationStrategy.GridBased);
    }

    @Test
    public void InterestBasedEnumerationSmallL2SVMTest() {
        HashMap<String, String> nvargs = new HashMap<>();
        nvargs.put("$fileX", DATA_DIR+"SmallDenseX.csv");
        nvargs.put("$fileY", DATA_DIR+"SmallY.csv");
        nvargs.put("$fileW", DATA_DIR+"DummyOutputW.csv");
        runEnumerationOnFullSetInstances("L2SVM.dml", nvargs, Enumerator.EnumerationStrategy.InterestBased);
    }

    @Test
    public void InterestBasedEnumerationMediumL2SVMTest() {
        HashMap<String, String> nvargs = new HashMap<>();
        nvargs.put("$fileX", DATA_DIR+"MediumDenseX.csv");
        nvargs.put("$fileY", DATA_DIR+"MediumY.csv");
        nvargs.put("$fileW", DATA_DIR+"DummyOutputW.csv");
        runEnumerationOnFullSetInstances("L2SVM.dml", nvargs, Enumerator.EnumerationStrategy.InterestBased);
    }

    @Test
    public void InterestBasedEnumerationLargeL2SVMTest() {
        HashMap<String, String> nvargs = new HashMap<>();
        nvargs.put("$fileX", DATA_DIR+"LargeDenseX.csv");
        nvargs.put("$fileY", DATA_DIR+"LargeY.csv");
        nvargs.put("$fileW", DATA_DIR+"DummyOutputW.csv");
        runEnumerationOnFullSetInstances("L2SVM.dml", nvargs, Enumerator.EnumerationStrategy.InterestBased);
    }


    @Test
    public void PruneBasedEnumerationSmallL2SVMTest() {
        HashMap<String, String> nvargs = new HashMap<>();
        nvargs.put("$fileX", DATA_DIR+"SmallDenseX.csv");
        nvargs.put("$fileY", DATA_DIR+"SmallY.csv");
        nvargs.put("$fileW", DATA_DIR+"DummyOutputW.csv");
        runEnumerationOnFullSetInstances("L2SVM.dml", nvargs, Enumerator.EnumerationStrategy.PruneBased);
    }

    @Test
    public void PruneBasedEnumerationMediumL2SVMTest() {
        HashMap<String, String> nvargs = new HashMap<>();
        nvargs.put("$fileX", DATA_DIR+"MediumDenseX.csv");
        nvargs.put("$fileY", DATA_DIR+"MediumY.csv");
        nvargs.put("$fileW", DATA_DIR+"DummyOutputW.csv");
        runEnumerationOnFullSetInstances("L2SVM.dml", nvargs, Enumerator.EnumerationStrategy.PruneBased);
    }

    @Test
    public void PruneBasedEnumerationLargeL2SVMTest() {
        HashMap<String, String> nvargs = new HashMap<>();
        nvargs.put("$fileX", DATA_DIR+"LargeDenseX.csv");
        nvargs.put("$fileY", DATA_DIR+"LargeY.csv");
        nvargs.put("$fileW", DATA_DIR+"DummyOutputW.csv");
        runEnumerationOnFullSetInstances("L2SVM.dml", nvargs, Enumerator.EnumerationStrategy.PruneBased);
    }

    // Linreg tests

    @Test
    public void GridBasedEnumerationSmallLinregTest() {
        HashMap<String, String> nvargs = new HashMap<>();
        nvargs.put("$fileX", DATA_DIR+"SmallDenseX.csv");
        nvargs.put("$fileY", DATA_DIR+"SmallY.csv");
        nvargs.put("$fileW", DATA_DIR+"DummyOutputW.csv");
        runEnumerationOnFullSetInstances("Linreg.dml", nvargs, Enumerator.EnumerationStrategy.GridBased);
    }

    @Test
    public void GridBasedEnumerationMediumLinregTest() {
        HashMap<String, String> nvargs = new HashMap<>();
        nvargs.put("$fileX", DATA_DIR+"MediumDenseX.csv");
        nvargs.put("$fileY", DATA_DIR+"MediumY.csv");
        nvargs.put("$fileW", DATA_DIR+"DummyOutputW.csv");
        runEnumerationOnFullSetInstances("Linreg.dml", nvargs, Enumerator.EnumerationStrategy.GridBased);
    }

    @Test
    public void GridBasedEnumerationLargeLinregTest() {
        HashMap<String, String> nvargs = new HashMap<>();
        nvargs.put("$fileX", DATA_DIR+"LargeDenseX.csv");
        nvargs.put("$fileY", DATA_DIR+"LargeY.csv");
        nvargs.put("$fileW", DATA_DIR+"DummyOutputW.csv");
        runEnumerationOnFullSetInstances("Linreg.dml", nvargs, Enumerator.EnumerationStrategy.GridBased);
    }

    @Test
    public void InterestBasedEnumerationSmallLinregTest() {
        HashMap<String, String> nvargs = new HashMap<>();
        nvargs.put("$fileX", DATA_DIR+"SmallDenseX.csv");
        nvargs.put("$fileY", DATA_DIR+"SmallY.csv");
        nvargs.put("$fileW", DATA_DIR+"DummyOutputW.csv");
        runEnumerationOnFullSetInstances("Linreg.dml", nvargs, Enumerator.EnumerationStrategy.InterestBased);
    }

    @Test
    public void InterestBasedEnumerationMediumLinregTest() {
        HashMap<String, String> nvargs = new HashMap<>();
        nvargs.put("$fileX", DATA_DIR+"MediumDenseX.csv");
        nvargs.put("$fileY", DATA_DIR+"MediumY.csv");
        nvargs.put("$fileW", DATA_DIR+"DummyOutputW.csv");
        runEnumerationOnFullSetInstances("Linreg.dml", nvargs, Enumerator.EnumerationStrategy.InterestBased);
    }

    @Test
    public void InterestBasedEnumerationLargeLinregTest() {
        HashMap<String, String> nvargs = new HashMap<>();
        nvargs.put("$fileX", DATA_DIR+"LargeDenseX.csv");
        nvargs.put("$fileY", DATA_DIR+"LargeY.csv");
        nvargs.put("$fileW", DATA_DIR+"DummyOutputW.csv");
        runEnumerationOnFullSetInstances("Linreg.dml", nvargs, Enumerator.EnumerationStrategy.InterestBased);
    }


    @Test
    public void PruneBasedEnumerationSmallLinregTest() {
        HashMap<String, String> nvargs = new HashMap<>();
        nvargs.put("$fileX", DATA_DIR+"SmallDenseX.csv");
        nvargs.put("$fileY", DATA_DIR+"SmallY.csv");
        nvargs.put("$fileW", DATA_DIR+"DummyOutputW.csv");
        runEnumerationOnFullSetInstances("Linreg.dml", nvargs, Enumerator.EnumerationStrategy.PruneBased);
    }

    @Test
    public void PruneBasedEnumerationMediumLinregTest() {
        HashMap<String, String> nvargs = new HashMap<>();
        nvargs.put("$fileX", DATA_DIR+"MediumDenseX.csv");
        nvargs.put("$fileY", DATA_DIR+"MediumY.csv");
        nvargs.put("$fileW", DATA_DIR+"DummyOutputW.csv");
        runEnumerationOnFullSetInstances("Linreg.dml", nvargs, Enumerator.EnumerationStrategy.PruneBased);
    }

    @Test
    public void PruneBasedEnumerationLargeLinregTest() {
        HashMap<String, String> nvargs = new HashMap<>();
        nvargs.put("$fileX", DATA_DIR+"LargeDenseX.csv");
        nvargs.put("$fileY", DATA_DIR+"LargeY.csv");
        nvargs.put("$fileW", DATA_DIR+"DummyOutputW.csv");
        runEnumerationOnFullSetInstances("Linreg.dml", nvargs, Enumerator.EnumerationStrategy.PruneBased);
    }

    // PCA Tests

    @Test
    public void GridBasedEnumerationSmallPCATest() {
        HashMap<String, String> nvargs = new HashMap<>();
        nvargs.put("$fileX", DATA_DIR+"SmallDenseX.csv");
        nvargs.put("$fileXReduced", DATA_DIR+"DummyOutputXReduced.csv");
        nvargs.put("$fileC", DATA_DIR+"DummyOutputC.csv");
        nvargs.put("$fileC2", DATA_DIR+"DummyOutputC.csv");
        nvargs.put("$fileS2", DATA_DIR+"DummyOutputC.csv");

        runEnumerationOnFullSetInstances("PCA.dml", nvargs, Enumerator.EnumerationStrategy.GridBased);
    }

    @Test
    public void GridBasedEnumerationMediumPCATest() {
        HashMap<String, String> nvargs = new HashMap<>();
        nvargs.put("$fileX", DATA_DIR+"MediumDenseX.csv");
        nvargs.put("$fileXReduced", DATA_DIR+"DummyOutputXReduced.csv");
        nvargs.put("$fileC", DATA_DIR+"DummyOutputC.csv");
        nvargs.put("$fileC2", DATA_DIR+"DummyOutputC.csv");
        nvargs.put("$fileS2", DATA_DIR+"DummyOutputC.csv");
        runEnumerationOnFullSetInstances("PCA.dml", nvargs, Enumerator.EnumerationStrategy.GridBased);
    }

    @Test
    public void GridBasedEnumerationLargePCATest() {
        HashMap<String, String> nvargs = new HashMap<>();
        nvargs.put("$fileX", DATA_DIR+"LargeDenseX.csv");
        nvargs.put("$fileXReduced", DATA_DIR+"DummyOutputXReduced.csv");
        nvargs.put("$fileC", DATA_DIR+"DummyOutputC.csv");
        nvargs.put("$fileC2", DATA_DIR+"DummyOutputC.csv");
        nvargs.put("$fileS2", DATA_DIR+"DummyOutputC.csv");
        runEnumerationOnFullSetInstances("PCA.dml", nvargs, Enumerator.EnumerationStrategy.GridBased);
    }

    @Test
    public void InterestBasedEnumerationSmallPCATest() {
        HashMap<String, String> nvargs = new HashMap<>();
        nvargs.put("$fileX", DATA_DIR+"SmallDenseX.csv");
        nvargs.put("$fileXReduced", DATA_DIR+"DummyOutputXReduced.csv");
        nvargs.put("$fileC", DATA_DIR+"DummyOutputC.csv");
        nvargs.put("$fileC2", DATA_DIR+"DummyOutputC.csv");
        nvargs.put("$fileS2", DATA_DIR+"DummyOutputC.csv");
        runEnumerationOnFullSetInstances("PCA.dml", nvargs, Enumerator.EnumerationStrategy.InterestBased);
    }

    @Test
    public void InterestBasedEnumerationMediumPCATest() {
        HashMap<String, String> nvargs = new HashMap<>();
        nvargs.put("$fileX", DATA_DIR+"MediumDenseX.csv");
        nvargs.put("$fileXReduced", DATA_DIR+"DummyOutputXReduced.csv");
        nvargs.put("$fileC", DATA_DIR+"DummyOutputC.csv");
        nvargs.put("$fileC2", DATA_DIR+"DummyOutputC.csv");
        nvargs.put("$fileS2", DATA_DIR+"DummyOutputC.csv");
        runEnumerationOnFullSetInstances("PCA.dml", nvargs, Enumerator.EnumerationStrategy.InterestBased);
    }

    @Test
    public void InterestBasedEnumerationLargePCATest() {
        HashMap<String, String> nvargs = new HashMap<>();
        nvargs.put("$fileX", DATA_DIR+"LargeDenseX.csv");
        nvargs.put("$fileXReduced", DATA_DIR+"DummyOutputXReduced.csv");
        nvargs.put("$fileC", DATA_DIR+"DummyOutputC.csv");
        nvargs.put("$fileC2", DATA_DIR+"DummyOutputC.csv");
        nvargs.put("$fileS2", DATA_DIR+"DummyOutputC.csv");
        runEnumerationOnFullSetInstances("PCA.dml", nvargs, Enumerator.EnumerationStrategy.InterestBased);
    }


    @Test
    public void PruneBasedEnumerationSmallPCATest() {
        HashMap<String, String> nvargs = new HashMap<>();
        nvargs.put("$fileX", DATA_DIR+"SmallDenseX.csv");
        nvargs.put("$fileXReduced", DATA_DIR+"DummyOutputXReduced.csv");
        nvargs.put("$fileC", DATA_DIR+"DummyOutputC.csv");
        nvargs.put("$fileC2", DATA_DIR+"DummyOutputC.csv");
        nvargs.put("$fileS2", DATA_DIR+"DummyOutputC.csv");
        runEnumerationOnFullSetInstances("PCA.dml", nvargs, Enumerator.EnumerationStrategy.PruneBased);
    }

    @Test
    public void PruneBasedEnumerationMediumPCATest() {
        HashMap<String, String> nvargs = new HashMap<>();
        nvargs.put("$fileX", DATA_DIR+"MediumDenseX.csv");
        nvargs.put("$fileXReduced", DATA_DIR+"DummyOutputXReduced.csv");
        nvargs.put("$fileC", DATA_DIR+"DummyOutputC.csv");
        nvargs.put("$fileC2", DATA_DIR+"DummyOutputC.csv");
        nvargs.put("$fileS2", DATA_DIR+"DummyOutputC.csv");
        runEnumerationOnFullSetInstances("PCA.dml", nvargs, Enumerator.EnumerationStrategy.PruneBased);
    }

    @Test
    public void PruneBasedEnumerationLargePCATest() {
        HashMap<String, String> nvargs = new HashMap<>();
        nvargs.put("$fileX", DATA_DIR+"LargeDenseX.csv");
        nvargs.put("$fileXReduced", DATA_DIR+"DummyOutputXReduced.csv");
        nvargs.put("$fileC", DATA_DIR+"DummyOutputC.csv");
        nvargs.put("$fileC2", DATA_DIR+"DummyOutputC.csv");
        nvargs.put("$fileS2", DATA_DIR+"DummyOutputC.csv");
        runEnumerationOnFullSetInstances("PCA.dml", nvargs, Enumerator.EnumerationStrategy.PruneBased);
    }

    // PNMF Tests

    @Test
    public void GridBasedEnumerationSmallPNMFTest() {
        HashMap<String, String> nvargs = new HashMap<>();
        nvargs.put("$fileX", DATA_DIR+"SmallDenseX.csv");
        nvargs.put("$fileW", DATA_DIR+"DummyOutputW.csv");
        nvargs.put("$fileH", DATA_DIR+"DummyOutputH.csv");

        runEnumerationOnFullSetInstances("PNMF.dml", nvargs, Enumerator.EnumerationStrategy.GridBased);
    }

    @Test
    public void GridBasedEnumerationMediumPNMFTest() {
        HashMap<String, String> nvargs = new HashMap<>();
        nvargs.put("$fileX", DATA_DIR+"MediumDenseX.csv");
        nvargs.put("$fileW", DATA_DIR+"DummyOutputW.csv");
        nvargs.put("$fileH", DATA_DIR+"DummyOutputH.csv");
        runEnumerationOnFullSetInstances("PNMF.dml", nvargs, Enumerator.EnumerationStrategy.GridBased);
    }

    @Test
    public void GridBasedEnumerationLargePNMFTest() {
        HashMap<String, String> nvargs = new HashMap<>();
        nvargs.put("$fileX", DATA_DIR+"LargeDenseX.csv");
        nvargs.put("$fileW", DATA_DIR+"DummyOutputW.csv");
        nvargs.put("$fileH", DATA_DIR+"DummyOutputH.csv");
        runEnumerationOnFullSetInstances("PNMF.dml", nvargs, Enumerator.EnumerationStrategy.GridBased);
    }

    @Test
    public void InterestBasedEnumerationSmallPNMFTest() {
        HashMap<String, String> nvargs = new HashMap<>();
        nvargs.put("$fileX", DATA_DIR+"SmallDenseX.csv");
        nvargs.put("$fileW", DATA_DIR+"DummyOutputW.csv");
        nvargs.put("$fileH", DATA_DIR+"DummyOutputH.csv");
        runEnumerationOnFullSetInstances("PNMF.dml", nvargs, Enumerator.EnumerationStrategy.InterestBased);
    }

    @Test
    public void InterestBasedEnumerationMediumPNMFTest() {
        HashMap<String, String> nvargs = new HashMap<>();
        nvargs.put("$fileX", DATA_DIR+"MediumDenseX.csv");
        nvargs.put("$fileW", DATA_DIR+"DummyOutputW.csv");
        nvargs.put("$fileH", DATA_DIR+"DummyOutputH.csv");
        runEnumerationOnFullSetInstances("PNMF.dml", nvargs, Enumerator.EnumerationStrategy.InterestBased);
    }

    @Test
    public void InterestBasedEnumerationLargePNMFTest() {
        HashMap<String, String> nvargs = new HashMap<>();
        nvargs.put("$fileX", DATA_DIR+"LargeDenseX.csv");
        nvargs.put("$fileW", DATA_DIR+"DummyOutputW.csv");
        nvargs.put("$fileH", DATA_DIR+"DummyOutputH.csv");
        runEnumerationOnFullSetInstances("PNMF.dml", nvargs, Enumerator.EnumerationStrategy.InterestBased);
    }


    @Test
    public void PruneBasedEnumerationSmallPNMFTest() {
        HashMap<String, String> nvargs = new HashMap<>();
        nvargs.put("$fileX", DATA_DIR+"SmallDenseX.csv");
        nvargs.put("$fileW", DATA_DIR+"DummyOutputW.csv");
        nvargs.put("$fileH", DATA_DIR+"DummyOutputH.csv");
        runEnumerationOnFullSetInstances("PNMF.dml", nvargs, Enumerator.EnumerationStrategy.PruneBased);
    }

    @Test
    public void PruneBasedEnumerationMediumPNMFTest() {
        HashMap<String, String> nvargs = new HashMap<>();
        nvargs.put("$fileX", DATA_DIR+"MediumDenseX.csv");
        nvargs.put("$fileW", DATA_DIR+"DummyOutputW.csv");
        nvargs.put("$fileH", DATA_DIR+"DummyOutputH.csv");
        runEnumerationOnFullSetInstances("PNMF.dml", nvargs, Enumerator.EnumerationStrategy.PruneBased);
    }

    @Test
    public void PruneBasedEnumerationLargePNMFTest() {
        HashMap<String, String> nvargs = new HashMap<>();
        nvargs.put("$fileX", DATA_DIR+"LargeDenseX.csv");
        nvargs.put("$fileW", DATA_DIR+"DummyOutputW.csv");
        nvargs.put("$fileH", DATA_DIR+"DummyOutputH.csv");
        runEnumerationOnFullSetInstances("PNMF.dml", nvargs, Enumerator.EnumerationStrategy.PruneBased);
    }

    // Helpers ---------------------------------------------------------------------------------------------------------

    private void runEnumerationOnFullSetInstances(String scriptFilename, HashMap<String, String> nvargs,
                                                  Enumerator.EnumerationStrategy strategy) {
        if (nvargs == null) nvargs = new HashMap<>();
        EnumerationUtils.SolutionPoint solution;
        try {
            Program program = ResourceTestUtils.compileProgramWithNvargs(SCRIPTS_DIR + scriptFilename, nvargs);

            ConfigurationManager.setLocalConfig(new DMLConfig());
            System.out.println("Enumerator of type "+strategy.toString()+" launches on full range of instances...");
            Enumerator.setMinTime(100000);
            Enumerator.setMinTime(1000);
            Enumerator enumerator = (new Enumerator.Builder())
                    .withRuntimeProgram(program)
                    .withAvailableInstances(allInstances)
                    .withEnumerationStrategy(strategy)
                    .withOptimizationStrategy(objectiveFunction)
                    .withNumberExecutorsRange(0, maxNumberExecutors)
                    .build();
            // run the enumerator
            enumerator.preprocessing();
            enumerator.processing();
            solution = enumerator.postprocessing();
            if (solution.driverInstance == null) {
                Assert.fail("Enumeration process finished without finding optimal solution");
            }
            System.out.println("Enumeration process finished with optimal solution:\n" + solution);
            System.out.printf("Costs for the corresponding solutions are %.2f seconds and %.2f $",
                    solution.getTimeCost(), solution.getMonetaryCost());
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error at testing enumeration on full instance range");
        }
        Assert.assertTrue(solution.getTimeCost() < Double.MAX_VALUE);
        Assert.assertTrue(solution.getMonetaryCost() < Double.MAX_VALUE);
    }
}
