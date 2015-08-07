mvn compile;
mvn package;
mvn clean install;
cp target/GroupingMessagePassing-0.9-SNAPSHOT.jar ../OptApi/lib/br/ufrgs/MASBench/
cd ../OptApi/lib/br/ufrgs/MASBench
mvn deploy:deploy-file -Durl=file:///home/sim/Documentos/workspace/OptApi/lib/ -Dfile=GroupingMessagePassing-0.9-SNAPSHOT.jar -DgroupId=br.ufrgs.MASBench -DartifactId=GroupingMessagePassing -Dpackaging=jar -Dversion=0.9-SNAPSHOT

