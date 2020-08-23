<template>
  <div>
    <div class="SelectorContainer container" style="position: center;margin-top: 20px">
      <el-form :inline="true" :model="formInline" class="demo-form-inline" size="large">
        <div class="row">
          <div class="col-lg-9">
             <el-form-item label="品类"style="margin-left: 10px">
          <el-select
            v-model="value3" clearable  @change="showVariety"
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
          </div>
          <div class="col-lg-1">
          <el-form-item>
            <el-button type="primary" @click="onSubmit">查询</el-button>
          </el-form-item>
          </div>
        </div>
      </el-form>
    </div>
    <div class="lg-left sm-left">&nbsp;</div>
    <div class="lg-chart sm-chart">
      <div class="echarts">
        <div :style="{height:'700px',width:'100%'}" ref="myEchart"></div>
      </div>
    </div>
    <div class="lg-left sm-left">&nbsp;</div>
    <div class="lg-chart-right sm-chart">
      <h2 >区域行情</h2>
      <div id="RegionalData" style="height: 600px ;width:100%"></div>
    </div>
    <el-dialog
      title="提示"
      :visible.sync="dialogVisible"
      width="30%"
    >
      <span>请选择具体品种</span>
      <span slot="footer" class="dialog-footer">
                  <el-button type="primary" @click="dialogVisible = false">确 定</el-button>
            </span>
    </el-dialog>
  </div>
</template>

