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
package org.eclipse.che.jdt.ls.extension.core.internal.pom;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.util.ArrayList;
import java.util.List;
import org.eclipse.che.jdt.ls.extension.api.dto.ReImportMavenProjectsCommandParameters;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.jdt.ls.core.internal.JDTUtils;
import org.eclipse.jdt.ls.core.internal.managers.ProjectsManager;
import org.eclipse.jdt.ls.core.internal.preferences.PreferenceManager;
import org.eclipse.lsp4j.jsonrpc.json.adapters.CollectionTypeAdapterFactory;
import org.eclipse.lsp4j.jsonrpc.json.adapters.EitherTypeAdapterFactory;
import org.eclipse.lsp4j.jsonrpc.json.adapters.EnumTypeAdapterFactory;

/** @author Mykola Morhun */
public class ReImportMavenProjectsHandler {

  private static final Gson gson =
      new GsonBuilder()
          .registerTypeAdapterFactory(new CollectionTypeAdapterFactory())
          .registerTypeAdapterFactory(new EitherTypeAdapterFactory())
          .registerTypeAdapterFactory(new EnumTypeAdapterFactory())
          .create();

  /**
   * Updates given maven projects.
   *
   * @param arguments contains ReImportMavenProjectsCommandParameters in the first element
   * @param progressMonitor progress monitor
   * @return list of paths of updated projects
   */
  public static List<String> reImportMavenProjects(
      List<Object> arguments, IProgressMonitor progressMonitor) {
    ReImportMavenProjectsCommandParameters parameters =
        gson.fromJson(gson.toJson(arguments.get(0)), ReImportMavenProjectsCommandParameters.class);
    final List<String> projectsPaths = parameters.getProjectsToUpdate();
    final List<String> updatedProjectsPaths = new ArrayList<String>(projectsPaths.size());

    final PreferenceManager preferenceManager = new PreferenceManager();
    final ProjectsManager projectsManager = new ProjectsManager(preferenceManager);

    IFile pomFile;
    for (String pathToProject : projectsPaths) {
      ensureNotCancelled(progressMonitor);

      pomFile = JDTUtils.findFile(pathToProject + "/pom.xml");
      if (pomFile == null) {
        continue;
      }
      projectsManager.updateProject(pomFile.getProject());
      updatedProjectsPaths.add(pathToProject);
    }

    return updatedProjectsPaths;
  }

  private static void ensureNotCancelled(IProgressMonitor progressMonitor) {
    if (progressMonitor.isCanceled()) {
      throw new OperationCanceledException();
    }
  }
}
