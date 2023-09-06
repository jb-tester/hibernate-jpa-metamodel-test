package com.mytests.hibernate;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Page;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import static jakarta.persistence.Persistence.createEntityManagerFactory;
import static java.lang.System.out;

/**
 * *
 * <p>Created by irina on 9/6/2023.</p>
 * <p>Project: hibernate-63-test</p>
 * *
 */
public class RunMe {
    private static final SessionFactory factory;

    static {
        try {
            Configuration configuration = new Configuration();
            configuration.configure();

            factory = configuration.buildSessionFactory();
        } catch (Throwable ex) {
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static Session getSession() throws HibernateException {
        return factory.openSession();
    }

    public static void main(String[] args) {
        out.println("============ simple query:");
        inSession(factory, entityManager -> {
            for (Object d : entityManager.createQuery("from Sample where color = 'white'").getResultList()) {
                out.println(d);
            }
        });
        out.println("============ metamodel tests: use @HQL:");
        for (Sample sample : findSamples1("blue")) {
            out.println(sample);
        }
        out.println("============ metamodel tests: use @SQL:");
        for (Sample sample : findSamples3("blue","mysample")) {
            out.println(sample);
        }
        out.println("============ metamodel tests: use @Find:");
        for (Sample sample : findSamples2("mysample")) {
            out.println(sample);
        }
    }

    static void inSession(EntityManagerFactory factory, Consumer<EntityManager> work) {
        var entityManager = factory.createEntityManager();
        var transaction = entityManager.getTransaction();
        try {
            transaction.begin();
            work.accept(entityManager);
            transaction.commit();
        }
        catch (Exception e) {
            if (transaction.isActive()) transaction.rollback();
            throw e;
        }
        finally {
            entityManager.close();
        }
    }
     // The SampleQueries_ static class will remain unresolved until sources are generated
    // build the project to make the references resolved
    static List<Sample> findSamples1(String color) {
        var samples = factory.fromTransaction(session ->
                SampleQueries_.findSamplesByColor(session, color));
        return samples.isEmpty() ? new ArrayList<>() : samples;
    }
    static List<Sample> findSamples2(String sample) {
        var samples = factory.fromTransaction(session ->
                SampleQueries_.dummyMethodName(session, sample));
        return samples.isEmpty() ? new ArrayList<>() : samples;
    }
    static List<Sample> findSamples3(String color, String sample) {
        var samples = factory.fromTransaction(session ->
                SampleQueries_.findSamplesByColorAndSample(session, color, sample));
        return samples.isEmpty() ? new ArrayList<>() : samples;
    }
}
