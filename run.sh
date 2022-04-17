#!/bin/bash

for i in \
        logging-parent/pom.xml \
        logging-spring-parent/pom.xml \
        coding-root.xml \
        coding-root-kotlin.xml \
        coding-kotlin-enhancer/pom.xml \
        coding-parent-java.xml \
        coding-parent-kotlin.xml \
        spring-boot-java.xml \
        spring-boot-kotlin.xml \
        coding-common/pom.xml
do
    mvn clean install -f $i && mvn clean -f $i
done

