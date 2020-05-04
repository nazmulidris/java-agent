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

package com.developerlife.example.instrumentation.agent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.instrument.Instrumentation;

public class Agent {
private static final Logger LOGGER = LoggerFactory.getLogger(Agent.class.getSimpleName());

public static void premain(String agentArgs, Instrumentation inst) {
  LOGGER.info("[Agent] In premain method");

  transformClass(AgentConstants.MY_ATM_CLASS_NAME, inst);
}

public static void agentmain(String agentArgs, Instrumentation inst) {
  LOGGER.info("[Agent] In agentmain method");

  transformClass(AgentConstants.MY_ATM_CLASS_NAME, inst);
}

private static void transformClass(String className, Instrumentation instrumentation) {
  Class<?> targetCls = null;
  ClassLoader targetClassLoader = null;
  // see if we can get the class using forName
  try {
    targetCls = Class.forName(className);
    targetClassLoader = targetCls.getClassLoader();
    transform(targetCls, targetClassLoader, instrumentation);
    return;
  } catch (Exception ex) {
    LOGGER.error("Class [{}] not found with Class.forName");
  }
  // otherwise iterate all loaded classes and find what we want
  for (Class<?> clazz : instrumentation.getAllLoadedClasses()) {
    if (clazz.getName().equals(className)) {
      targetCls = clazz;
      targetClassLoader = targetCls.getClassLoader();
      transform(targetCls, targetClassLoader, instrumentation);
      return;
    }
  }
  throw new RuntimeException("Failed to find class [" + className + "]");
}

private static void transform(Class<?> clazz, ClassLoader classLoader, Instrumentation instrumentation) {
  AtmTransformer dt = new AtmTransformer(clazz.getName(), classLoader);
  instrumentation.addTransformer(dt, true);
  try {
    instrumentation.retransformClasses(clazz);
  } catch (Exception ex) {
    throw new RuntimeException("Transform failed for class: [" + clazz.getName() + "]", ex);
  }
}

}
