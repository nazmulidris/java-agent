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

@file:JvmName("Atm")

package com.developerlife.example.instrumentation.application

import ColorConsoleContext.Companion.colorConsole
import Colors
import span

/**
 * Created by adi on 6/11/18.
 */
object Atm {
  @JvmStatic
  fun withdrawMoney(amount: Int) {
    colorConsole {
      printLine {
        span(Colors.Red, "⏰ [${Atm.javaClass.simpleName}] Withdrawal transaction for [$amount] units")
      }
    }

    colorConsole {
      printLine {
        span(Colors.Red, "\t⏰ [${Atm.javaClass.simpleName}] Processing delay")
      }
    }

    Thread.sleep(AppConstants.ATM_PROCESSING_DELAY) // Processing delay.

    colorConsole {
      printLine {
        span(Colors.Green, "\t💰 [${Atm.javaClass.simpleName}] Successful withdrawal of [$amount] units!")
      }
    }
  }
}