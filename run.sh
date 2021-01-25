# 创建maven项目模板
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
 -DarchetypeArtifactId=luvx-sample-archetype \
 -DinteractiveMode=false