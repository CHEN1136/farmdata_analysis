<template>
  <div>
    <div class="SelectorContainer" style="position: center">
      <el-form :inline="true" :model="formInline" class="demo-form-inline" size="large">
        <div class="row">
          <div class="col-lg-11">
        <el-form-item label="区域">
          <el-select
            v-model="value1" clearable  @change="showMarket"
            style="margin-left: 20px;"
            placeholder="请选择区域">
            <el-option
              v-for="item in Regionaldata"
              :key="item.value"
              :label="item.label"
              :value="item.label">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="市场">
          <el-select
            v-model="value2" clearable  @change="getMarket"
            style="margin-left: 20px;"
            placeholder="请选择市场">
            <el-option
              v-for="item in Marketdata"
              :key="item.value"
              :label="item.label"
              :value="item.label">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="品类">
          <el-select
            v-model="value3" clearable  @change="showCategory"
            style="margin-left: 20px;"
            placeholder="请选择品类">
            <el-option
              v-for="item in Categorydata"
              :key="item.value"
              :label="item.label"
              :value="item.label">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="品种">
          <el-select
            v-model="value4" clearable  @change="getVariety"
            style="margin-left: 20px;"
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
    <div style="height: 40px"></div>
    <div style="float: left;width: 6%;">&nbsp;</div>
    <div class="lg-chart sm-chart">
      <div id="myChart" :style="{width: '100%', height: '100%'}"></div>
    </div>
    <div class="lg-left sm-left">&nbsp;</div>
    <div class="lg-table sm-table">
      <h3>预测价格</h3>
      <p id="predictprice"></p>
    </div>
  </div>
</template>

<script>
    export default {
      name: "PricePrediction",
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
          charts: '',
          /*	opinion: ["1", "3", "3", "4", "5"],*/
          opinionData: ["3", "2", "4", "4", "5"]
        }
      },
      mounted(){
        this.loadData()
      },
      methods: {
        drawLine(data) {
          // 基于准备好的dom，初始化echarts实例
          let myChart = this.$echarts.init(document.getElementById('myChart'));
          myChart.hideLoading();
          // 绘制图表
          myChart.setOption({
            legend: {
              data: ['']
            },
            dataset: {
              source:
              data.data
            },
            xAxis: {
              type: 'category',
              boundaryGap: false
            },
            yAxis: {
              type: 'value',
              axisLabel: {
                formatter: '{value}'
              }
            },
            series: [
              {
                name: '价格',
                type: 'line',
                markPoint: {
                  data: [
                    {type: 'max', name: '最大值'},
                    {type: 'min', name: '最小值'}
                  ]
                },
                markLine: {
                  data: [
                    {type: 'average', name: '平均值'}
                  ]
                }
              },
            ]
          });
        },
        //加载选择框数据
        loadData() {
          var _this = this
          var url = "http://139.155.228.86:8083"
          //加载区域数据
          this.$axios.get(url+"/market/getRegions").then(function (res) {
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

          //加载品类数据
          this.$axios.get(url + "/variety/getCategories").then(function (res) {
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

        },
        //选中品类操作
        showCategory(event){
          var _this = this
          _this.value4=null
          var url = "http://139.155.228.86:8083"
          _this.Category = event; //获取option对应的value值
          console.log("你选中了",_this.Category)
          //加载品种数据
          this.$axios.get(url +"/variety/getVarieties/"+ _this.Category).then(function (res) {
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

        showMarket(event){
          var _this = this
          _this.value2= null
          var url = "http://139.155.228.86:8083"
          _this.Region = event; //获取option对应的value值
          console.log("你选中了",_this.Region)
          //加载市场数据
          this.$axios.get(url +"/market/getMarkets/"+_this.Region).then(function (res) {
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
        },

        getPrediction(){
          var _this = this;
          var jsons = "";
          jsons = jsons + "{\"market_name\":\""+_this.Market+"\",";
          jsons = jsons + "\"variety_name\":\""+_this.Variety+"\"}";
          jsons = JSON.parse(jsons);
          this.$axios({
            url: 'http://139.155.228.86:5000/prediction',
            method:'post',
            contentType:"application/json",
            dataType: "JSON",
            data : jsons,
          }).then(function (res){
            if(res.data.code=200){
              var change = _this.Market+"的"+_this.Variety+"预计为"+res.data.result+"元/千克";
              document.getElementById("predictprice").innerHTML=change;
            }else{
              alert("无法预测该数据");
            }
          },function (err) {
            alert(err);
          });
        },

        onSubmit(){
          var _this = this;
          var medata = "";
          this.$axios.get("http://139.155.228.86:8083/price/getPrice?market="+_this.Market+"&pageNum=-1&variety="+_this.Variety).then(function (res) {
            if(res.data.data.length==0){
              alert("暂无数据");
            }else{
              if(res.data.data.length<100){
                for(var i = 0; i < res.data.data.length ; i++){
                  medata = medata + "{\"date\": \"" + res.data.data[i].date+ "\" ,\"avgPrice\": \""+ res.data.data[i].avgPrice + "\"},"
                }
              }else{
                for(var i = res.data.data.length-100; i < res.data.data.length ; i++){
                  medata = medata + "{\"date\": \"" + res.data.data[i].date+ "\" ,\"avgPrice\": \""+ res.data.data[i].avgPrice + "\"},"
                }
              }
              medata = medata.substring(0,medata.length-1)
              medata = "[" + medata + "]";
              medata = "{ \"data\":" + medata + "}";
              medata = JSON.parse(medata);
              console.log(medata);
              _this.drawLine(medata);
              _this.getPrediction();
            }
          });
        }
      },

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
    .sm-table{
      float: left;
      width:60%;
      height:200px;
    }
    .sm-left{
      float: left;
      width:15%;
    }
  }
  /**大屏768*/
  @media only screen and (min-width:768px){
    .lg-chart{
      float: left;
      width:60%;
      height:500px;
    }
    .lg-table{
      float: left;
      width:28%;
      height:200px;
    }
    .lg-left{
      float: left;
      width:0%;
    }
  }
  .SelectorContainer{
    position: relative;
    top: 30px;
    left: -3%;
    width: 100%;
    height: 20%;
  }

</style>
