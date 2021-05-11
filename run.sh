for i in \
        pom.xml \
        pom-java.xml \
        pom-kotlin.xml \
        spring-boot-java.xml \
        spring-boot-kotlin.xml
do
    mvn install -f $i
done

# 创建maven项目模板
# cd sample
# mvn archetype:create-from-project
# cd target/generated-sources/archetype
# mvn install

# 使用模板创建项目
mvn archetype:generate \
 -DgroupId=org.luvx \
 -DartifactId=usage-dataflow \
 -Dversion=1.0.1-SNAPSHOT \
 -Dpackage=org.luvx.app \
 -DarchetypeCatalog=local
#  -DarchetypeArtifactId=luvx-sample-archetype \
#  -DinteractiveMode=false
