## Apache Zeppelin Configuration


### Using Apache Zeppelin Docker

```
docker pull epahomov/docker-zeppelin
```

Log into the container in order to build zeppelin against the specific

```
docker run -i -t -p 8080:8080 -p 8081:8081 epahomov/docker-zeppelin /bin/bash
cd incubator-zeppelin
mvn clean package -Pspark-1.3 -Dhadoop.version=2.2.0 -Phadoop-2.2 -DskipTests
```

For more details: https://registry.hub.docker.com/u/epahomov/docker-zeppelin/

### Building Zeppelin

```
git clone https://github.com/apache/incubator-zeppelin
cd incubator-zeppelin
mvn clean package -Pspark-1.3 -Dhadoop.version=2.2.0 -Phadoop-2.2 -DskipTests
```

### Starting a server

```
bin/zeppelin.sh
```

### Edit SparkContext Parameters

Access the Zeppelin interface (http://localhost:8080 for example) and click on **Interpreter**.  Click **Edit** and add the following parameters:

|  Param | Value |
|-------------------|-------------|
| spark.gemfire.locators | localhost[10334] |
| spark.kryo.registrator | io.pivotal.gemfire.spark.connector.GemFireKryoRegistrator |

Now click **Save** and remember to **restart the Spark interpreter**.

### Working in the notebook

Select the **Notebook** and create a new one. That's our workspace from now on in Zeppelin. If would like more details about Zeppelin, there is a predefined Notebook named Tutorial.

Use the first note to load dependencies and libraries.

```
%dep
z.reset()
z.load("<CONNECTOR_PATH>/gemfire-spark-connector_2.10-0.5.0.jar")
z.load("<GEODE_HOME>/lib/gemfire-core-dependencies.jar")
z.load("<LAB_HOME>/client/IoT-Analytics/build/libs/IoT-Analytics-0.0.1-SNAPSHOT.jar")
```

Then use Geode OQL and register results as a table in SparkSQL in order to allow Zeppelin queries.

```
%spark
import io.pivotal.gemfire.spark.connector._

val dataFrame = sqlContext.gemfireOQL("SELECT p.signal_dbm, p.device_id FROM /Trilateration p")

import sqlContext.implicits._

dataFrame.toDF().registerTempTable("Probe")
val SQLResult = sqlContext.sql("SELECT * FROM Probe")
```

Now you can call ```Probe``` table using **SQL** (_SparkSQL_) in a different step in the notebook.  

```
%sql
select * from Probe
```
