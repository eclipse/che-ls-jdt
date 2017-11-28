/*
 * Copyright (c) 2012-2017 Red Hat, Inc.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   Red Hat, Inc. - initial API and implementation
 */
package org.eclipse.che.jdt.ls.extension.api.dto.externallibrary;

/**
 * This class represents a content of external library's node.
 *
 * @author Valeriy Svydenko
 */
public class ClassContent {
  private String content;
  private boolean isGenerated;

  public ClassContent() {}

  public ClassContent(String content, boolean isGenerated) {
    this.content = content;
    this.isGenerated = isGenerated;
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  public boolean isGenerated() {
    return isGenerated;
  }

  public void setGenerated(boolean generated) {
    isGenerated = generated;
  }
}