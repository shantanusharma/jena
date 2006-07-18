/*
 * (c) Copyright 2006 Hewlett-Packard Development Company, LP
 * All rights reserved.
 * [See end of file]
 */

package com.hp.hpl.jena.sdb.layout2;

import static com.hp.hpl.jena.sdb.sql.SQLUtils.sqlStr;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import com.hp.hpl.jena.sdb.sql.SDBConnection;
import com.hp.hpl.jena.sdb.sql.SDBExceptionSQL;

/** Interface to setting up the bulk loader environment.
 * 
 * @author Andy Seaborne
 * @version $Id: LoaderMySQL.java,v 1.1 2006/04/21 12:40:20 andy_seaborne Exp $
 */

public class LoaderMySQL extends BulkLoaderLJ
{
    public LoaderMySQL(SDBConnection connection) { super(connection) ; }
    
    public void createLoaderTable()
    {
        try {
            Statement s = connection().getSqlConnection().createStatement();
            s.execute(sqlStr(
                    "CREATE TEMPORARY TABLE IF NOT EXISTS NNode",
                    "(",
                    "  hash BIGINT NOT NULL,",
                    "  lex TEXT BINARY CHARACTER SET utf8 NOT NULL,",
                    "  lang VARCHAR(10) BINARY CHARACTER SET utf8 NOT NULL,",
                    "  datatype VARCHAR("+ TableNodes.UriLength+ ") BINARY CHARACTER SET utf8 NOT NULL,",
                    "  type int unsigned NOT NULL,",
                    "  vInt int,",
                    "  vDouble double,",
                    "  vDateTime datetime",
                    ") DEFAULT CHARSET=utf8;"
            ));
            s.execute(sqlStr(
            		"CREATE TEMPORARY TABLE IF NOT EXISTS NTrip",
            		"(",
            		"  s BIGINT NOT NULL,",
            		"  p BIGINT NOT NULL,",
            		"  o BIGINT NOT NULL",
            		");"
            ));
        }
        catch (SQLException ex)
        { throw new SDBExceptionSQL("Making loader table",ex) ; }
    }
    
    // Use INSERT IGNORE and TRUNCATE to clear
    @Override
    public void createPreparedStatements()
	{
		try {
		Connection conn = connection().getSqlConnection();

        super.clearTripleLoaderTable = conn.prepareStatement("TRUNCATE NTrip;");
        super.clearNodeLoaderTable = conn.prepareStatement("TRUNCATE NNode;");
        super.insertTripleLoaderTable = conn.prepareStatement("INSERT INTO NTrip VALUES (?,?,?);");
        super.insertNodeLoaderTable = conn
            .prepareStatement("INSERT INTO NNode VALUES (?,?,?,?,?,?,?,?);");
        
        super.insertNodes = conn.prepareStatement(sqlStr(
        		"INSERT IGNORE INTO Nodes (hash, lex, lang, datatype, type)",
        		"	SELECT NNode.hash, NNode.lex, NNode.lang, NNode.datatype, NNode.type",
        		"	FROM NNode"
            ));
        
		super.insertTriples = conn.prepareStatement(sqlStr(
				"INSERT IGNORE INTO Triples",
				"	SELECT S.id, P.id, O.id FROM",
				"	  NTrip JOIN Nodes AS S ON (NTrip.s=S.hash)",
				"     JOIN Nodes AS P ON (NTrip.p=P.hash)",
				"     JOIN Nodes AS O ON (NTrip.o=O.hash)"
            ));
        } catch (SQLException ex)
        { ex.printStackTrace(); throw new SDBExceptionSQL("Preparing statements",ex) ; }
	}
}

/*
 * (c) Copyright 2006 Hewlett-Packard Development Company, LP
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 * 3. The name of the author may not be used to endorse or promote products
 *    derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE AUTHOR ``AS IS'' AND ANY EXPRESS OR
 * IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
 * IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY DIRECT, INDIRECT,
 * INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT
 * NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 * DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF
 * THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */