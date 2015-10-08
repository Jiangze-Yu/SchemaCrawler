/*
 *
 * SchemaCrawler
 * http://www.schemacrawler.com
 * Copyright (c) 2000-2015, Sualeh Fatehi.
 *
 * This library is free software; you can redistribute it and/or modify it under the terms
 * of the GNU Lesser General Public License as published by the Free Software Foundation;
 * either version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License along with this
 * library; if not, write to the Free Software Foundation, Inc., 59 Temple Place, Suite 330,
 * Boston, MA 02111-1307, USA.
 *
 */
package schemacrawler.tools.lint.executable;


import java.util.logging.Level;
import java.util.logging.Logger;

import schemacrawler.schemacrawler.SchemaCrawlerOptions;
import schemacrawler.tools.executable.CommandProvider;
import schemacrawler.tools.executable.Executable;
import schemacrawler.tools.lint.LinterHelp;
import schemacrawler.tools.options.OutputOptions;

public class LintCommandProvider
  implements CommandProvider
{

  private static final Logger LOGGER = Logger
    .getLogger(LintCommandProvider.class.getName());

  @Override
  public Executable configureNewExecutable(final SchemaCrawlerOptions schemaCrawlerOptions,
                                           final OutputOptions outputOptions)
  {
    final LintExecutable executable = new LintExecutable();
    if (schemaCrawlerOptions != null)
    {
      executable.setSchemaCrawlerOptions(schemaCrawlerOptions);
    }
    if (outputOptions != null)
    {
      executable.setOutputOptions(outputOptions);
    }
    return executable;
  }

  @Override
  public String getCommand()
  {
    return LintExecutable.COMMAND;
  }

  @Override
  public String getHelpAdditionalText()
  {
    try
    {
      return LinterHelp.getLinterHelpText();
    }
    catch (final Exception e)
    {
      LOGGER.log(Level.FINE, "Cannot get linter help", e);
      return "";
    }
  }

  @Override
  public String getHelpResource()
  {
    return "/help/LintCommandProvider.txt";
  }

}
