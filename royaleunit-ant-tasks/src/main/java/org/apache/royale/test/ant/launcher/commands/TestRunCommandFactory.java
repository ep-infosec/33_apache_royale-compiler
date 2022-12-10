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
package org.apache.royale.test.ant.launcher.commands;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import org.apache.royale.test.ant.launcher.OperatingSystem;
import org.apache.royale.test.ant.launcher.commands.player.AdlCommand;
import org.apache.royale.test.ant.launcher.commands.player.CustomPlayerCommand;
import org.apache.royale.test.ant.launcher.commands.player.DefaultPlayerCommand;
import org.apache.royale.test.ant.launcher.commands.player.FlashPlayerCommand;
import org.apache.royale.test.ant.launcher.commands.playwright.DefaultPlaywrightCommand;
import org.apache.royale.test.ant.launcher.commands.playwright.PlaywrightCommand;
import org.apache.royale.test.ant.launcher.platforms.LinuxDefaults;
import org.apache.royale.test.ant.launcher.platforms.MacOSXDefaults;
import org.apache.royale.test.ant.launcher.platforms.WindowsDefaults;

public class TestRunCommandFactory
{
    private static final List<String> VALID_PLAYWRIGHT_PLAYERS = Arrays.asList(new String[]{"html", "chromium", "firefox", "webkit"});

    /**
     * Factory method to create the appropriate player and provide it with a set of defaults for
     * the executing platform.
     * 
     * @param os
     * @param player  "flash" or "air"
     * @param customCommand
     * @param customCommandArgs
     * @param localTrusted
     * @return Desired player command with platform defaults possibly wrapped in a custom command
     */
    public static TestRunCommand createCommand(OperatingSystem os,
        String player, File customCommand, String[] customCommandArgs,
        boolean localTrusted)
    {
        TestRunCommand newInstance = null;
        
        //choose runtime
        if (customCommand == null && VALID_PLAYWRIGHT_PLAYERS.contains(player))
        {
            PlaywrightCommand playwrightCommand = new DefaultPlaywrightCommand();
            playwrightCommand.setBrowser(player);
            newInstance = playwrightCommand;
        }
        else
        {
            DefaultPlayerCommand defaultInstance = null;

            if (!player.equals("air"))
            {
                FlashPlayerCommand fpCommand = new FlashPlayerCommand();
                fpCommand.setLocalTrusted(localTrusted);
                defaultInstance = fpCommand;
            }
            else
            {
                defaultInstance = new AdlCommand();
            }

            //set defaults
            if (os.equals(OperatingSystem.WINDOWS))
            {
                defaultInstance.setDefaults(new WindowsDefaults());
            }
            else if(os.equals(OperatingSystem.MACOSX))
            {
                defaultInstance.setDefaults(new MacOSXDefaults());
            }
            else
            {
                defaultInstance.setDefaults(new LinuxDefaults());
            }

            //if a custom command has been provided, use it to wrap the default command
            if(customCommand != null)
            {
                CustomPlayerCommand customInstance = new CustomPlayerCommand();
                customInstance.setProxiedCommand(defaultInstance);
                customInstance.setExecutable(customCommand);
                customInstance.setExecutableArgs(customCommandArgs);
                newInstance = customInstance;
            }
            else
            {
                newInstance = defaultInstance;
            }
        }
        
        return newInstance;
    }
}
