package com.mytests.hibernate;

import org.hibernate.annotations.processing.Find;
import org.hibernate.annotations.processing.HQL;
import org.hibernate.annotations.processing.SQL;

import java.util.List;

public interface SampleQueries {

    // inject Hibernate QL
    @HQL("from Sample where color = :color")
    List<Sample> findSamplesByColor(String color);

    // Inject SQL
    @SQL("select * from sample where color = ?1 and sample = ?2")
    List<Sample> findSamplesByColorAndSample(String color, String sample);

    // the method name doesn't matter but check the parameter names!
    @Find
    List<Sample> dummyMethodName(String sample);


}