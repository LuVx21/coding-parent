#!/bin/bash

for i in \
        coding-dependencies.xml \
        coding-kotlin-dependencies.xml \
        coding-kotlin-enhancer/pom.xml \
        coding-parent-java.xml \
        coding-parent-kotlin.xml \
        spring-boot-java.xml \
        spring-boot-kotlin.xml
do
    mvn clean install -f $i
done

