package utils;

import java.io.IOException;
import java.text.DateFormat;

import javax.naming.NamingException;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import org.quartz.Scheduler;
import org.quartz.Trigger;
import org.quartz.impl.StdSchedulerFactory;

import utils.JndiFactory;

public class QuartzInitializerServlet extends HttpServlet {
    
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static Logger mLog = Logger.getLogger(QuartzInitializerServlet.class);

    private boolean performShutdown = true;

    private Scheduler scheduler = null;

    /*
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     * 
     * Interface.
     * 
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     */

    public void init(ServletConfig cfg) throws javax.servlet.ServletException {
        

        String[] jobGroups;
        String[] jobsInGroup;
        int i;
        int j;
        
        
        super.init(cfg);

        StdSchedulerFactory factory;
        try {
            if (JndiFactory.getInstance().getEnvironmentAsBoolean("wai_webcam").booleanValue() == true)
            {
                mLog.info ("Quartz Initializer Servlet loaded, initializing Scheduler...");

	            String configFile = JndiFactory.getInstance().getEnvironmentAsString("projectPath")
	    						  + JndiFactory.getInstance().getEnvironmentAsString("configPath")
	    						  + "/quartz.properties";
	            
	            // get Properties
                factory = new StdSchedulerFactory(configFile);
	
	            // Always want to get the scheduler, even if it isn't starting,
	            // to make sure it is both initialized and registered.
	            scheduler = factory.getScheduler();
	
	            // Should the Scheduler being started now or later
	            String startOnLoad = cfg.getInitParameter("start-scheduler-on-load");
	            /*
	             * If the "start-scheduler-on-load" init-parameter is not
	             * specified, the scheduler will be started. This is to maintain
	             * backwards compatability.
	             */
	            if (startOnLoad == null || (Boolean.valueOf(startOnLoad).booleanValue())) {
	                // Start now
	                scheduler.start();
	                mLog.info("Scheduler has been started...");
	            } else {
	                mLog.info("Scheduler has not been started. Use scheduler.start()");
	            }
	
	            String shutdownPref = cfg.getInitParameter("shutdown-on-unload");
	            if (shutdownPref != null) {
	                performShutdown = Boolean.valueOf(shutdownPref).booleanValue();
	            }

	            if (mLog.isInfoEnabled())
	            {
		            jobGroups = scheduler.getJobGroupNames();
		            for (i = 0; i < jobGroups.length; i++) {
		                mLog.info("Group: " + jobGroups[i] + " contains the following jobs");
		                jobsInGroup = scheduler.getJobNames(jobGroups[i]);
		
		                for (j = 0; j < jobsInGroup.length; j++) {
		                    mLog.info("- " + jobsInGroup[j]);
		                    Trigger[] triggers = scheduler.getTriggersOfJob(jobsInGroup[j], jobGroups[i]);
		                    for (int k = 0; k < triggers.length; k++) {
		                        Trigger trigger = triggers[k];
		                        mLog.info("next Execution at: " + DateFormat.getDateTimeInstance().format(trigger.getNextFireTime().getTime()));
		                    }
		
		                }
		            }
	            }
            }
        } catch (NamingException e) {
            log("Quartz Scheduler failed to initialize: " + e.toString());
            e.printStackTrace();
            throw new ServletException(e);
        } catch (Exception e) {
            log("Quartz Scheduler failed: " + e.toString());
            e.printStackTrace();
            throw new ServletException(e);
    	}
    }

    public void destroy() {

        if (!performShutdown) {
            return;
        }

        try {
            if (scheduler != null) {
                scheduler.shutdown();
            }
        } catch (Exception e) {
            mLog.info("Quartz Scheduler failed to shutdown cleanly: " + e.toString());
            e.printStackTrace();
        }

        mLog.info("Quartz Scheduler successful shutdown.");
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.sendError(HttpServletResponse.SC_FORBIDDEN);
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.sendError(HttpServletResponse.SC_FORBIDDEN);
    }

}
