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

import javassist.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.instrument.ClassFileTransformer;
import java.lang.instrument.IllegalClassFormatException;
import java.security.ProtectionDomain;

public class AtmTransformer implements ClassFileTransformer {

private static final Logger      LOGGER = LoggerFactory.getLogger(AtmTransformer.class.getSimpleName());
/** The internal form class name of the class to transform */
private final        String      targetClassName;
/** The class loader of the class we want to transform */
private final        ClassLoader targetClassLoader;

public AtmTransformer(String targetClassName, ClassLoader targetClassLoader) {
  this.targetClassName = targetClassName;
  this.targetClassLoader = targetClassLoader;
}

@Override
public byte[] transform(ClassLoader loader, String className, Class<?> classBeingRedefined,
                        ProtectionDomain protectionDomain, byte[] classfileBuffer) throws IllegalClassFormatException {
  byte[] byteCode = classfileBuffer;

  String finalTargetClassName = this.targetClassName.replaceAll("\\.", "/"); //replace . with /
  if (!className.equals(finalTargetClassName)) {
    return byteCode;
  }

  if (className.equals(finalTargetClassName) && loader.equals(targetClassLoader)) {
    LOGGER.info("[Agent] Transforming class " + className);
    try {
      ClassPool cp = ClassPool.getDefault();

      CtClass cc = cp.get(targetClassName);

      CtMethod m = cc.getDeclaredMethod(AgentConstants.WITHDRAW_MONEY_METHOD);
      m.addLocalVariable("startTime", CtClass.longType);
      m.insertBefore("startTime = System.currentTimeMillis();");
      m.addLocalVariable("endTime", CtClass.longType);
      m.addLocalVariable("opTime", CtClass.longType);

      StringBuilder endBlock = new StringBuilder();
      endBlock.append("endTime = System.currentTimeMillis();");
      endBlock.append("opTime = (endTime-startTime)/1000;");
      endBlock.append(
          "System.out.println(\"[Application] Withdrawal operation completed in: \" " +
          "+ opTime + " + "\" seconds!\");");
      m.insertAfter(endBlock.toString());

      byteCode = cc.toBytecode();
      cc.detach();
    } catch (NotFoundException | CannotCompileException | IOException e) {
      LOGGER.error("Exception", e);
    }
  }
  return byteCode;
}
}
