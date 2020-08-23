<template>
  <div>
      <div class="SelectorContainer container" style="position: center;margin-top: 20px">
        <el-form :inline="true" :model="formInline" class="demo-form-inline" size="large">
          <el-form-item label="区域" style="margin-left: 10px">
            <el-select
              v-model="value1" clearable   @change="showMarket"
              style="margin-left: 5px;"
              placeholder="请选择区域">
              <el-option
                v-for="item in Regionaldata"
                :key="item.value"
                :label="item.label"
                :value="item.label">
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="市场"style="margin-left: 20px">
            <el-select
              v-model="value2" clearable    @change="getMarket"
              style="margin-left: 10px;"
              placeholder="请选择市场">
              <el-option
                v-for="item in Marketdata"
                :key="item.value"
                :label="item.label"
                :value="item.label">
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="品类"style="margin-left: 10px">
            <el-select
              v-model="value3" clearable  @change="showCategory"
              style="margin-left: 5px;"
              placeholder="请选择品类">
              <el-option
                v-for="item in Categorydata"
                :key="item.value"
                :label="item.label"
                :value="item.label">
              </el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="品种"style="margin-left: 10px">
            <el-select
              v-model="value4" clearable  @change="getVariety"
              style="margin-left: 5px;"
              placeholder="请选择品种">
              <el-option
                v-for="item in Varietydata"
                :key="item.value"
                :label="item.label"
                :value="item.label">
              </el-option>
            </el-select>
          </el-form-item>
  </el-form>
          <el-button type="primary"  @click="onSubmit">查询</el-button>
      </div>
    <div style="height: 40px"></div>
    <div style="float: left;width: 12%;">&nbsp;</div>
    <div class="lg-chart sm-chart">
    <div id="InfoByData" style="width: 100%;height: 100%;"></div>
    </div>
    <div class="lg-left sm-left">&nbsp;</div>
    <div class="lg-chart sm-chart">
    <div id ="dashPanel" style="width: 100%;height:  100%"></div>
    </div>
  </div>
</template>

