package org.apache.ibatis.ibator.ant;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.ibator.api.Ibator;
import org.apache.ibatis.ibator.config.IbatorConfiguration;
import org.apache.ibatis.ibator.config.xml.IbatorConfigurationParser;
import org.apache.ibatis.ibator.internal.DefaultShellCallback;

public class _Ibator_Run_Test {

    public static void main(String... strings) {
	try {
	    List<String> warnings = new ArrayList<String>();
	    boolean overwrite = true;
	    File configFile = new File(ClassLoader.getSystemResource(
	    // "ConfigIbatis.xml").getFile());
		    "ConfigIbatisOracle3.xml").getFile());
	    IbatorConfigurationParser cp = new IbatorConfigurationParser(
		    warnings);
	    IbatorConfiguration config = cp
		    .parseIbatorConfiguration(configFile);
	    DefaultShellCallback callback = new DefaultShellCallback(overwrite);
	    Ibator ibator = new Ibator(config, callback, warnings);
	    ibator.generate(null);
	    for (String warning : warnings) {
		System.out.println("warning:" + warning);
	    }
	} catch (Exception ex) {
	    ex.printStackTrace();
	}
    }

}
