package com.mytests.hibernate;

import org.hibernate.annotations.processing.Find;
import org.hibernate.annotations.processing.HQL;
import org.hibernate.annotations.processing.SQL;

import java.util.List;

public interface SampleQueries {

    @HQL("from Sample where color = :color")
    List<Sample> findSamplesByColor(String color);

    @SQL("select * from sample where color = ?1 and sample = ?2")
    List<Sample> findSamplesByColorAndSample(String color, String sample);

    @Find
    List<Sample> dummyMethodName(String sample);


}