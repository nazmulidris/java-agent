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

@file:JvmName("Main")

package com.developerlife.example.instrumentation.application

import ColorConsoleContext.Companion.colorConsole
import Colors
import lineNoCommasOrTimestamp
import span
import java.util.*

/**
 * Command line usage:
 * - start sleepTime amount1 amount2
 * - loadagent
 */
object Main {
  @JvmStatic
  fun main(args: Array<String>) {
    if (args.isEmpty()) {
      colorConsole {
        println(
            lineNoCommasOrTimestamp {
              span(Colors.Purple, "Command line usage:")
            }
        )
        println(
            lineNoCommasOrTimestamp {
              span(Colors.Green, "start")
              span(Colors.Yellow, " sleepTime")
              span(Colors.Yellow, " amount1")
              span(Colors.Yellow, " amount2")
            }
        )
        println(
            lineNoCommasOrTimestamp {
              span(Colors.Green, "loadagent")
            }
        )
      }
    }
    else {
      when (args[0]) {
        AppConstants.CMD_START     -> App.main(Arrays.copyOfRange(args, 1, args.size))
        AppConstants.CMD_LOADAGENT -> AgentLoader.main()
      }
    }
  }
}