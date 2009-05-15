/*
 * (c) Copyright 2009 Hewlett-Packard Development Company, LP
 * All rights reserved.
 * [See end of file]
 */

package com.hp.hpl.jena.tdb.modify;

import java.util.Iterator;

import com.hp.hpl.jena.graph.Graph;
import com.hp.hpl.jena.graph.Node;
import com.hp.hpl.jena.query.Dataset;
import com.hp.hpl.jena.shared.Lock;
import com.hp.hpl.jena.update.GraphStore;

// DatasetGraphTDB isa GraphStore?
public class GraphStoreTDB implements GraphStore
{
    // NOT USED
    @Override
    public void finishRequest()
    {}

    @Override
    public void startRequest()
    {}

    @Override
    public Dataset toDataset()
    {
        return null ;
    }

    @Override
    public void addGraph(Node graphName, Graph graph)
    {}

    @Override
    public void close()
    {}

    @Override
    public Graph removeGraph(Node graphName)
    {
        return null ;
    }

    @Override
    public void setDefaultGraph(Graph g)
    {}

    @Override
    public boolean containsGraph(Node graphNode)
    {
        return false ;
    }

    @Override
    public Graph getDefaultGraph()
    {
        return null ;
    }

    @Override
    public Graph getGraph(Node graphNode)
    {
        return null ;
    }

    @Override
    public Lock getLock()
    {
        return null ;
    }

    @Override
    public Iterator<Node> listGraphNodes()
    {
        return null ;
    }

    @Override
    public int size()
    {
        return 0 ;
    }

}

/*
 * (c) Copyright 2009 Hewlett-Packard Development Company, LP
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