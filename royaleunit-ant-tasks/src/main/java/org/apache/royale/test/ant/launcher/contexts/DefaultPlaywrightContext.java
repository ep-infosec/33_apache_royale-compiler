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
package org.apache.royale.test.ant.launcher.contexts;

import java.io.IOException;

import org.apache.royale.test.ant.launcher.commands.playwright.PlaywrightCommand;
import org.apache.tools.ant.AntClassLoader;
import org.apache.tools.ant.Project;

import com.microsoft.playwright.Playwright;

public class DefaultPlaywrightContext implements PlaywrightExecutionContext
{
    private PlaywrightCommand command;
    
    @SuppressWarnings("unused")
    private Project project;
    
    public void setProject(Project project)
    {
        this.project = project;
    }

    public void setCommand(PlaywrightCommand command)
    {
        this.command = command;
    }

    public void start() throws IOException
    {
        // calls resetThreadContextLoader() in stop() after test run completes
        ((AntClassLoader)getClass().getClassLoader()).setThreadContextLoader();
       
        //prep anything the command needs to run
        command.prepare();
    }

    public void stop(Playwright playwright) throws IOException
    {
        //destroy the playwright instance, if it exists
        if(playwright != null)
        {
            // setThreadContextLoader() was called in start()
            ((AntClassLoader)getClass().getClassLoader()).resetThreadContextLoader();

            playwright.close();
        }
    }
}
