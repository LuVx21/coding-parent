install:
	cd coding-parent-boot-dependencies && mvn clean install -DskipTest=true && mvnd clean && cd -
	mvn clean install -DskipTest=true && mvnd clean

dep-update:
	mvn versions:display-dependency-updates > dep.log && mvnd clean
plu-update:
	mvn versions:display-plugin-updates > plugin.log && mvnd clean


link:
	#   ln -s $(HOME)/code/config/jetbrain/consoles $(HOME)/Library/Application\ Support/JetBrains/IntelliJIdea*/consoles
	@for file in dataSources dataSources.local.xml dataSources.xml; do \
		ln -s $(HOME)/code/config/jetbrain/$$file ./.idea/$$file; \
	done
