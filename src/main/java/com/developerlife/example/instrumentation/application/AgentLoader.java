/*
 * Copyright 2020 Nazmul Idris. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.developerlife.example.instrumentation.application;

import com.sun.tools.attach.VirtualMachine;
import com.sun.tools.attach.VirtualMachineDescriptor;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.List;

public class AgentLoader {
private static final Logger LOGGER = LoggerFactory.getLogger(AgentLoader.class.getSimpleName());

public static void main() {
  // Iterate all JVMs and get the first one that matches our application name.
  List<VirtualMachineDescriptor> vms = VirtualMachine.list();
  vms.forEach(vmd -> {
    LOGGER.info("VM name '" + StringUtils.left(vmd.displayName(), 80) + "', id: '" + vmd.id() + "'");
  });

  String vmSearchTerm = AppConstants.APP_NAME + " " + AppConstants.CMD_START;

  LOGGER.info("Looking for this in display name: " + vmSearchTerm);

  boolean isPresent = false;
  String jvmPid = null;
  String jvmName = null;

  for (VirtualMachineDescriptor vmd : vms) {
    String displayName = vmd.displayName();
    if (displayName.contains(vmSearchTerm)) {
      jvmName = displayName;
      isPresent = true;
      jvmPid = vmd.id();
      LOGGER.info("VM found with search term '" + vmSearchTerm + "'" +
                  "\n\tpid: " + jvmPid +
                  "\n\tdisplayName: " + StringUtils.left(jvmName, 80));
      break;
    }
  }

  if (!isPresent) {
    LOGGER.error("Target application not found");
    return;
  }

  String currentDirectory = System.getProperty("user.dir");
  LOGGER.info("The current working directory is " + currentDirectory);
  File agentFile = new File(currentDirectory + AppConstants.AGENT_FILE_PATH);

  if (!agentFile.exists()) {
    LOGGER.error("Agent JAR file not found: " + agentFile.getAbsolutePath());
    return;
  } else {
    LOGGER.info("Agent JAR file found: " + agentFile.getAbsolutePath());
  }

  try {
    LOGGER.info("Attaching to target JVM with PID: " + jvmPid);
    VirtualMachine jvm = VirtualMachine.attach(jvmPid);
    jvm.loadAgent(agentFile.getAbsolutePath());
    jvm.detach();
    LOGGER.info("Attached to target JVM and loaded Java agent successfully");
  } catch (Exception e) {
    throw new RuntimeException(e);
  }
}

}
