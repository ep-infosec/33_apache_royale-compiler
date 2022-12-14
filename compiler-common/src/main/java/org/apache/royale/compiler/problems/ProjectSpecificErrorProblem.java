/*
 *
 *  Licensed to the Apache Software Foundation (ASF) under one or more
 *  contributor license agreements.  See the NOTICE file distributed with
 *  this work for additional information regarding copyright ownership.
 *  The ASF licenses this file to You under the Apache License, Version 2.0
 *  (the "License"); you may not use this file except in compliance with
 *  the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */
package org.apache.royale.compiler.problems;

import org.apache.royale.compiler.common.ISourceLocation;
import org.apache.royale.compiler.problems.annotations.DefaultSeverity;

/**
 *  A generic problem type for problems that can be specific to a particular target.
 **/
@DefaultSeverity(CompilerProblemSeverity.ERROR)
public class ProjectSpecificErrorProblem extends CompilerProblem
{
    public static final String DESCRIPTION =
        "${description}. This is a problem is specific to compilation for ${projectTypeDetail}.";

    public static final int errorCode = 10000;

    public ProjectSpecificErrorProblem(ISourceLocation site, String description, String projectTypeDetail)
    {
        super(site);
        this.description = description;
        this.projectTypeDetail = projectTypeDetail;
    }

    public ProjectSpecificErrorProblem(String sourcePath, int start, int end, int line, int column, int endLine, int endColumn, String description, String projectTypeDetail)
    {
        super(sourcePath, start, end, line, column, endLine, endColumn);
        this.description = description;
        this.projectTypeDetail = projectTypeDetail;
    }
    public final String description;
    public final String projectTypeDetail;
}
