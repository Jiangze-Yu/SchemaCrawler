/*
========================================================================
SchemaCrawler
http://www.schemacrawler.com
Copyright (c) 2000-2017, Sualeh Fatehi <sualeh@hotmail.com>.
All rights reserved.
------------------------------------------------------------------------

SchemaCrawler is distributed in the hope that it will be useful, but
WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.

SchemaCrawler and the accompanying materials are made available under
the terms of the Eclipse Public License v1.0, GNU General Public License
v3 or GNU Lesser General Public License v3.

You may elect to redistribute this code under any of these licenses.

The Eclipse Public License is available at:
http://www.eclipse.org/legal/epl-v10.html

The GNU General Public License v3 and the GNU Lesser General Public
License v3 are available at:
http://www.gnu.org/licenses/

========================================================================
*/

package schemacrawler.tools.text.schema;


import static sf.util.Utility.isBlank;

import schemacrawler.schema.ColumnDataType;
import schemacrawler.schema.CrawlInfo;
import schemacrawler.schema.DatabaseInfo;
import schemacrawler.schema.DatabaseObject;
import schemacrawler.schema.JdbcDriverInfo;
import schemacrawler.schema.Routine;
import schemacrawler.schema.SchemaCrawlerInfo;
import schemacrawler.schema.Sequence;
import schemacrawler.schema.Synonym;
import schemacrawler.schema.Table;
import schemacrawler.schemacrawler.SchemaCrawlerException;
import schemacrawler.tools.options.OutputOptions;
import schemacrawler.tools.text.base.BaseFormatter;
import schemacrawler.tools.text.utility.TextFormattingHelper.DocumentHeaderType;
import schemacrawler.tools.text.utility.html.Alignment;
import schemacrawler.tools.traversal.SchemaTraversalHandler;

/**
 * Text formatting of schema.
 *
 * @author Sualeh Fatehi
 */