<script>
    export default {
      name: "InfoCount",
      props: ["userJson"],
      data() {
        return {
          formInline: {
            region: '',
            market: '',
            category: '',
            varity: "",
            user: '',
          },
          chart: null,
          Regionaldata: null,
          Marketdata: null,
          Categorydata: null,
          Varietydata: null,
          value1: [],
          value2: [],
          value3: [],
          value4: [],
          Category:"",
          Region: "",
          Variety: "",
          Market: "",
        }
      },
      mounted() {
        this.drawLine();
        this.drawGraph();
        this.loadData()
      },
      methods: {
        onSubmit() {
          this.drawGraph();

        },
        //加载横向直方图
        drawLine() {
          let myChart = this.$echarts.init(document.getElementById("InfoByData"));
          /*this.$axios({
            method: 'get',
            url: "http://139.155.228.86:8083/infoCount/getInfoCount/2020-8-4",
            data: {}
          }).then(function (res) {*/
         this.$axios.get("http://139.155.228.86:8083/infoCount/getInfoCount/").then(function (res) {
            myChart.hideLoading();
            // 填入数据
            myChart.setOption({
              legend: {
                data: ['品类数', '市场数',"品种数","总数"]

              },
              tooltip: {},
              dimensions: ['categoryCount', 'date', 'marketCount', 'totalCount',"varietyCount"],
              dataset: {
                source:
                  res.data.data
              },
              grid: [
                {bottom: '60%',},
                {top: '60%',
                  bottom:'10%'}
              ],
              xAxis: [{
                type: 'category',
                name: '日期',
                gridIndex: 0
              },
                {
                  type: 'category',
                  name: '日期',
                  gridIndex: 1
                }
              ],
              yAxis: [{
                name: '数量',
                gridIndex: 0
              },
                {
                  name: '数量',
                  gridIndex: 1
                },
                ],
              // Declare several bar series, each will be mapped
              // to a column of dataset.source by default.
              series: [{
                name: '品类数',
                type: 'bar',
                encode: {
                  x: 1,
                  y: 'categoryCount',
                  tooltip: 'categoryCount'
                }
              },
                {
                  name: '市场数',
                  type: 'bar',
                  encode: {
                    x: 1,
                    y: 'marketCount',
                    tooltip: 'marketCount'
                  }
                },
                {
                  name: '品种数',
                  type: 'bar',
                  encode: {
                    x: 1,
                    y: 'varietyCount',
                    tooltip: 'varietyCount'
                  }
                },
                {
                  name: '总数',
                  type: 'bar',
                  xAxisIndex: 1, yAxisIndex: 1,
                  encode: {
                    x: 1,
                    y: 'totalCount',
                    tooltip: 'totalCount'
                  }
                }

              ]
            });

            window.onresize = function () {
               myChart.resize();
               _this.chart.resize();
             }

          })
        },

        //加载仪表图
        drawGraph() {
          var _this = this
          _this.chart = this.$echarts.init(document.getElementById("dashPanel"));
          this.$axios({
            method: 'get',
            url: "http://139.155.228.86:8083/price/getCount" ,
            params: {
              market: _this.Market,
              variety: _this.Variety,
              region: _this.Region,
              category: _this.Category
            }
          }).then(function (res) {
            console.log(_this.value4)
            console.log(res.data.data)
            _this.chart.hideLoading();
            // 填入数据
            _this.chart.setOption({
              tooltip: {
                formatter: '{a} <br/>{b} : {c}%'
              },
              toolbox: {
                feature: {
                }
              },
              series: [
                {
                  name: '已查询数量',
                  min: 0,                 // 最小的数据值,默认 0 。映射到 minAngle。
                  max: 10000,               // 最大的数据值,默认 100 。映射到 maxAngle。
                  type: 'gauge',
                  detail: {formatter: '{value}条'},
                  data: [{value: res.data.data, name: '已查询到的数量'}]
          }
              ]
            })
          })
        },

        //加载选择框数据
        loadData() {
          var _this = this
          //加载区域数据
          this.$axios.get("http://139.155.228.86:8083/market/getRegions").then(function (res) {
            var medata = ""
            for(var i = 0; i < res.data.data.length ; i++){
              medata = medata + "{\"value\":  \"选项" + (i+1) + "\" ," + "\"label\": \""+ res.data.data[i].marketRegion +"\"},"
            }
            medata = medata.substring(0,medata.length-1)
            medata = "[" + medata + "]"
            medata = "{ \"data\":" + medata + "}"
            medata = JSON.parse(medata)
            _this.Regionaldata = medata.data
          })

          //加载市场数据
          /*this.$axios.get("../static/marketdata.json").then(function (res) {
            var medata = ""

            for(var i = 0; i < res.data.data.length ; i++){
              medata = medata + "{\"value\":  \"选项" + (i+1) + "\" ," + "\"label\": \""+ res.data.data[i].market +"\"},"
            }
            medata = medata.substring(0,medata.length-1)
            medata = "[" + medata + "]"
            medata = "{ \"data\":" + medata + "}"
            medata = JSON.parse(medata)
            _this.Marketdata = medata.data
          })
          */
          //加载品类数据
          this.$axios.get("http://139.155.228.86:8083/variety/getCategories").then(function (res) {
            var medata = ""

            for(var i = 0; i < res.data.data.length ; i++){
              medata = medata + "{\"value\":  \"选项" + (i+1) + "\" ," + "\"label\": \""+ res.data.data[i].categoryName +"\"},"
            }
            medata = medata.substring(0,medata.length-1)
            medata = "[" + medata + "]"
            medata = "{ \"data\":" + medata + "}"
            medata = JSON.parse(medata)
            _this.Categorydata = medata.data
          })



        //加载品种数据
          /*this.$axios.get("http://localhost:8082/variety/getVarieties/"+ null).then(function (res) {
            var medata = ""
            console.log(_this.Categorydata.value)
            for(var i = 0; i < res.data.data.length ; i++){
              medata = medata + "{\"value\":  \"选项" + (i+1) + "\" ," + "\"label\": \""+ res.data.data[i].varietyName +"\"},"
            }
            medata = medata.substring(0,medata.length-1)
            medata = "[" + medata + "]"
            medata = "{ \"data\":" + medata + "}"
            medata = JSON.parse(medata)
            _this.Varietydata = medata.data
          })
*/

        },

        //选中品类操作
        showCategory(event){
          var _this = this
          _this.value4 = null
          _this.Category = event; //获取option对应的value值
          console.log("你选中了",_this.Category)
          //加载品种数据
          this.$axios.get("http://139.155.228.86:8083/variety/getVarieties/"+ _this.Category).then(function (res) {
            var medata = ""
            console.log(_this.Category.value)
            for(var i = 0; i < res.data.data.length ; i++){
              medata = medata + "{\"value\":  \"选项" + (i+1) + "\" ," + "\"label\": \""+ res.data.data[i].varietyName +"\"},"
            }
            medata = medata.substring(0,medata.length-1)
            medata = "[" + medata + "]"
            medata = "{ \"data\":" + medata + "}"
            medata = JSON.parse(medata)
            _this.Varietydata = medata.data
          })

        },

        showMarket(event){
          var _this = this
          _this.value2 = null
          _this.Region = event; //获取option对应的value值
          console.log("你选中了",_this.Region)
          //加载市场数据
          this.$axios.get("http://139.155.228.86:8083/market/getMarkets/"+_this.Region).then(function (res) {
            var medata = ""

            for(var i = 0; i < res.data.data.length ; i++){
              medata = medata + "{\"value\":  \"选项" + (i+1) + "\" ," + "\"label\": \""+ res.data.data[i].marketName +"\"},"
            }
            medata = medata.substring(0,medata.length-1)
            medata = "[" + medata + "]"
            medata = "{ \"data\":" + medata + "}"
            medata = JSON.parse(medata)
            _this.Marketdata = medata.data
          })
        },

        getMarket(event){
          this.Market = event
        },

        getVariety(event){
          this.Variety = event
        }



      }
    }
</script>

<style scoped>
  /**小屏0px-768px*/
  @media only screen and (min-width:0px) and (max-width:768px){
    .sm-chart{
      float: left;
      width:77%;
      height:500px;
    }
    .sm-left{
      float: left;
      width:20%;
    }
  }

  /**大屏768*/
  @media only screen and (min-width:768px){
    .lg-chart{
      float: left;
      width:40%;
      height:600px;
    }
    .lg-left{
      float: left;
      width:0%;
    }
  }
  .SelectorContainer {
    position: relative;
    top: 30px;
    left: -3%;
    width: 100%;
    height: 20%;
  }

</style>
