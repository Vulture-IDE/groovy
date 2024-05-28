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
package org.codehaus.groovy.syntax;

import org.codehaus.groovy.GroovyException;
import org.codehaus.groovy.ast.ASTNode;

/** Base exception indicating a syntax error. */
public class SyntaxException extends GroovyException {

  private static final long serialVersionUID = 7447641806794047013L;

  /** Line upon which the error occurred. */
  private int startLine;

  private int endLine;

  /** Column upon which the error occurred. */
  private int startColumn;

  private int endColumn;

  private String sourceLocator;

  public SyntaxException(String message, ASTNode node) {
    this(
        message,
        node.getLineNumber(),
        node.getColumnNumber(),
        node.getLastLineNumber(),
        node.getLastColumnNumber());
  }

  public SyntaxException(String message, int startLine, int startColumn) {
    this(message, startLine, startColumn, startLine, startColumn + 1);
  }

  public SyntaxException(
      String message, int startLine, int startColumn, int endLine, int endColumn) {
    super(message, false);
    this.startLine = startLine;
    this.startColumn = startColumn;
    this.endLine = endLine;
    this.endColumn = endColumn;
  }

  public SyntaxException(String message, Throwable cause, int startLine, int startColumn) {
    this(message, cause, startLine, startColumn, startLine, startColumn + 1);
  }

  public SyntaxException(
      String message, Throwable cause, int startLine, int startColumn, int endLine, int endColumn) {
    super(message, cause);
    this.startLine = startLine;
    this.startColumn = startColumn;
    this.endLine = endLine;
    this.endColumn = endColumn;
  }

  // Properties
  // ----------------------------------------------------------------------
  public void setSourceLocator(String sourceLocator) {
    this.sourceLocator = sourceLocator;
  }

  public String getSourceLocator() {
    return this.sourceLocator;
  }

  /**
   * Retrieve the line upon which the error occurred.
   *
   * @return The line.
   */
  public int getLine() {
    return getStartLine();
  }

  /**
   * Set the line on which the error occurred. This method is typically used internally and should
   * not be called directly by clients unless they have a specific reason to modify the error
   * location.
   *
   * @param line the line number where the error occurred
   */
  public void setLine(int line) {
    setStartLine(line);
  }

  /**
   * @return the line on which the error occurs
   */
  public int getStartLine() {
    return startLine;
  }

  /**
   * Set the line on which the error occurred. This method is typically used internally and should
   * not be called directly by clients unless they have a specific reason to modify the error
   * location.
   *
   * @param line the line number where the error occurred
   */
  public void setStartLine(int line) {
    this.startLine = line;
  }

  /**
   * Retrieve the column upon which the error occurred.
   *
   * @return The column.
   */
  public int getStartColumn() {
    return startColumn;
  }

  /**
   * Set the column on which the error occurred. This method is typically used internally and should
   * not be called directly by clients unless they have a specific reason to modify the error
   * location.
   *
   * @param column the column number where the error occurred
   */
  public void setStartColumn(int column) {
    this.startColumn = column;
  }

  /**
   * Retrieve the end line on which the error occurred.
   *
   * @return the end line
   */
  public int getEndLine() {
    return endLine;
  }

  /**
   * Set the end line on which the error occurred. This method is typically used internally and
   * should not be called directly by clients unless they have a specific reason to modify the error
   * location.
   *
   * @param endLine the end line number where the error occurred
   */
  public void setEndLine(int endLine) {
    this.endLine = endLine;
  }

  /**
   * Retrieve the end column on which the error occurred.
   *
   * @return the end column
   */
  public int getEndColumn() {
    return endColumn;
  }

  /**
   * Set the end column on which the error occurred. This method is typically used internally and
   * should not be called directly by clients unless they have a specific reason to modify the error
   * location.
   *
   * @param endColumn the end column number where the error occurred
   */
  public void setEndColumn(int endColumn) {
    this.endColumn = endColumn;
  }

  public String getOriginalMessage() {
    return super.getMessage();
  }

  public String getMessage() {
    return super.getMessage() + " @ line " + startLine + ", column " + startColumn + ".";
  }
}
