/*
 *  Licensed to the Apache Software Foundation (ASF) under one
 *  or more contributor license agreements.  See the NOTICE file
 *  distributed with this work for additional information
 *  regarding copyright ownership.  The ASF licenses this file
 *  to you under the Apache License, Version 2.0 (the
 *  "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an
 *  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 *  KIND, either express or implied.  See the License for the
 *  specific language governing permissions and limitations
 *  under the License.
 */
package org.codehaus.groovy.control.messages;

import java.io.PrintWriter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.codehaus.groovy.control.Janitor;
import org.codehaus.groovy.control.SourceUnit;
import org.codehaus.groovy.syntax.SyntaxException;

/** A class for error messages produced by the parser system. */
public class SyntaxErrorMessage extends Message {
  protected SyntaxException cause;
  protected SourceUnit source;

  public SyntaxErrorMessage(SyntaxException cause, SourceUnit source) {
    this.source = source;

    String message = cause.getMessage();
    boolean isEOF = message.contains("EOF");

    String errorLineString = this.source.getSource().getLine(cause.getLine(), new Janitor());
    String errorName = null;
    int start = -1;
    int end = -1;

    if (!isEOF) {
      Matcher matcher = Pattern.compile("'(.*?)'").matcher(message);

      if (matcher.find()) {
        errorName = matcher.group(1);
      }

      start = errorLineString.indexOf(errorName) + 1;
      end = start + errorName.length();

      if (cause.getStartColumn() != start || cause.getEndColumn() != end) {
        cause.setStartColumn(start);
        cause.setEndColumn(end);
        cause.setEndLine(end);
      }
    } else {
      errorName = errorLineString.trim();
      if (errorName.isEmpty()) {
        int line = getValidErrorLine(cause.getLine());
        errorLineString = this.source.getSource().getLine(line, new Janitor());
        errorName = errorLineString.trim();
        start = errorLineString.indexOf(errorName) + 1;
        end = start + errorName.length();
        cause.setLine(line);
        cause.setStartColumn(end - 1);
        cause.setEndColumn(end);
      } else {
        errorLineString = this.source.getSource().getLine(cause.getLine(), new Janitor());
        errorName = errorLineString.trim();
        start = errorLineString.indexOf(errorName) + 1;
        end = start + errorName.length();
        cause.setStartColumn(end - 1);
        cause.setEndColumn(end);
      }
    }

    this.cause = cause;
    cause.setSourceLocator(source.getName());
  }

  /** Returns the underlying SyntaxException. */
  public SyntaxException getCause() {
    return this.cause;
  }

  /** Returns the valid error line for EOF SyntaxException. */
  private int getValidErrorLine(int line) {
    while (line >= 0) {
      String string = this.source.getSource().getLine(line, new Janitor());
      if (string != null && !string.trim().isEmpty()) {
        return line;
      }
      line--;
    }
    return line;
  }

  /** Writes out a nicely formatted summary of the syntax error. */
  public void write(PrintWriter output, Janitor janitor) {
    String name = source.getName();
    int line = getCause().getStartLine();
    int column = getCause().getStartColumn();
    String sample = source.getSample(line, column, janitor);

    output.print(name + ": " + line + ": " + getCause().getMessage());
    if (sample != null) {
      output.println();
      output.print(sample);
      output.println();
    }
  }
}