<script>


  import echarts from "echarts";
  //   import '../../node_modules/echarts/map/js/world.js'
  import '../../node_modules/echarts/map/js/china.js' // 引入中国地图数据
  let ChinaChart = null
  let BarChart =  null
  export default {
      name: "RegionalQuote",
      props: ["userJson"],
      data: function () {
        return {
          formInline: {
            region: '',
            market: '',
            category: '',
            varity: "",
            user: '',
          },
          chart: null,
          Categorydata: null,
          Varietydata: null,
          value3: [],
          value4: [],
          Category: "",
          Variety: "",
          dialogVisible: false
        };
      },
      mounted() {
        this.chinaConfigure();
        this.drawLine();
        this.loadData();

      },
      beforeDestroy() {
        if (!this.chart) {
          return;
        }
        this.chart.dispose();
        this.chart = null;
      },
      methods: {
        chinaConfigure() {
          var _this = this
          _this.ChinaChart = echarts.init(this.$refs.myEchart); //这里是为了获得容器所在位置
          window.onresize = _this.ChinaChart.resize;
          this.$axios({
            method: 'get',
            url: "http://139.155.228.86:8083/price/getAvgPrice/" + _this.Variety,
            data: {}
          }).then(function (res) {
            console.log(res.data.data)
            var medata = "";
            for(var i = 0; i < res.data.data.length ; i++){
              medata = medata + "{\"name\":  \"" + res.data.data[i].region + "\" ," + "\"value\": "+ res.data.data[i].avgPrice +"},"
            }
            medata = medata.substring(0,medata.length-1)
            medata = "[" + medata + "]"
            medata = "{ \"data\":" + medata + "}"
            console.log(medata)
            medata = JSON.parse(medata)

            _this.ChinaChart.setOption({ // 进行相关配置
              tooltip: {}, // 鼠标移到图里面的浮动提示框
              dataRange: {
                show: false,
                min: 0,
                max: 300,
                text: ['High', 'Low'],
                realtime: true,
                calculable: true,
                color: ['orangered', 'yellow', 'lightskyblue']
              },
              geo: { // 这个是重点配置区
                map: 'china', // 表示中国地图
                roam: true,
                label: {
                  normal: {
                    show: true, // 是否显示对应地名
                    textStyle: {
                      color: 'rgba(0,0,0,0.4)'
                    }
                  }
                },
                itemStyle: {
                  normal: {
                    borderColor: 'rgba(0, 0, 0, 0.2)',
                    borderWidth: 0.5,
                    areaColor: "#ffefd5", //区域颜色
                  },
                  emphasis: {
                    areaColor: "#ffdead",
                    shadowOffsetX: 0,
                    shadowOffsetY: 0,
                    shadowBlur: 20,
                    borderWidth: 0,
                    shadowColor: 'rgba(0, 0, 0, 0.5)'
                  }
                }
              },
              series: [{
                type: 'scatter',
                coordinateSystem: 'geo', // 对应上方配置
              },
                {
                  name: '单价', // 浮动框的标题
                  type: 'map',
                  geoIndex: 0,
                  data: medata.data
                }
              ]
            })
          })
        },
        drawLine(){
          var _this = this
          _this.BarChart = this.$echarts.init(document.getElementById("RegionalData"));
          this.$axios({
            method: 'get',
            url: "http://139.155.228.86:8083/price/getAvgPrice/" + _this.Variety,
            data: {}
          }).then(function (res) {
              console.log(_this.Variety)
            _this.BarChart.hideLoading();
              // 填入数据
            _this.BarChart.setOption({
                title: {},
                tooltip: {
                  trigger: "axis"
                },
                legend: {
                  data: []
                },
                calculable: true,

                grid: {containLabel: true},
                xAxis: { name: "元/千克"},
                yAxis: {name: "区域" ,type: 'category'},
                visualMap: {
                  orient: 'horizontal',
                  left: 'center',
                  // Map the score column to color
                  dimension: 0,
                  min: 0,
                  max: 40,
                  inRange: {
                    color: ['#daad5b', '#e14755'],
                    shadowOffsetX: 0,
                    shadowOffsetY: 0,
                    shadowBlur: 20,
                    borderWidth: 0,
                    shadowColor: 'rgba(255, 160 ,122, 1)'
                  },
                },
                dataset: {
                  source:
                  res.data.data

                },
                series: [
                  {
                    type: 'bar',
                    encode: {
                      // Map the "amount" column to X axis.
                      x: 'avg_price',
                      // Map the "product" column to Y axis
                      y: 'region'
                    }
                  }
                ]
              });
         })
        },
        onSubmit() {

          var _this = this
          if(_this.Variety != '') {
            this.$axios({
              method: 'get',
              url: "http://139.155.228.86:8083/price/getAvgPrice/" + _this.Variety,
              data: {}
            }).then(function (res) {
              console.log(_this.Variety)
              _this.BarChart.setOption({
                title: {},
                tooltip: {
                  trigger: "axis"
                },
                legend: {
                  data: []
                },
                calculable: true,

                grid: {containLabel: true},
                xAxis: { name: "元/千克"},
                yAxis: {name: "区域" ,type: 'category'},
                visualMap: {
                  orient: 'horizontal',
                  left: 'center',
                  // Map the score column to color
                  dimension: 0,
                  min: 0,
                  max: 40,
                  inRange: {
                    color: ['#daad5b', '#e14755'],
                    shadowOffsetX: 0,
                    shadowOffsetY: 0,
                    shadowBlur: 20,
                    borderWidth: 0,
                    shadowColor: 'rgba(255, 160 ,122, 1)'
                  },
                },
                dataset: {
                  source:
                  res.data.data

                },
                series: [
                  {
                    type: 'bar',
                    encode: {
                      // Map the "amount" column to X axis.
                      x: 'avg_price',
                      // Map the "product" column to Y axis
                      y: 'region'
                    }
                  }
                ]
              });



              var medata = "";
              for (var i = 0; i < res.data.data.length; i++) {
                medata = medata + "{\"name\":  \"" + res.data.data[i].region + "\" ," + "\"value\": " + res.data.data[i].avgPrice + "},"
              }
              medata = medata.substring(0, medata.length - 1)
              medata = "[" + medata + "]"
              medata = "{ \"data\":" + medata + "}"
              console.log(medata)
              medata = JSON.parse(medata)
              _this.ChinaChart.setOption({ // 进行相关配置
                tooltip: {}, // 鼠标移到图里面的浮动提示框
                dataRange: {
                  show: false,
                  min: 0,
                  max: 40,
                  text: ['High', 'Low'],
                  realtime: true,
                  calculable: true,
                  color: [
                    'rgb(238, 59 ,59)'
                    ,'rgb(255, 99, 71)'
                    ,'rgb(250 ,128 ,114)'
                    ,'rgb(255, 127, 80)'
                    ,'rgb(50 ,205 ,50)'
                    ,'rgb(127, 255, 0)'
                    ,'rgb(173,255,47)',
                    'rgb(127, 255, 212)'
                    ,'rgb(238, 221 ,130)'
                  ]
                },
                geo: { // 这个是重点配置区
                  map: 'china', // 表示中国地图
                  roam: true,
                  label: {
                    normal: {
                      show: true, // 是否显示对应地名
                      textStyle: {
                        color: 'rgba(0,0,0,0.4)'
                      }
                    }
                  },
                  itemStyle: {
                    normal: {
                      borderColor: 'rgba(0, 0, 0, 0.2)',
                      borderWidth: 1,
                      areaColor: "#ffef95", //区域颜色
                    },
                    emphasis: {
                      areaColor: "#ffb683",
                      shadowOffsetX: 0,
                      shadowOffsetY: 0,
                      shadowBlur: 10,
                      borderWidth: 0,
                      shadowColor: 'rgba(255,54,53,0.5)'
                    }
                  }
                },
                series: [{
                  type: 'scatter',
                  coordinateSystem: 'geo', // 对应上方配置
                },
                  {
                    name: '单价', // 浮动框的标题
                    type: 'map',
                    geoIndex: 0,
                    data: medata.data

                  }
                ]
              })

            })
          }else{
                _this.dialogVisible = true;
          }

        },

        loadData() {
          var _this = this
          this.$axios.get("http://139.155.228.86:8083/variety/getCategories").then(function (res) {
            var medata = ""

            for(var i = 0; i < res.data.data.length ; i++){
              medata = medata + "{\"value\":  \"选项" + (i+1) + "\" ," + "\"label\": \""+ res.data.data[i].categoryName +"\"},"
            }
            medata = medata.substring(0,medata.length-1)
            medata = "[" + medata + "]"
            medata = "{ \"data\":" + medata + "}"
            console.log(medata)
            medata = JSON.parse(medata)
            console.log(medata)
            _this.Categorydata = medata.data
            console.log(_this.Categorydata)
          })
        },

        showVariety(event){

          var _this = this
          _this.value4 = null
          _this.Category = event; //获取option对应的value值
          console.log("你选中了",_this.Category)
          //加载市场数据
          this.$axios.get("http://139.155.228.86:8083/variety/getVarieties/"+_this.Category).then(function (res) {
            var medata = ""

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

        getVariety(event){
          this.Variety = event
        },


      }
    }

</script>

<style scoped>
  #RegionalData {
    position: relative;
    padding: 0px;
    margin: 0px;
    border-width: 0px;
    cursor: default;
    overflow-y:auto
  }
  @media only screen and (min-width:0px) and (max-width:768px){
    .sm-chart{
      float: left;
      width:100%;
      height:700px;
    }
    .sm-left{
      float: left;
      width:10%;
    }

  }
  /**大屏768*/
  @media only screen and (min-width:768px){
    .lg-chart{
      float: left;
      width:60%;
      height:700px;
    }
    .lg-chart-right{
      float: left;
      width:35%;
      height:600px;
    }
    .lg-left{
      float: left;
      width:0%;
    }
  }

</style>
