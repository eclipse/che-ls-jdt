/*
 * Copyright (c) 2012-2018 Red Hat, Inc.
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *   Red Hat, Inc. - initial API and implementation
 */
package org.eclipse.che.jdt.ls.extension.core.internal;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;
import org.eclipse.che.jdt.ls.extension.api.Commands;
import org.eclipse.che.jdt.ls.extension.core.internal.classpath.ResolveClassPathsHandler;
import org.eclipse.che.jdt.ls.extension.core.internal.configuration.GetJavaCoreOptionsCommand;
import org.eclipse.che.jdt.ls.extension.core.internal.configuration.GetPreferencesCommand;
import org.eclipse.che.jdt.ls.extension.core.internal.configuration.UpdateJavaCoreOptionsCommand;
import org.eclipse.che.jdt.ls.extension.core.internal.configuration.UpdatePreferencesCommand;
import org.eclipse.che.jdt.ls.extension.core.internal.debug.FqnDiscover;
import org.eclipse.che.jdt.ls.extension.core.internal.externallibrary.ExternalLibrariesChildrenCommand;
import org.eclipse.che.jdt.ls.extension.core.internal.externallibrary.LibraryChildrenCommand;
import org.eclipse.che.jdt.ls.extension.core.internal.externallibrary.LibraryEntryCommand;
import org.eclipse.che.jdt.ls.extension.core.internal.externallibrary.ProjectExternalLibraryCommand;
import org.eclipse.che.jdt.ls.extension.core.internal.imports.OrganizeImportsCommand;
import org.eclipse.che.jdt.ls.extension.core.internal.navigation.FindImplementersHandler;
import org.eclipse.che.jdt.ls.extension.core.internal.plain.AddJarsCommand;
import org.eclipse.che.jdt.ls.extension.core.internal.plain.CreateSimpleProjectCommand;
import org.eclipse.che.jdt.ls.extension.core.internal.plain.GetProjectSourceLocationsCommand;
import org.eclipse.che.jdt.ls.extension.core.internal.plain.GetSourceFoldersCommand;
import org.eclipse.che.jdt.ls.extension.core.internal.plain.UpdateProjectClasspathCommand;
import org.eclipse.che.jdt.ls.extension.core.internal.pom.EffectivePomHandler;
import org.eclipse.che.jdt.ls.extension.core.internal.pom.GetMavenProjectsCommand;
import org.eclipse.che.jdt.ls.extension.core.internal.pom.ReImportMavenProjectsHandler;
import org.eclipse.che.jdt.ls.extension.core.internal.pom.RecomputePomDiagnosticsCommand;
import org.eclipse.che.jdt.ls.extension.core.internal.refactoring.move.GetDestinationsCommand;
import org.eclipse.che.jdt.ls.extension.core.internal.refactoring.move.MoveCommand;
import org.eclipse.che.jdt.ls.extension.core.internal.refactoring.move.ValidateMoveCommand;
import org.eclipse.che.jdt.ls.extension.core.internal.refactoring.move.VerifyMoveDestinationCommand;
import org.eclipse.che.jdt.ls.extension.core.internal.refactoring.rename.GetLinkedElementsCommand;
import org.eclipse.che.jdt.ls.extension.core.internal.refactoring.rename.GetRenamingElementInfoCommand;
import org.eclipse.che.jdt.ls.extension.core.internal.refactoring.rename.RenameCommand;
import org.eclipse.che.jdt.ls.extension.core.internal.refactoring.rename.ValidateNewNameCommand;
import org.eclipse.che.jdt.ls.extension.core.internal.testdetection.TestDetectionHandler;
import org.eclipse.che.jdt.ls.extension.core.internal.testdetection.TestFinderHandler;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jdt.ls.core.internal.IDelegateCommandHandler;

/**
 * Implementation of {@link IDelegateCommandHandler} which handles custom commands. For each
 * supported command should be registered its id into plugin.xml.
 */
public class CheDelegateCommandHandler implements IDelegateCommandHandler {
  private static final Map<String, BiFunction<List<Object>, IProgressMonitor, ? extends Object>>
      commands;

