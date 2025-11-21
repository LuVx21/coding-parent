
install:
	cd coding-parent-boot-dependencies && mvn clean install -DskipTest=true && mvnd clean && cd -
	mvn clean install -DskipTest=true && mvnd clean

dep-update:
	mvn versions:display-dependency-updates > dep.log && mvnd clean
plu-update:
	mvn versions:display-plugin-updates > plugin.log && mvnd clean
