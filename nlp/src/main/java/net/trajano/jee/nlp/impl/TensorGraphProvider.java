package net.trajano.jee.nlp.impl;

import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.ejb.Lock;
import javax.ejb.LockType;
import javax.ejb.Schedule;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Dependent;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.persistence.EntityManager;

import org.tensorflow.Graph;
import org.tensorflow.OperationBuilder;
import org.tensorflow.Session;

import net.trajano.jee.nlp.jpa.TensorGraph;

/**
 * This is a tensor graph provider. It will periodically save it's data to the
 * database type.
 *
 * @author Archimedes Trajano
 */
@Singleton
@Startup
@ApplicationScoped
public class TensorGraphProvider {

    private static final long GRAPH_ID = 1L;

    private static final Logger LOG = Logger.getLogger("net.trajano.jee.nlp");

    private EntityManager em;

    private Graph graph;

    @Lock(LockType.WRITE)
    @PreDestroy
    public void closeGraph() {

        graph.close();
    }

    /**
     * This will load the current graph from the database or construct a new one
     * if it is not found.
     */
    @PostConstruct
    public void loadFromDatabase() {

        TensorGraph dbData = em.find(TensorGraph.class, GRAPH_ID);
        if (dbData == null) {
            graph = new Graph();
            dbData = new TensorGraph(GRAPH_ID);
            dbData.setGraphDef(graph.toGraphDef());
            em.persist(dbData);
        } else {
            graph.importGraphDef(dbData.getGraphDef());
        }

    }

    /**
     * Please ensure that the session is closed after use.
     */
    @Lock(LockType.READ)
    @Produces
    @Dependent
    public Session newSession() {

        LOG.warning("new session created");
        return new Session(graph);
    }

    public OperationBuilder opBuilder(final String type,
        final String name) {

        return graph.opBuilder(type, name);
    }

    @Schedule(minute = "*/10",
        hour = "*")
    @Lock(LockType.READ)
    public void persistCurrentGraph() {

        final TensorGraph dbData = em.find(TensorGraph.class, GRAPH_ID);
        dbData.setGraphDef(graph.toGraphDef());
        em.merge(dbData);
    }

    @Inject
    public void setEm(final EntityManager em) {

        this.em = em;
    }
}
