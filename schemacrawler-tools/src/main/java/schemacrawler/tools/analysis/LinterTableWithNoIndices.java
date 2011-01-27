/* 
 *
 * SchemaCrawler
 * http://sourceforge.net/projects/schemacrawler
 * Copyright (c) 2000-2011, Sualeh Fatehi.
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
package schemacrawler.tools.analysis;


import schemacrawler.schema.Index;
import schemacrawler.schema.Table;
import schemacrawler.schema.View;

public class LinterTableWithNoIndices
  extends BaseLinter<Table>
{

  public void lint(final Table table)
  {
    if (table != null && !(table instanceof View))
    {
      final Index[] indices = table.getIndices();
      if (table.getPrimaryKey() == null && indices.length == 0)
      {
        addLint(table, new Lint("no indices", Boolean.TRUE)
        {

          private static final long serialVersionUID = -9070658409181468265L;

          @Override
          public String getLintValueAsString()
          {
            return getLintValue().toString();
          }
        });
      }
    }
  }

}
