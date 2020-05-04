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

interface AppConstants {
String AGENT_FILE_PATH      = "/target/java-instrumentation-agent-0.1.0-SNAPSHOT.jar";
String APP_NAME             = "com.developerlife.example.instrumentation.application.Main";
long   ATM_PROCESSING_DELAY = 2000L;
String CMD_START            = "start";
String CMD_LOADAGENT        = "loadagent";
}