final class SchemaListFormatter
  extends BaseFormatter<SchemaTextOptions>
  implements SchemaTraversalHandler
{

  private final boolean isVerbose;

  /**
   * Text formatting of schema.
   *
   * @param schemaTextDetailType
   *        Types for text formatting of schema
   * @param options
   *        Options for text formatting of schema
   * @param outputOptions
   *        Options for text formatting of schema
   * @param identifierQuoteString
   *        TODO
   * @throws SchemaCrawlerException
   *         On an exception
   */
  SchemaListFormatter(final SchemaTextDetailType schemaTextDetailType,
                      final SchemaTextOptions options,
                      final OutputOptions outputOptions,
                      final String identifierQuoteString)
    throws SchemaCrawlerException
  {
    super(options,
          schemaTextDetailType == SchemaTextDetailType.details,
          outputOptions,
          identifierQuoteString);
    isVerbose = schemaTextDetailType == SchemaTextDetailType.details;
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void begin()
  {
    if (!options.isNoHeader())
    {
      formattingHelper.writeDocumentStart();
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void end()
    throws SchemaCrawlerException
  {
    if (!options.isNoFooter())
    {
      formattingHelper.writeDocumentEnd();
    }

    super.end();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void handle(final ColumnDataType columnDataType)
    throws SchemaCrawlerException
  {
    if (printVerboseDatabaseInfo && isVerbose)
    {
      final String databaseSpecificTypeName;
      if (options.isShowUnqualifiedNames())
      {
        databaseSpecificTypeName = columnDataType.getName();
      }
      else
      {
        databaseSpecificTypeName = columnDataType.getFullName();
      }
      formattingHelper.writeNameRow(databaseSpecificTypeName, "[data type]");
    }
  }

  @Override
  public void handle(final CrawlInfo crawlInfo)
  {
    if (crawlInfo == null)
    {
      return;
    }

    final String title = crawlInfo.getTitle();
    if (!isBlank(title))
    {
      formattingHelper.writeHeader(DocumentHeaderType.title, title);
    }

    if (options.isNoInfo())
    {
      return;
    }

    formattingHelper.writeHeader(DocumentHeaderType.subTitle,
                                 "System Information");

    if (!options.isNoSchemaCrawlerInfo())
    {
      formattingHelper.writeObjectStart();
      formattingHelper.writeNameValueRow("generated by",
                                         crawlInfo.getSchemaCrawlerInfo(),
                                         Alignment.inherit);
      formattingHelper
        .writeNameValueRow("generated on",
                           formatTimestamp(crawlInfo.getCrawlTimestamp()),
                           Alignment.inherit);
    }

    if (options.isShowDatabaseInfo())
    {
      formattingHelper.writeNameValueRow("database version",
                                         crawlInfo.getDatabaseInfo(),
                                         Alignment.inherit);
    }
    if (options.isShowJdbcDriverInfo())
    {
      formattingHelper.writeNameValueRow("driver version",
                                         crawlInfo.getJdbcDriverInfo(),
                                         Alignment.inherit);
    }

    formattingHelper.writeObjectEnd();
  }

  @Override
  public final void handle(final DatabaseInfo dbInfo)
  {
  }

  @Override
  public void handle(final JdbcDriverInfo driverInfo)
  {
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void handle(final Routine routine)
  {
    final String routineTypeDetail = String
      .format("%s, %s", routine.getRoutineType(), routine.getReturnType());
    final String routineName;
    if (options.isShowUnqualifiedNames())
    {
      routineName = routine.getName();
    }
    else
    {
      routineName = routine.getFullName();
    }
    final String routineType = "[" + routineTypeDetail + "]";

    formattingHelper.writeNameRow(routineName, routineType);
    printRemarks(routine);
  }

  @Override
  public void handle(final SchemaCrawlerInfo schemaCrawlerInfo)
  {
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void handle(final Sequence sequence)
  {
    final String sequenceName;
    if (options.isShowUnqualifiedNames())
    {
      sequenceName = sequence.getName();
    }
    else
    {
      sequenceName = sequence.getFullName();
    }
    final String sequenceType = "[sequence]";

    formattingHelper.writeNameRow(sequenceName, sequenceType);
    printRemarks(sequence);

  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void handle(final Synonym synonym)
  {
    final String synonymName;
    if (options.isShowUnqualifiedNames())
    {
      synonymName = synonym.getName();
    }
    else
    {
      synonymName = synonym.getFullName();
    }
    final String synonymType = "[synonym]";

    formattingHelper.writeNameRow(synonymName, synonymType);
    printRemarks(synonym);
  }

  @Override
  public void handle(final Table table)
  {
    final String tableName;
    if (options.isShowUnqualifiedNames())
    {
      tableName = table.getName();
    }
    else
    {
      tableName = table.getFullName();
    }
    final String tableType = "[" + table.getTableType() + "]";

    formattingHelper.writeNameRow(tableName, tableType);
    printRemarks(table);
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void handleColumnDataTypesEnd()
  {
    if (printVerboseDatabaseInfo && isVerbose)
    {
      formattingHelper.writeObjectEnd();
    }
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void handleColumnDataTypesStart()
  {
    if (printVerboseDatabaseInfo && isVerbose)
    {
      formattingHelper.writeHeader(DocumentHeaderType.subTitle, "Data Types");

      formattingHelper.writeObjectStart();
    }
  }

  @Override
  public final void handleHeaderEnd()
    throws SchemaCrawlerException
  {

  }

  @Override
  public final void handleHeaderStart()
    throws SchemaCrawlerException
  {
  }

  @Override
  public final void handleInfoEnd()
    throws SchemaCrawlerException
  {

  }

  @Override
  public final void handleInfoStart()
    throws SchemaCrawlerException
  {
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void handleRoutinesEnd()
    throws SchemaCrawlerException
  {
    formattingHelper.writeObjectEnd();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void handleRoutinesStart()
    throws SchemaCrawlerException
  {
    formattingHelper.writeHeader(DocumentHeaderType.subTitle, "Routines");

    formattingHelper.writeObjectStart();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void handleSequencesEnd()
    throws SchemaCrawlerException
  {
    formattingHelper.writeObjectEnd();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void handleSequencesStart()
    throws SchemaCrawlerException
  {
    formattingHelper.writeHeader(DocumentHeaderType.subTitle, "Sequences");

    formattingHelper.writeObjectStart();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void handleSynonymsEnd()
    throws SchemaCrawlerException
  {
    formattingHelper.writeObjectEnd();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void handleSynonymsStart()
    throws SchemaCrawlerException
  {
    formattingHelper.writeHeader(DocumentHeaderType.subTitle, "Synonyms");

    formattingHelper.writeObjectStart();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void handleTablesEnd()
    throws SchemaCrawlerException
  {
    formattingHelper.writeObjectEnd();
  }

  /**
   * {@inheritDoc}
   */
  @Override
  public void handleTablesStart()
    throws SchemaCrawlerException
  {
    formattingHelper.writeHeader(DocumentHeaderType.subTitle, "Tables");

    formattingHelper.writeObjectStart();
  }

  private void printRemarks(final DatabaseObject object)
  {
    if (object == null || !object.hasRemarks() || options.isHideRemarks())
    {
      return;
    }
    formattingHelper.writeDescriptionRow(object.getRemarks());
  }
}
