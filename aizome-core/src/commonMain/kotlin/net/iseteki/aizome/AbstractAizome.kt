/**
 * Copyright 2025 Nobuhiro Ito
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

package net.iseteki.aizome

import net.iseteki.aizome.logger.AizomeDefaultLogger
import net.iseteki.aizome.logger.AizomeLogger
import net.iseteki.aizome.parser.Parser
import net.iseteki.aizome.parser.ParserLoggerImpl
import net.iseteki.aizome.render.Render
import net.iseteki.aizome.render.RenderImpl
import net.iseteki.aizome.render.RenderLoggerImpl
import net.iseteki.aizome.render.RenderOperator

abstract class AbstractAizome<T>(
    private val renderOperator: RenderOperator<T>,
) {
    val defaultStyles: Map<String, StringStyle<T>>
        get() = _defaultStyles
    private var _defaultStyles: Map<String, StringStyle<T>> = emptyMap()

    val logger: AizomeLogger
        get() = _logger
    private var _logger: AizomeLogger = AizomeDefaultLogger()

    fun setDefaultStyles(styles: Map<String, StringStyle<T>>) {
        _defaultStyles = styles
    }

    fun setLogger(logger: AizomeLogger) {
        _logger = logger
    }

    fun createParserRender(): Pair<Parser, Render<T>> = Pair(
        Parser(ParserLoggerImpl(_logger)),
        RenderImpl(RenderLoggerImpl(_logger), renderOperator),
    )
}