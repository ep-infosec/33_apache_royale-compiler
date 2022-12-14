/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.royale.test.ant.tasks;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.royale.test.ant.tasks.configuration.TaskConfiguration;
import org.apache.royale.test.ant.tasks.types.LoadConfig;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.DynamicAttribute;
import org.apache.tools.ant.DynamicElement;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.types.FileSet;

public class RoyaleUnitTask extends Task implements DynamicElement, DynamicAttribute
{
	private static final Pattern COMMAND_ARGS_PATTERN = Pattern.compile(
			"([\\-+]([^\\s]+\\+?=)(('([^'])*')|(\"([^\"])*\")|([^\\s\"',]+))(,(('([^'])*')|(\"([^\"])*\")|([^\\s\"']+)))*)|('([^'])*')|(\"([^\"])*\")|([^\\s\"']+)");

    private TaskConfiguration configuration;

    public RoyaleUnitTask()
    {
    }
    
    @Override
    public void setProject(Project project)
    {
        super.setProject(project);
        configuration = new TaskConfiguration(project);
    }
    
    /**
     * Sets local trusted, default is false
     * 
     * @param localTrusted
     */
    public void setLocalTrusted(final boolean localTrusted)
    {
        configuration.setLocalTrusted(localTrusted);
    }

    /**
     * Set the port to receive the test results on. Default is 1024
     * 
     * @param serverPort
     *           the port to set.
     */
    public void setPort(final int serverPort)
    {
        configuration.setPort(serverPort);
    }

    /**
     * Set the timeout for receiving the royaleunit report.
     * 
     * @param timeout
     *           in milliseconds.
     */
    public void setTimeout(final int timeout)
    {
        configuration.setSocketTimeout(timeout);
    }

    /**
     * The buffer size the {@RoyaleUnitSocketServer} uses
     * for its inbound data stream.
     */
    public void setBuffer(final int size)
    {
        configuration.setServerBufferSize(size);
    }

    /**
     * The SWF for the RoyaleUnit tests to run.
     * 
     * @param testSWF
     *           the SWF to set.
     */
    public void setSWF(final String testSWF)
    {
        configuration.setSwf(testSWF);
    }

    /**
     * Set the directory to output the test reports to.
     * 
     * @param toDir
     *           the directory to set.
     */
    public void setToDir(final String toDir)
    {
        configuration.setReportDir(toDir);
    }

    /**
     * Should we fail the build if the royale tests fail?
     * 
     * @param fail
     */
    public void setHaltonfailure(final boolean fail)
    {
        configuration.setFailOnTestFailure(fail);
    }

    /**
     * Custom ant property noting test failure
     * 
     * @param failprop
     */
    public void setFailureproperty(final String failprop)
    {
        configuration.setFailureProperty(failprop);
    }

    /**
     * Toggle display of descriptive messages
     * 
     * @param verbose
     */
    public void setVerbose(final boolean verbose)
    {
        configuration.setVerbose(verbose);
    }

    public void setPlayer(String player)
    {
        configuration.setPlayer(player);
    }

    public void setCommand(String executableFilePath)
    {
        configuration.setCommand(executableFilePath);
    }

    public void setHeadless(boolean headless)
    {
        configuration.setHeadless(headless);
    }

    public void setDisplay(int number)
    {
        configuration.setDisplay(number);
    }
    
    public void addSource(FileSet fileset)
    {
        configuration.addSource(fileset);
    }
    
    public void addTestSource(FileSet fileset)
    {
        configuration.addTestSource(fileset);
    }
    
    public void addLibrary(FileSet fileset)
    {
        configuration.addLibrary(fileset);
    }
    
    public void setWorkingDir(String workingDirPath)
    {
        configuration.setWorkingDir(workingDirPath);
    }
    
    /**
     * Called by Ant to execute the task.
     */
    public void execute() throws BuildException
    {
        //verify entire configuration
        configuration.verify();
        
        //compile tests if necessary
        if(configuration.shouldCompile())
        {
            Compilation compilation = new Compilation(getProject(), configuration.getCompilationConfiguration());
            configuration.setSwf(compilation.compile());
        }
        
        //executes tests
        TestRun testRun = new TestRun(getProject(), configuration.getTestRunConfiguration());
        testRun.run();
    }
    
    public void setDebug(boolean value)
    {
         configuration.setDebug(value);
    }

    public Object createDynamicElement(String arg0) throws BuildException 
    {
        if("load-config".equals(arg0))
        {
            LoadConfig loadconfig = new LoadConfig();
            configuration.setLoadConfig(loadconfig);
            return loadconfig;
        }
        else
        {
            throw new BuildException( "The <royaleUnit> type doesn't support the " + arg0 + " nested element");
        }
    }

    public void setDynamicAttribute(String arg0, String arg1) throws BuildException
    {
        if("commandargs".equals(arg0))
        {
            if (arg1.length() > 0)
            {
                String[] commandArgs = parseCommandArgs(arg1);
                configuration.setCommandArgs(commandArgs);
            }
            else
            {
                configuration.setCommandArgs(null);
            }
        }
        else
        {	
            throw new BuildException( "The <royaleUnit> type doesn't support the " + arg0 + " attribute");
        }
    }

    private String[] parseCommandArgs(String combined)
    {
        List<String> result = new ArrayList<String>();
        Matcher matcher = COMMAND_ARGS_PATTERN.matcher(combined);
        while (matcher.find())
        {
            String option = matcher.group();
            result.add(option);
        }
        return result.toArray(new String[0]);
    }
}
