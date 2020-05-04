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

@file:JvmName("App")

package com.developerlife.example.instrumentation.application

import ColorConsoleContext.Companion.colorConsole
import Colors
import span

object App {
  @JvmStatic
  fun main(args: Array<String>) {

    val sleepTime = args[0].toInt()
    val amount1 = args[1].toInt()
    val amount2 = args[2].toInt()

    colorConsole {
      printLine {
        span(Colors.Purple, "🏁 [${App.javaClass.simpleName}] BEGIN")
        span(Colors.Yellow, "sleepTime: $sleepTime")
        span(Colors.Yellow, "amount1: $amount1")
        span(Colors.Yellow, "amount2: $amount2")
      }
    }

    Atm.withdrawMoney(amount1)
    colorConsole {
      printLine {
        span(Colors.Red, "⏰ [${App.javaClass.simpleName}] sleeping")
      }
    }
    Thread.sleep(sleepTime.toLong())
    Atm.withdrawMoney(amount2)

    colorConsole { printLine { span(Colors.Purple, "🏁 [${App.javaClass.simpleName}] END ") } }
  }
}