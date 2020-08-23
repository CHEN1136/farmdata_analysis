import java.lang.Exception
import java.sql.DriverManager
import java.text.SimpleDateFormat
import java.util.{Calendar, Date}

import org.apache.log4j.{Level, Logger}
import org.apache.parquet.format.IntType
import org.apache.spark.SparkConf
import org.apache.spark.api.java.JavaSparkContext
import org.apache.spark.sql.types.{DateType, DoubleType, LongType, StringType, StructField, StructType}
import org.apache.spark.sql.{DataFrame, SparkSession}

object Analysis {
  val spark = SparkSession.builder().master("local").appName("price analysis")
    .config("dfs.client.use.datanode.hostname", "true").getOrCreate()
  case class Price(region:String, market_name:String, variety_name:String, lowest_price:String, highest_price:String, avg_price:String, date:String)

  val calendar = Calendar.getInstance()
  calendar.setTime(new Date())
  calendar.add(Calendar.DATE, 0)
  val yesterday = dateformat(calendar.getTime)

  def main(args: Array[String]): Unit = {
    //远程连接hdfs集群,加载数据，初步提取
    //加载数据源
    val totalSource = spark.sparkContext.textFile("hdfs://hadoop01:9000/flume/*/")
      .map(_.split(",")).map(x => Price(x(0),x(1),x(2),x(3),x(4),x(5),x(6))).distinct()

    val sourceRdd= spark.sparkContext.textFile(s"hdfs://hadoop01:9000/flume/$yesterday/").map(_.split(","))
      .map(x => Price(x(0),x(1),x(2),x(3),x(4),x(5),x(6))).distinct()


    val sourceDF = spark.createDataFrame(sourceRdd).distinct()

    val totalSourceDF = spark.createDataFrame(totalSource).distinct()

    val varietyDF = spark.read.format("csv")
      .option("sep",",")
      .option("header","true")
      .load("hdfs://hadoop01:9000/input/variety.csv")
      .select("variety_name","category_name","another_name")

    getCount(totalSourceDF,varietyDF)
    getPrice(sourceDF,varietyDF)
    getMarkets(totalSourceDF)
  }

  /***
   * 筛选出近五次数据
   * @param source
   */
  def getPrice(source: DataFrame,variety:DataFrame): Unit ={
    val resultRdd = source.select("region","market_name","variety_name","lowest_price","highest_price","avg_price","date")
      .distinct().orderBy(source("date").desc)
    storeDataToMysql("price",resultRdd)
    //getVarieties(resultRdd,variety)
  }

  /***
   *
   * 市场和区域对照表
   * @param source
   */
  def getMarkets(source: DataFrame): Unit ={
    val resultRdd = source.select("market_name","region").distinct().orderBy("region")
    resultRdd.show(20)
    val tempsc = spark.sparkContext.parallelize(resultRdd.collect())
    val schema = new StructType().add(StructField("market_name", StringType,true)).add(StructField("market_region", StringType,true))
    val resultDF = spark.createDataFrame(tempsc,schema)
    storeMarketData(resultDF)
  }


  /***
   *
   * 获取数据监控近3五天数据
   * @param sourceDF
   * @param varietyDF
   */
  def getCount(sourceDF: DataFrame,varietyDF:DataFrame): Unit ={
    //计算市场种数
    val marketCount = sourceDF.select("market_name","date").distinct().groupBy("date")
      .count().toDF("date","market_count")
    //计算总数
    val totalCount =sourceDF.groupBy("date").count().toDF("date","total_count")
    //计算品种总数
    val varietyCount = sourceDF.select("variety_name","date").distinct().groupBy("date")
      .count().toDF("date","variety_count")
    //计算品类总数

    val categoryCount = sourceDF.join(varietyDF,sourceDF("variety_name") === varietyDF("variety_name"),"left_outer")
      .select("date","category_name").distinct().groupBy("date")
      .count().toDF("date","category_count")

    //连接市场总数和品类总数
    val tempDF_1 = marketCount.join(varietyCount,marketCount("date")===varietyCount("date"),"left_outer")
      .distinct()
    //连接品种总数

    val tempDF_2 = tempDF_1.join(categoryCount,categoryCount("date") === marketCount("date"),"left_outer")
      .distinct()

    //连接总数
    val result = tempDF_2.join(totalCount,totalCount("date")===marketCount("date"),"left_outer")
      .distinct().drop(varietyCount("date")).drop(categoryCount("date")).drop(totalCount("date")).orderBy(marketCount("date").desc)

    val top5Rdd = spark.sparkContext.parallelize(result.take(5))
    //声明Rdd对应的schema
    val schema = new StructType().add(StructField("date", StringType,true))
      .add(StructField("market_count",LongType,true)).add(StructField("variety_count",LongType,true))
      .add(StructField("category_count",LongType,true)).add(StructField("total_count",LongType,true))
    //Rdd(纯数据) + schema -------->具有格式、包含数据的DataFrame
    val top5DF = spark.createDataFrame(top5Rdd,schema)

    storeInfoCount(top5DF)
  }



  /***
   * 将结果数据转存MYSQL
   * @param tableNameInMysql
   * @param result
   */
  def storeDataToMysql(tableNameInMysql: String, result: DataFrame): Unit ={
    val url = "jdbc:mysql://139.155.228.86:3306/agriculture_db?characterEncoding=utf8&serverTimezone=Asia/Shanghai&useSSL=false"
    val tableName = tableNameInMysql
    val props = new java.util.Properties()
    props.put("user","root")
    props.put("password","ROOTroot_1")
    props.put("driver","com.mysql.cj.jdbc.Driver")

    try {
      result.write.mode("append").jdbc(url, tableName, props)
    }catch {case e: Exception => e.printStackTrace()}
    println("------------------传输到mysql成功--------------------")
  }


  def dateformat(date:Date): String ={
    val sdf = new SimpleDateFormat("yyyy-MM-dd")
    sdf.format(date)
  }

  /***
   * 将结果数据转存MYSQL
   * @param result
   */
  def storeInfoCount(result: DataFrame): Unit ={
    val url = "jdbc:mysql://139.155.228.86:3306/agriculture_db?characterEncoding=utf8&serverTimezone=Asia/Shanghai&useSSL=false"
    val tableName = "info_count"
    val props = new java.util.Properties()
    props.put("user","root")
    props.put("password","ROOTroot_1")
    props.put("driver","com.mysql.cj.jdbc.Driver")
    //先清空MySQL表
    val conn = DriverManager.getConnection(url,props)
    val sm = conn.prepareCall("truncate table " + tableName)
    sm.execute()
    sm.close()
    try {
      result.write.mode("append").jdbc(url, tableName, props)
    }catch {case e:Exception => null}
    println("------------------传输到mysql成功--------------------")
  }

  /***
   * 将结果数据转存MYSQL
   * @param result
   */
  def storeMarketData(result: DataFrame): Unit ={
    val url = "jdbc:mysql://139.155.228.86:3306/agriculture_db?characterEncoding=utf8&serverTimezone=Asia/Shanghai&useSSL=false"
    val tableName = "market"
    val props = new java.util.Properties()
    props.put("user","root")
    props.put("password","ROOTroot_1")
    props.put("driver","com.mysql.cj.jdbc.Driver")
    //先清空MySQL表
    val conn = DriverManager.getConnection(url,props)
    val sm = conn.prepareCall("truncate table " + tableName)
    sm.execute()
    sm.close()
    try {
      result.write.mode("append").jdbc(url, tableName, props)
    }catch {case e:Exception => null}
    println("------------------传输到mysql成功--------------------")
  }

}
