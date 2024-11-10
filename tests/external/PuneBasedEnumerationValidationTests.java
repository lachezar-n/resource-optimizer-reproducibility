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
        Enumerator.setCpuQuota(300);
        Enumerator.setCostsWeightFactor(0.002);
    }
    private static final String TEST_DIR = "./tests/";
    private static final String SCRIPTS_DIR = TEST_DIR + "resources/scripts/";
    private static final String DATA_DIR = TEST_DIR + "resources/data/";
    private static HashMap<String, CloudInstance> allInstances;

    // Further relevant enumeration setting
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
    public void L2SSVMDefaultMinCostsTest() {
        Enumerator.setMinTime(1000);
        Enumerator.setMinPrice(1);

        HashMap<String, String> nvargs = new HashMap<>();
        nvargs.put("$fileX", DATA_DIR+"DefaultX.csv");
        nvargs.put("$fileY", DATA_DIR+"DefaultY.csv");
        nvargs.put("$fileW", DATA_DIR+"DummyOutputW.csv");
        runTest("L2SVM.dml", nvargs, Enumerator.OptimizationStrategy.MinCosts);
    }

    @Test
    public void L2SSVMDefaultMinPriceTest() {
        Enumerator.setMinTime(1000);
        Enumerator.setMinPrice(1);

        HashMap<String, String> nvargs = new HashMap<>();
        nvargs.put("$fileX", DATA_DIR+"DefaultX.csv");
        nvargs.put("$fileY", DATA_DIR+"DefaultY.csv");
        nvargs.put("$fileW", DATA_DIR+"DummyOutputW.csv");
        runTest("L2SVM.dml", nvargs, Enumerator.OptimizationStrategy.MinPrice);
    }

    @Test
    public void L2SSVMDefaultMinTimeTest() {
        Enumerator.setMinTime(1000);
        Enumerator.setMinPrice(1);

        HashMap<String, String> nvargs = new HashMap<>();
        nvargs.put("$fileX", DATA_DIR+"DefaultX.csv");
        nvargs.put("$fileY", DATA_DIR+"DefaultY.csv");
        nvargs.put("$fileW", DATA_DIR+"DummyOutputW.csv");
        runTest("L2SVM.dml", nvargs, Enumerator.OptimizationStrategy.MinTime);
    }

    @Test
    public void L2SSVMSmallInputMinCostsTest() {
        Enumerator.setMinTime(1000);
        Enumerator.setMinPrice(1);

        HashMap<String, String> nvargs = new HashMap<>();
        nvargs.put("$fileX", DATA_DIR + "SmallDenseX.csv");
        nvargs.put("$fileY", DATA_DIR + "SmallY.csv");
        nvargs.put("$fileW", DATA_DIR + "DummyOutputW.csv");
        runTest("L2SVM.dml", nvargs, Enumerator.OptimizationStrategy.MinCosts);
    }

    @Test
    public void L2SSVMSmallInputMinPriceTest() {
        Enumerator.setMinTime(1000);
        Enumerator.setMinPrice(1);

        HashMap<String, String> nvargs = new HashMap<>();
        nvargs.put("$fileX", DATA_DIR + "SmallDenseX.csv");
        nvargs.put("$fileY", DATA_DIR + "SmallY.csv");
        nvargs.put("$fileW", DATA_DIR + "DummyOutputW.csv");
        runTest("L2SVM.dml", nvargs, Enumerator.OptimizationStrategy.MinPrice);
    }

    @Test
    public void L2SSVMSmallInputMinTimeTest() {
        Enumerator.setMinTime(1000);
        Enumerator.setMinPrice(1);

        HashMap<String, String> nvargs = new HashMap<>();
        nvargs.put("$fileX", DATA_DIR + "SmallDenseX.csv");
        nvargs.put("$fileY", DATA_DIR + "SmallY.csv");
        nvargs.put("$fileW", DATA_DIR + "DummyOutputW.csv");
        runTest("L2SVM.dml", nvargs, Enumerator.OptimizationStrategy.MinTime);
    }

    @Test
    public void L2SSVMMediumInputMinCostsTest() {
        Enumerator.setMinTime(2000);
        Enumerator.setMinPrice(2);

        HashMap<String, String> nvargs = new HashMap<>();
        nvargs.put("$fileX", DATA_DIR + "MediumDenseX.csv");
        nvargs.put("$fileY", DATA_DIR + "MediumY.csv");
        nvargs.put("$fileW", DATA_DIR + "DummyOutputW.csv");
        runTest("L2SVM.dml", nvargs, Enumerator.OptimizationStrategy.MinCosts);
    }

    @Test
    public void L2SSVMMediumInputMinPriceTest() {
        Enumerator.setMinTime(2000);
        Enumerator.setMinPrice(2);

        HashMap<String, String> nvargs = new HashMap<>();
        nvargs.put("$fileX", DATA_DIR + "MediumDenseX.csv");
        nvargs.put("$fileY", DATA_DIR + "MediumY.csv");
        nvargs.put("$fileW", DATA_DIR + "DummyOutputW.csv");
        runTest("L2SVM.dml", nvargs, Enumerator.OptimizationStrategy.MinPrice);
    }

    @Test
    public void L2SSVMMediumInputMinTimeTest() {
        Enumerator.setMinTime(2000);
        Enumerator.setMinPrice(2);

        HashMap<String, String> nvargs = new HashMap<>();
        nvargs.put("$fileX", DATA_DIR + "MediumDenseX.csv");
        nvargs.put("$fileY", DATA_DIR + "MediumY.csv");
        nvargs.put("$fileW", DATA_DIR + "DummyOutputW.csv");
        runTest("L2SVM.dml", nvargs, Enumerator.OptimizationStrategy.MinTime);
    }

    @Test
    public void L2SSVMLargeInputMinCostsTest() {
        Enumerator.setMinTime(4000);
        Enumerator.setMinPrice(4);

        HashMap<String, String> nvargs = new HashMap<>();
        nvargs.put("$fileX", DATA_DIR + "LargeDenseX.csv");
        nvargs.put("$fileY", DATA_DIR + "LargeY.csv");
        nvargs.put("$fileW", DATA_DIR + "DummyOutputW.csv");
        runTest("L2SVM.dml", nvargs, Enumerator.OptimizationStrategy.MinCosts);
    }

    @Test
    public void L2SSVMLargeInputMinPriceTest() {
        Enumerator.setMinTime(4000);
        Enumerator.setMinPrice(4);

        HashMap<String, String> nvargs = new HashMap<>();
        nvargs.put("$fileX", DATA_DIR + "LargeDenseX.csv");
        nvargs.put("$fileY", DATA_DIR + "LargeY.csv");
        nvargs.put("$fileW", DATA_DIR + "DummyOutputW.csv");
        runTest("L2SVM.dml", nvargs, Enumerator.OptimizationStrategy.MinPrice);
    }

    @Test
    public void L2SSVMLargeInputMinTimeTest() {
        Enumerator.setMinTime(4000);
        Enumerator.setMinPrice(4);

        HashMap<String, String> nvargs = new HashMap<>();
        nvargs.put("$fileX", DATA_DIR + "LargeDenseX.csv");
        nvargs.put("$fileY", DATA_DIR + "LargeY.csv");
        nvargs.put("$fileW", DATA_DIR + "DummyOutputW.csv");
        runTest("L2SVM.dml", nvargs, Enumerator.OptimizationStrategy.MinTime);
    }

    // Linreg

    @Test
    public void LinregDefaultMinCostsTest() {
        Enumerator.setMinTime(1000);
        Enumerator.setMinPrice(1);

        HashMap<String, String> nvargs = new HashMap<>();
        nvargs.put("$fileX", DATA_DIR + "DefaultX.csv");
        nvargs.put("$fileY", DATA_DIR + "DefaultY.csv");
        nvargs.put("$fileW", DATA_DIR + "DummyOutputW.csv");
        runTest("Linreg.dml", nvargs, Enumerator.OptimizationStrategy.MinCosts);
    }

    @Test
    public void LinregDefaultMinPriceTest() {
        Enumerator.setMinTime(1000);
        Enumerator.setMinPrice(1);

        HashMap<String, String> nvargs = new HashMap<>();
        nvargs.put("$fileX", DATA_DIR + "DefaultX.csv");
        nvargs.put("$fileY", DATA_DIR + "DefaultY.csv");
        nvargs.put("$fileW", DATA_DIR + "DummyOutputW.csv");
        runTest("Linreg.dml", nvargs, Enumerator.OptimizationStrategy.MinPrice);
    }

    @Test
    public void LinregDefaultMinTimeTest() {
        Enumerator.setMinTime(1000);
        Enumerator.setMinPrice(1);

        HashMap<String, String> nvargs = new HashMap<>();
        nvargs.put("$fileX", DATA_DIR + "DefaultX.csv");
        nvargs.put("$fileY", DATA_DIR + "DefaultY.csv");
        nvargs.put("$fileW", DATA_DIR + "DummyOutputW.csv");
        runTest("Linreg.dml", nvargs, Enumerator.OptimizationStrategy.MinTime);
    }

    @Test
    public void LinregSmallDenseMinCostsTest() {
        Enumerator.setMinTime(1000);
        Enumerator.setMinPrice(1);

        HashMap<String, String> nvargs = new HashMap<>();
        nvargs.put("$fileX", DATA_DIR + "SmallDenseX.csv");
        nvargs.put("$fileY", DATA_DIR + "SmallY.csv");
        nvargs.put("$fileW", DATA_DIR + "DummyOutputW.csv");
        runTest("Linreg.dml", nvargs, Enumerator.OptimizationStrategy.MinCosts);
    }

    @Test
    public void LinregSmallDenseMinPriceTest() {
        Enumerator.setMinTime(1000);
        Enumerator.setMinPrice(1);

        HashMap<String, String> nvargs = new HashMap<>();
        nvargs.put("$fileX", DATA_DIR + "SmallDenseX.csv");
        nvargs.put("$fileY", DATA_DIR + "SmallY.csv");
        nvargs.put("$fileW", DATA_DIR + "DummyOutputW.csv");
        runTest("Linreg.dml", nvargs, Enumerator.OptimizationStrategy.MinPrice);
    }

    @Test
    public void LinregSmallDenseMinTimeTest() {
        Enumerator.setMinTime(1000);
        Enumerator.setMinPrice(1);

        HashMap<String, String> nvargs = new HashMap<>();
        nvargs.put("$fileX", DATA_DIR + "SmallDenseX.csv");
        nvargs.put("$fileY", DATA_DIR + "SmallY.csv");
        nvargs.put("$fileW", DATA_DIR + "DummyOutputW.csv");
        runTest("Linreg.dml", nvargs, Enumerator.OptimizationStrategy.MinTime);
    }

    @Test
    public void LinregMediumMinCostsTest() {
        Enumerator.setMinTime(2000);
        Enumerator.setMinPrice(2);

        HashMap<String, String> nvargs = new HashMap<>();
        nvargs.put("$fileX", DATA_DIR + "MediumDenseX.csv");
        nvargs.put("$fileY", DATA_DIR + "MediumY.csv");
        nvargs.put("$fileW", DATA_DIR + "DummyOutputW.csv");
        runTest("Linreg.dml", nvargs, Enumerator.OptimizationStrategy.MinCosts);
    }

    @Test
    public void LinregMediumMinPriceTest() {
        Enumerator.setMinTime(2000);
        Enumerator.setMinPrice(2);

        HashMap<String, String> nvargs = new HashMap<>();
        nvargs.put("$fileX", DATA_DIR + "MediumDenseX.csv");
        nvargs.put("$fileY", DATA_DIR + "MediumY.csv");
        nvargs.put("$fileW", DATA_DIR + "DummyOutputW.csv");
        runTest("Linreg.dml", nvargs, Enumerator.OptimizationStrategy.MinPrice);
    }

    @Test
    public void LinregMediumMinTimeTest() {
        Enumerator.setMinTime(2000);
        Enumerator.setMinPrice(2);

        HashMap<String, String> nvargs = new HashMap<>();
        nvargs.put("$fileX", DATA_DIR + "MediumDenseX.csv");
        nvargs.put("$fileY", DATA_DIR + "MediumY.csv");
        nvargs.put("$fileW", DATA_DIR + "DummyOutputW.csv");
        runTest("Linreg.dml", nvargs, Enumerator.OptimizationStrategy.MinTime);
    }

    @Test
    public void LinregLargeInputMinCostsTest() {
        Enumerator.setMinTime(4000);
        Enumerator.setMinPrice(4);

        HashMap<String, String> nvargs = new HashMap<>();
        nvargs.put("$fileX", DATA_DIR + "LargeDenseX.csv");
        nvargs.put("$fileY", DATA_DIR + "LargeY.csv");
        nvargs.put("$fileW", DATA_DIR + "DummyOutputW.csv");
        runTest("Linreg.dml", nvargs, Enumerator.OptimizationStrategy.MinCosts);
    }

    @Test
    public void LinregLargeInputMinPriceTest() {
        Enumerator.setMinTime(4000);
        Enumerator.setMinPrice(4);

        HashMap<String, String> nvargs = new HashMap<>();
        nvargs.put("$fileX", DATA_DIR + "LargeDenseX.csv");
        nvargs.put("$fileY", DATA_DIR + "LargeY.csv");
        nvargs.put("$fileW", DATA_DIR + "DummyOutputW.csv");
        runTest("Linreg.dml", nvargs, Enumerator.OptimizationStrategy.MinPrice);
    }

    @Test
    public void LinregLargeInputMinTimeTest() {
        Enumerator.setMinTime(4000);
        Enumerator.setMinPrice(4);

        HashMap<String, String> nvargs = new HashMap<>();
        nvargs.put("$fileX", DATA_DIR + "LargeDenseX.csv");
        nvargs.put("$fileY", DATA_DIR + "LargeY.csv");
        nvargs.put("$fileW", DATA_DIR + "DummyOutputW.csv");
        runTest("Linreg.dml", nvargs, Enumerator.OptimizationStrategy.MinTime);
    }

    // PCA
    @Test
    public void PCADefaultMinCostsTest() {
        Enumerator.setMinTime(1000);
        Enumerator.setMinPrice(1);

        HashMap<String, String> nvargs = new HashMap<>();
        nvargs.put("$fileX", DATA_DIR + "DefaultX.csv");
        nvargs.put("$fileXReduced", DATA_DIR + "DummyOutputX.csv");
        nvargs.put("$fileC", DATA_DIR + "DummyOutputC.csv");
        nvargs.put("$fileC2", DATA_DIR + "DummyOutputC2.csv");
        nvargs.put("$fileS2", DATA_DIR + "DummyOutputS2.csv");
        runTest("PCA.dml", nvargs, Enumerator.OptimizationStrategy.MinCosts);
    }

    @Test
    public void PCADefaultMinPriceTest() {
        Enumerator.setMinTime(1000);
        Enumerator.setMinPrice(1);

        HashMap<String, String> nvargs = new HashMap<>();
        nvargs.put("$fileX", DATA_DIR + "DefaultX.csv");
        nvargs.put("$fileXReduced", DATA_DIR + "DummyOutputX.csv");
        nvargs.put("$fileC", DATA_DIR + "DummyOutputC.csv");
        nvargs.put("$fileC2", DATA_DIR + "DummyOutputC2.csv");
        nvargs.put("$fileS2", DATA_DIR + "DummyOutputS2.csv");
        runTest("PCA.dml", nvargs, Enumerator.OptimizationStrategy.MinPrice);
    }

    @Test
    public void PCADefaultMinTimeTest() {
        Enumerator.setMinTime(1000);
        Enumerator.setMinPrice(1);

        HashMap<String, String> nvargs = new HashMap<>();
        nvargs.put("$fileX", DATA_DIR + "DefaultX.csv");
        nvargs.put("$fileXReduced", DATA_DIR + "DummyOutputX.csv");
        nvargs.put("$fileC", DATA_DIR + "DummyOutputC.csv");
        nvargs.put("$fileC2", DATA_DIR + "DummyOutputC2.csv");
        nvargs.put("$fileS2", DATA_DIR + "DummyOutputS2.csv");
        runTest("PCA.dml", nvargs, Enumerator.OptimizationStrategy.MinTime);
    }

    @Test
    public void PCASmallMinCostsTest() {
        Enumerator.setMinTime(1000);
        Enumerator.setMinPrice(1);

        HashMap<String, String> nvargs = new HashMap<>();
        nvargs.put("$fileX", DATA_DIR + "SmallDenseX.csv");
        nvargs.put("$fileXReduced", DATA_DIR + "DummyOutputX.csv");
        nvargs.put("$fileC", DATA_DIR + "DummyOutputC.csv");
        nvargs.put("$fileC2", DATA_DIR + "DummyOutputC2.csv");
        nvargs.put("$fileS2", DATA_DIR + "DummyOutputS2.csv");
        runTest("PCA.dml", nvargs, Enumerator.OptimizationStrategy.MinCosts);
    }

    @Test
    public void PCASmallMinPriceTest() {
        Enumerator.setMinTime(1000);
        Enumerator.setMinPrice(1);

        HashMap<String, String> nvargs = new HashMap<>();
        nvargs.put("$fileX", DATA_DIR + "SmallDenseX.csv");
        nvargs.put("$fileXReduced", DATA_DIR + "DummyOutputX.csv");
        nvargs.put("$fileC", DATA_DIR + "DummyOutputC.csv");
        nvargs.put("$fileC2", DATA_DIR + "DummyOutputC2.csv");
        nvargs.put("$fileS2", DATA_DIR + "DummyOutputS2.csv");
        runTest("PCA.dml", nvargs, Enumerator.OptimizationStrategy.MinPrice);
    }

    @Test
    public void PCASmallMinTimeTest() {
        Enumerator.setMinTime(1000);
        Enumerator.setMinPrice(1);

        HashMap<String, String> nvargs = new HashMap<>();
        nvargs.put("$fileX", DATA_DIR + "SmallDenseX.csv");
        nvargs.put("$fileXReduced", DATA_DIR + "DummyOutputX.csv");
        nvargs.put("$fileC", DATA_DIR + "DummyOutputC.csv");
        nvargs.put("$fileC2", DATA_DIR + "DummyOutputC2.csv");
        nvargs.put("$fileS2", DATA_DIR + "DummyOutputS2.csv");
        runTest("PCA.dml", nvargs, Enumerator.OptimizationStrategy.MinTime);
    }

    @Test
    public void PCAMediumMinCostsTest() {
        Enumerator.setMinTime(2000);
        Enumerator.setMinPrice(2);

        HashMap<String, String> nvargs = new HashMap<>();
        nvargs.put("$fileX", DATA_DIR + "MediumDenseX.csv");
        nvargs.put("$fileXReduced", DATA_DIR + "DummyOutputX.csv");
        nvargs.put("$fileC", DATA_DIR + "DummyOutputC.csv");
        nvargs.put("$fileC2", DATA_DIR + "DummyOutputC2.csv");
        nvargs.put("$fileS2", DATA_DIR + "DummyOutputS2.csv");
        runTest("PCA.dml", nvargs, Enumerator.OptimizationStrategy.MinCosts);
    }

    @Test
    public void PCAMediumMinPriceTest() {
        Enumerator.setMinTime(2000);
        Enumerator.setMinPrice(2);

        HashMap<String, String> nvargs = new HashMap<>();
        nvargs.put("$fileX", DATA_DIR + "MediumDenseX.csv");
        nvargs.put("$fileXReduced", DATA_DIR + "DummyOutputX.csv");
        nvargs.put("$fileC", DATA_DIR + "DummyOutputC.csv");
        nvargs.put("$fileC2", DATA_DIR + "DummyOutputC2.csv");
        nvargs.put("$fileS2", DATA_DIR + "DummyOutputS2.csv");
        runTest("PCA.dml", nvargs, Enumerator.OptimizationStrategy.MinPrice);
    }

    @Test
    public void PCAMediumMinTimeTest() {
        Enumerator.setMinTime(2000);
        Enumerator.setMinPrice(2);

        HashMap<String, String> nvargs = new HashMap<>();
        nvargs.put("$fileX", DATA_DIR + "MediumDenseX.csv");
        nvargs.put("$fileXReduced", DATA_DIR + "DummyOutputX.csv");
        nvargs.put("$fileC", DATA_DIR + "DummyOutputC.csv");
        nvargs.put("$fileC2", DATA_DIR + "DummyOutputC2.csv");
        nvargs.put("$fileS2", DATA_DIR + "DummyOutputS2.csv");
        runTest("PCA.dml", nvargs, Enumerator.OptimizationStrategy.MinTime);
    }

    @Test
    public void PCALargeInputMinCostsTest() {
        Enumerator.setMinTime(4000);
        Enumerator.setMinPrice(4);

        HashMap<String, String> nvargs = new HashMap<>();
        nvargs.put("$fileX", DATA_DIR + "LargeDenseX.csv");
        nvargs.put("$fileXReduced", DATA_DIR + "DummyOutputX.csv");
        nvargs.put("$fileC", DATA_DIR + "DummyOutputC.csv");
        nvargs.put("$fileC2", DATA_DIR + "DummyOutputC2.csv");
        nvargs.put("$fileS2", DATA_DIR + "DummyOutputS2.csv");
        runTest("PCA.dml", nvargs, Enumerator.OptimizationStrategy.MinCosts);
    }

    @Test
    public void PCALargeInputMinPriceTest() {
        Enumerator.setMinTime(4000);
        Enumerator.setMinPrice(4);

        HashMap<String, String> nvargs = new HashMap<>();
        nvargs.put("$fileX", DATA_DIR + "LargeDenseX.csv");
        nvargs.put("$fileXReduced", DATA_DIR + "DummyOutputX.csv");
        nvargs.put("$fileC", DATA_DIR + "DummyOutputC.csv");
        nvargs.put("$fileC2", DATA_DIR + "DummyOutputC2.csv");
        nvargs.put("$fileS2", DATA_DIR + "DummyOutputS2.csv");
        runTest("PCA.dml", nvargs, Enumerator.OptimizationStrategy.MinPrice);
    }

    @Test
    public void PCALargeInputMinTimeTest() {
        Enumerator.setMinTime(4000);
        Enumerator.setMinPrice(4);

        HashMap<String, String> nvargs = new HashMap<>();
        nvargs.put("$fileX", DATA_DIR + "LargeDenseX.csv");
        nvargs.put("$fileXReduced", DATA_DIR + "DummyOutputX.csv");
        nvargs.put("$fileC", DATA_DIR + "DummyOutputC.csv");
        nvargs.put("$fileC2", DATA_DIR + "DummyOutputC2.csv");
        nvargs.put("$fileS2", DATA_DIR + "DummyOutputS2.csv");
        runTest("PCA.dml", nvargs, Enumerator.OptimizationStrategy.MinTime);
    }

    // PNMF

    @Test
    public void PNMFDefaultMinCostsTest() {
        Enumerator.setMinTime(1000);
        Enumerator.setMinPrice(1);

        HashMap<String, String> nvargs = new HashMap<>();
        nvargs.put("$fileX", DATA_DIR + "DefaultX.csv");
        nvargs.put("$fileW", DATA_DIR + "DummyOutputW.csv");
        nvargs.put("$fileH", DATA_DIR + "DummyOutputH.csv");
        runTest("PNMF.dml", nvargs, Enumerator.OptimizationStrategy.MinCosts);
    }

    @Test
    public void PNMFDefaultMinPriceTest() {
        Enumerator.setMinTime(1000);
        Enumerator.setMinPrice(1);

        HashMap<String, String> nvargs = new HashMap<>();
        nvargs.put("$fileX", DATA_DIR + "DefaultX.csv");
        nvargs.put("$fileW", DATA_DIR + "DummyOutputW.csv");
        nvargs.put("$fileH", DATA_DIR + "DummyOutputH.csv");
        runTest("PNMF.dml", nvargs, Enumerator.OptimizationStrategy.MinPrice);
    }

    @Test
    public void PNMFDefaultMinTimeTest() {
        Enumerator.setMinTime(1000);
        Enumerator.setMinPrice(1);

        HashMap<String, String> nvargs = new HashMap<>();
        nvargs.put("$fileX", DATA_DIR + "DefaultX.csv");
        nvargs.put("$fileW", DATA_DIR + "DummyOutputW.csv");
        nvargs.put("$fileH", DATA_DIR + "DummyOutputH.csv");
        runTest("PNMF.dml", nvargs, Enumerator.OptimizationStrategy.MinTime);
    }

    @Test
    public void PNMFSmallMinCostsTest() {
        Enumerator.setMinTime(1000);
        Enumerator.setMinPrice(1);

        HashMap<String, String> nvargs = new HashMap<>();
        nvargs.put("$fileX", DATA_DIR + "SmallDenseX.csv");
        nvargs.put("$fileW", DATA_DIR + "DummyOutputW.csv");
        nvargs.put("$fileH", DATA_DIR + "DummyOutputH.csv");
        runTest("PNMF.dml", nvargs, Enumerator.OptimizationStrategy.MinCosts);
    }

    @Test
    public void PNMFSmallMinPriceTest() {
        Enumerator.setMinTime(1000);
        Enumerator.setMinPrice(1);

        HashMap<String, String> nvargs = new HashMap<>();
        nvargs.put("$fileX", DATA_DIR + "SmallDenseX.csv");
        nvargs.put("$fileW", DATA_DIR + "DummyOutputW.csv");
        nvargs.put("$fileH", DATA_DIR + "DummyOutputH.csv");
        runTest("PNMF.dml", nvargs, Enumerator.OptimizationStrategy.MinPrice);
    }

    @Test
    public void PNMFSmallMinTimeTest() {
        Enumerator.setMinTime(1000);
        Enumerator.setMinPrice(1);

        HashMap<String, String> nvargs = new HashMap<>();
        nvargs.put("$fileX", DATA_DIR + "SmallDenseX.csv");
        nvargs.put("$fileW", DATA_DIR + "DummyOutputW.csv");
        nvargs.put("$fileH", DATA_DIR + "DummyOutputH.csv");
        runTest("PNMF.dml", nvargs, Enumerator.OptimizationStrategy.MinTime);
    }

    @Test
    public void PNMFMediumMinCostsTest() {
        Enumerator.setMinTime(2000);
        Enumerator.setMinPrice(2);

        HashMap<String, String> nvargs = new HashMap<>();
        nvargs.put("$fileX", DATA_DIR + "MediumDenseX.csv");
        nvargs.put("$fileW", DATA_DIR + "DummyOutputW.csv");
        nvargs.put("$fileH", DATA_DIR + "DummyOutputH.csv");
        runTest("PNMF.dml", nvargs, Enumerator.OptimizationStrategy.MinCosts);
    }

    @Test
    public void PNMFMediumMinPriceTest() {
        Enumerator.setMinTime(2000);
        Enumerator.setMinPrice(2);

        HashMap<String, String> nvargs = new HashMap<>();
        nvargs.put("$fileX", DATA_DIR + "MediumDenseX.csv");
        nvargs.put("$fileW", DATA_DIR + "DummyOutputW.csv");
        nvargs.put("$fileH", DATA_DIR + "DummyOutputH.csv");
        runTest("PNMF.dml", nvargs, Enumerator.OptimizationStrategy.MinPrice);
    }

    @Test
    public void PNMFMediumMinTimeTest() {
        Enumerator.setMinTime(2000);
        Enumerator.setMinPrice(2);

        HashMap<String, String> nvargs = new HashMap<>();
        nvargs.put("$fileX", DATA_DIR + "MediumDenseX.csv");
        nvargs.put("$fileW", DATA_DIR + "DummyOutputW.csv");
        nvargs.put("$fileH", DATA_DIR + "DummyOutputH.csv");
        runTest("PNMF.dml", nvargs, Enumerator.OptimizationStrategy.MinTime);
    }

    @Test
    public void PNMFLargeInputMinCostsTest() {
        Enumerator.setMinTime(4000);
        Enumerator.setMinPrice(4);

        HashMap<String, String> nvargs = new HashMap<>();
        nvargs.put("$fileX", DATA_DIR + "LargeDenseX.csv");
        nvargs.put("$fileW", DATA_DIR + "DummyOutputW.csv");
        nvargs.put("$fileH", DATA_DIR + "DummyOutputH.csv");
        runTest("PNMF.dml", nvargs, Enumerator.OptimizationStrategy.MinCosts);
    }

    @Test
    public void PNMFLargeInputMinPriceTest() {
        Enumerator.setMinTime(4000);
        Enumerator.setMinPrice(4);

        HashMap<String, String> nvargs = new HashMap<>();
        nvargs.put("$fileX", DATA_DIR + "LargeDenseX.csv");
        nvargs.put("$fileW", DATA_DIR + "DummyOutputW.csv");
        nvargs.put("$fileH", DATA_DIR + "DummyOutputH.csv");
        runTest("PNMF.dml", nvargs, Enumerator.OptimizationStrategy.MinPrice);
    }

    @Test
    public void PNMFLargeInputMinTimeTest() {
        Enumerator.setMinTime(4000);
        Enumerator.setMinPrice(4);

        HashMap<String, String> nvargs = new HashMap<>();
        nvargs.put("$fileX", DATA_DIR + "LargeDenseX.csv");
        nvargs.put("$fileW", DATA_DIR + "DummyOutputW.csv");
        nvargs.put("$fileH", DATA_DIR + "DummyOutputH.csv");
        runTest("PNMF.dml", nvargs, Enumerator.OptimizationStrategy.MinTime);
    }

    // Helpers ---------------------------------------------------------------------------------------------------------

    private void runTest(String scriptFilename, HashMap<String, String> args, Enumerator.OptimizationStrategy objectiveFunction) {
        System.out.println("Comparing Grid- vs Pune-based Enumeration strategies with optimization for "
                + objectiveFunction);
        long startTime, endTime;
        Program program = ResourceTestUtils.compileProgramWithNvargs(SCRIPTS_DIR + scriptFilename, args);
        // grid-based enumerator for expected value generation
        GridBasedEnumerator gridEnumerator = (GridBasedEnumerator) (new Enumerator.Builder())
                .withRuntimeProgram(program)
                .withAvailableInstances(allInstances)
                .withEnumerationStrategy(Enumerator.EnumerationStrategy.GridBased)
                .withOptimizationStrategy(objectiveFunction)
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