  static {
    commands = new HashMap<String, BiFunction<List<Object>, IProgressMonitor, ? extends Object>>();
    commands.put(Commands.FILE_STRUCTURE_COMMAND, FileStructureCommand::execute);
    commands.put(Commands.TEST_DETECT_COMMAND, TestDetectionHandler::detect);
    commands.put(Commands.FIND_TEST_BY_CURSOR_COMMAND, TestFinderHandler::getTestByCursorPosition);
    commands.put(
        Commands.FIND_TESTS_FROM_PROJECT_COMMAND, TestFinderHandler::getClassesFromProject);
    commands.put(Commands.FIND_TESTS_FROM_FOLDER_COMMAND, TestFinderHandler::getClassesFromFolder);
    commands.put(Commands.FIND_TESTS_FROM_ENTRY_COMMAND, TestFinderHandler::getClassesFromSet);
    commands.put(Commands.FIND_TESTS_IN_FILE_COMMAND, TestFinderHandler::getClassFqn);
    commands.put(Commands.RESOLVE_CLASSPATH_COMMAND, ResolveClassPathsHandler::resolveClasspaths);
    commands.put(Commands.GET_OUTPUT_DIR_COMMAND, ResolveClassPathsHandler::getOutputDirectory);
    commands.put(Commands.GET_EFFECTIVE_POM_COMMAND, EffectivePomHandler::getEffectivePom);
    commands.put(Commands.GET_MAVEN_PROJECTS_COMMAND, GetMavenProjectsCommand::execute);
    commands.put(Commands.RECOMPUTE_POM_DIAGNOSTICS, RecomputePomDiagnosticsCommand::execute);
    commands.put(
        Commands.REIMPORT_MAVEN_PROJECTS_COMMAND,
        ReImportMavenProjectsHandler::reImportMavenProjects);
    commands.put(
        Commands.GET_CLASS_PATH_TREE_COMMAND, ResolveClassPathsHandler::getClasspathModelTree);
    commands.put(Commands.GET_EXTERNAL_LIBRARIES_COMMAND, ProjectExternalLibraryCommand::execute);
    commands.put(
        Commands.GET_EXTERNAL_LIBRARIES_CHILDREN_COMMAND,
        ExternalLibrariesChildrenCommand::execute);
    commands.put(Commands.GET_LIBRARY_CHILDREN_COMMAND, LibraryChildrenCommand::execute);
    commands.put(Commands.GET_LIBRARY_ENTRY_COMMAND, LibraryEntryCommand::execute);
    commands.put(Commands.IDENTIFY_FQN_IN_RESOURCE, FqnDiscover::identifyFqnInResource);
    commands.put(Commands.FIND_RESOURCES_BY_FQN, FqnDiscover::findResourcesByFqn);
    commands.put(Commands.UPDATE_WORKSPACE, UpdateWorkspaceCommand::execute);
    commands.put(Commands.CREATE_SIMPLE_PROJECT, CreateSimpleProjectCommand::execute);
    commands.put(Commands.ADD_JARS_COMMAND, AddJarsCommand::execute);
    commands.put(Commands.UPDATE_PROJECT_CLASSPATH, UpdateProjectClasspathCommand::execute);
    commands.put(Commands.GET_SOURCE_FOLDERS, GetSourceFoldersCommand::execute);
    commands.put(Commands.FIND_IMPLEMENTERS_COMMAND, FindImplementersHandler::getImplementers);
    commands.put(Commands.USAGES_COMMAND, UsagesCommand::execute);
    commands.put(Commands.GET_JAVA_CORE_OPTIONS_СOMMAND, GetJavaCoreOptionsCommand::execute);
    commands.put(Commands.UPDATE_JAVA_CORE_OPTIONS_СOMMAND, UpdateJavaCoreOptionsCommand::execute);
    commands.put(Commands.GET_PREFERENCES_СOMMAND, GetPreferencesCommand::execute);
    commands.put(Commands.UPDATE_PREFERENCES_СOMMAND, UpdatePreferencesCommand::execute);
    commands.put(Commands.ORGANIZE_IMPORTS, OrganizeImportsCommand::execute);
    commands.put(Commands.RENAME_COMMAND, RenameCommand::execute);
    commands.put(Commands.GET_RENAME_TYPE_COMMAND, GetRenamingElementInfoCommand::execute);
    commands.put(Commands.VALIDATE_RENAMED_NAME_COMMAND, ValidateNewNameCommand::execute);
    commands.put(Commands.GET_LINKED_ELEMENTS_COMMAND, GetLinkedElementsCommand::execute);
    commands.put(Commands.GET_DESTINATIONS_COMMAND, GetDestinationsCommand::execute);
    commands.put(
        Commands.GET_PROJECT_SOURCE_LOCATIONS_COMMAND, GetProjectSourceLocationsCommand::execute);
    commands.put(Commands.MOVE_COMMAND, MoveCommand::execute);
    commands.put(Commands.VALIDATE_MOVE_COMMAND, ValidateMoveCommand::execute);
    commands.put(Commands.VERIFY_MOVE_DESTINATION_COMMAND, VerifyMoveDestinationCommand::execute);
  }

  @Override
  public Object executeCommand(String commandId, List<Object> arguments, IProgressMonitor progress)
      throws Exception {
    BiFunction<List<Object>, IProgressMonitor, ? extends Object> command = commands.get(commandId);
    if (command != null) {
      return command.apply(arguments, progress);
    }
    throw new UnsupportedOperationException(String.format("Unsupported command '%s'!", commandId));
  }
}
