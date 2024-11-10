package external;

import org.apache.sysds.api.DMLScript;
import org.apache.sysds.conf.ConfigurationManager;
import org.apache.sysds.parser.DMLProgram;
import org.apache.sysds.parser.DMLTranslator;
import org.apache.sysds.parser.ParserFactory;
import org.apache.sysds.parser.ParserWrapper;
import org.apache.sysds.resource.enumeration.EnumerationUtils;
import org.apache.sysds.runtime.controlprogram.Program;
import org.junit.Assert;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;

public class ResourceTestUtils {
    public static final String DEFAULT_INSTANCE_INFO_TABLE = "./ec2_stats.csv";
//    public static final String DEFAULT_INSTANCE_INFO_TABLE = "scaled_ec2_stats.csv";

    public static Program compileProgramWithNvargs(String scriptPath, HashMap<String, String> nvargs) {
        Program returnProgram;
        try {
            //read script
            StringBuilder dmlScriptString = new StringBuilder();
            try (BufferedReader in = new BufferedReader(new FileReader(scriptPath))) {
                String s1;
                while ((s1 = in.readLine()) != null)
                    dmlScriptString.append(s1).append("\n");
            }

            //simplified compilation chain
            ParserWrapper parser = ParserFactory.createParser();
            DMLProgram prog = parser.parse(DMLScript.DML_FILE_PATH_ANTLR_PARSER, dmlScriptString.toString(), nvargs);
            DMLTranslator dmlt = new DMLTranslator(prog);
            dmlt.liveVariableAnalysis(prog);
            dmlt.validateParseTree(prog);
            dmlt.constructHops(prog);
            dmlt.rewriteHopsDAG(prog);
            dmlt.constructLops(prog);
            returnProgram = dmlt.getRuntimeProgram(prog, ConfigurationManager.getDMLConfig());
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Script compilation failed");
        }
        return returnProgram;
    }

    public static void assertEqualSolutionPoints(EnumerationUtils.SolutionPoint expected, EnumerationUtils.SolutionPoint actual) {
        System.out.printf("Expected solution (%f.1s, %f.2$) with configurations:\n" + expected.toString() + "%n",
                expected.getTimeCost(), expected.getMonetaryCost());
        System.out.println();
        System.out.printf("Actual solution (%f.1s, %f.2$) with configurations:\n"+actual.toString(),
                actual.getTimeCost(), actual.getMonetaryCost());
        Assert.assertEquals(expected.driverInstance.getInstanceName(), actual.driverInstance.getInstanceName());
        Assert.assertEquals(expected.numberExecutors, actual.numberExecutors);
        if (expected.numberExecutors > 0)
            Assert.assertEquals(expected.executorInstance.getInstanceName(), actual.executorInstance.getInstanceName());
        Assert.assertEquals(expected.getTimeCost(), actual.getTimeCost(), 0);
        Assert.assertEquals(expected.getMonetaryCost(), actual.getMonetaryCost(), 0);
    }
}
