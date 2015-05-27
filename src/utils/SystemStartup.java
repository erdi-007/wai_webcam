package utils;

import java.io.File;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import utils.JndiFactory;

public final class SystemStartup implements ServletContextListener {

	private static Logger jLog = Logger.getLogger(SystemStartup.class);

	public void contextInitialized(ServletContextEvent event) {
		JndiFactory jndiFactory = JndiFactory.getInstance();

		try {
			// Initialize Logging
			File logConfigFile = new File(jndiFactory.getEnvironmentAsString("projectPath")
					  					+ jndiFactory.getEnvironmentAsString("configPath")
					  					+ "/log4j.properties");

			if (!logConfigFile.exists()) {
				System.out.println("ERROR: Logging configuration file not found: " 
								  + jndiFactory.getEnvironmentAsString("projectPath")
								  + jndiFactory.getEnvironmentAsString("configPath")
								  + "/log4j.properties");
			} else {
				String logpath = logConfigFile.getPath();
				PropertyConfigurator.configure(logpath);
			}
			jLog.info("ContextInitialized");
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void contextDestroyed(ServletContextEvent event) {
		jLog.info("ContextDestroyed");
	}

}
