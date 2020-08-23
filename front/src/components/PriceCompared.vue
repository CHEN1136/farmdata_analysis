<template>
  <div>
    <div class="SelectorContainer container" style="position: center;margin-top: 20px">
      <el-form :inline="true" :model="formInline" class="demo-form-inline" size="large">
        <div class="row">
          <div class="col-lg-11">
        <el-form-item label="区域A">
          <el-select
            v-model="value1" clearable  @change="showMarketA"
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
        <el-form-item label="市场A">
          <el-select
            v-model="value2" clearable    @change="getMarketA"
            style="margin-left: 20px;"
            placeholder="请选择市场">
            <el-option
              v-for="item in MarketdataA"
              :key="item.value"
              :label="item.label"
              :value="item.label">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="区域B">
          <el-select
            v-model="value3" clearable  @change="showMarketB"
            style="margin-left: 28px;"
            placeholder="请选择区域">
            <el-option
              v-for="item in Regionaldata"
              :key="item.value"
              :label="item.label"
              :value="item.label">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="市场B">
        <el-select
          v-model="value4" clearable    @change="getMarketB"
          style="margin-left: 20px;"
          placeholder="请选择市场">
          <el-option
            v-for="item in MarketdataB"
            :key="item.value"
            :label="item.label"
            :value="item.label">
          </el-option>
        </el-select>
      </el-form-item>
        <el-form-item label="品类">
          <el-select
            v-model="value5" clearable  @change="showCategory"
            style="margin-left: 28px;"
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
              v-model="value6" clearable  @change="getVariety"
            style="margin-left: 28px;"
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
            <el-form-item style="margin-top: 20px;">
              <el-button type="primary" @click="onSubmit">查询</el-button>
            </el-form-item>
          </div>
        </div>
      </el-form>
    </div>
    <div style="height: 40px"></div>
    <div style="float: left;width: 12%;">&nbsp;</div>
    <div class="lg-chart sm-chart">
      <div id="InfoByData" style="width: 90%;height: 100%;"></div>
    </div>
    <div class="lg-left sm-left">&nbsp;</div>
    <div class="lg-table sm-table">
      <el-table
        :data="tableData"
        height="100%"
        style="width: 100%">
        <el-table-column
          prop="date"
          label="日期"
          width="135">
        </el-table-column>
        <el-table-column
          prop="marketA"
          label="市场A(单位：元/千克)"
          width="200">
        </el-table-column>
        <el-table-column
          prop="marketB"
          label="市场B(单位：元/千克)"
          width="200">
        </el-table-column>
      </el-table>
    </div>
    <div style="float: left;width: 100%;height: 40px">&nbsp;</div>
  </div>
</template>

<script>
  export default {
    name: "PriceCompared",
    props: ["userJson"],
    data() {
      return {
        formInline: {
          region_a: '',
          market_a: '',
          region_b: '',
          market_b: '',
          category: '',
          varity: "",
          user: '',
        },
        chart: null,
        Regionaldata: null,
        MarketdataA: null,
        MarketdataB: null,
        Categorydata: null,
        Varietydata: null,
        value1: [],
        value2: [],
        value3: [],
        value4: [],
        value5: [],
        value6: [],
        Category:"",
        Variety: "",
        RegionA: "",
        RegionB: "",
        MarketA: "",
        MarketB: "",
        tableData: null,
      };
    },

    mounted() {
      this.loadData();
    },
    methods: {

      //加载横向直方图
      drawLine(data) {
        let myChart = this.$echarts.init(document.getElementById("InfoByData"));
          myChart.hideLoading();
          // 填入数据
          myChart.setOption({
            legend: {},
            tooltip: {},
            dataset: {
              source:
              data.data
            },
            xAxis: {type: 'category'},
            yAxis: {},
            // Declare several bar series, each will be mapped
            // to a column of dataset.source by default.
            series: [
              {type: 'bar'},
              {type: 'bar'},
            ]
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
      },

      //选中品类操作
      showCategory(event){
        var _this = this
        _this.value6 = null
        _this.Category = event; //获取option对应的value值
        console.log("你选中了",_this.Category)
        //加载品种数据
        this.$axios.get("http://139.155.228.86:8083/variety/getVarieties/"+ _this.Category).then(function (res) {
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

      showMarketA(event){
        var _this = this
        _this.value2 = null
        _this.RegionA = event; //获取option对应的value值
        console.log("你选中了",_this.RegionA)
        //加载市场数据
        this.$axios.get("http://139.155.228.86:8083/market/getMarkets/"+_this.RegionA).then(function (res) {
          var medata = ""

          for(var i = 0; i < res.data.data.length ; i++){
            medata = medata + "{\"value\":  \"选项" + (i+1) + "\" ," + "\"label\": \""+ res.data.data[i].marketName +"\"},"
          }
          medata = medata.substring(0,medata.length-1)
          medata = "[" + medata + "]"
          medata = "{ \"data\":" + medata + "}"
          medata = JSON.parse(medata)
          _this.MarketdataA = medata.data
        })
      },

      showMarketB(event){
        var _this = this
        _this.value4 = null
        _this.RegionB = event; //获取option对应的value值
        console.log("你选中了",_this.RegionB)
        //加载市场数据
        this.$axios.get("http://139.155.228.86:8083/market/getMarkets/"+_this.RegionB).then(function (res) {
          var medata = ""

          for(var i = 0; i < res.data.data.length ; i++){
            medata = medata + "{\"value\":  \"选项" + (i+1) + "\" ," + "\"label\": \""+ res.data.data[i].marketName +"\"},"
          }
          medata = medata.substring(0,medata.length-1)
          medata = "[" + medata + "]"
          medata = "{ \"data\":" + medata + "}"
          medata = JSON.parse(medata)
          _this.MarketdataB = medata.data
        })
      },

      getMarketA(event){
        this.MarketA = event
      },
      getMarketB(event){
        this.MarketB = event
      },
      getVariety(event){
        this.Variety = event
      },

      onSubmit(){
        console.log('submit!');
        //合并AB市场数据
        var _this = this;
        var url = "http://139.155.228.86:8083";
        var medataA = "";
        var medata = "";
        var dataexist = 1;
        this.$axios.get(url + "/price/getPrice?market="+_this.MarketA+"&pageNum=-1&variety="+_this.Variety).then(function (res) {
          if(res.data.data.length<5){
            alert("暂无市场A数据");
            dataexist = 0;
          }else{
            for(var i = res.data.data.length-5; i < res.data.data.length ; i++){
              medataA = medataA + "{\"date\": \"" + res.data.data[i].date+ "\" ,\"marketA\": \""+ res.data.data[i].avgPrice + "\"},"
            }
            medataA = medataA.substring(0,medataA.length-1);
            medataA = "[" + medataA + "]";
            medataA = "{ \"data\":" + medataA + "}";
            medataA = JSON.parse(medataA)
          }
        })

        if(dataexist==1){
          this.$axios.get(url + "/price/getPrice?market="+_this.MarketB+"&pageNum=-1&variety="+_this.Variety).then(function (res) {
            if(res.data.data.length<5){
              alert("暂无市场B数据");
            }else{
              var j = 0;
              for(var i = res.data.data.length-5; i < res.data.data.length ; i++){
                medata = medata + "{\"date\": \"" + res.data.data[i].date+ "\" ,\"marketA\": \""+ medataA.data[j].marketA + "\" ,\"marketB\": \""+ res.data.data[i].avgPrice + "\"},";
                j++;
              }
              medata = medata.substring(0,medata.length-1);
              medata = "[" + medata + "]";
              medata = "{ \"data\":" + medata + "}";
              medata = JSON.parse(medata);
              console.log(medata);
              _this.tableData = medata.data;
              //加载直方图
              _this.drawLine(medata)
            }
          })
        }
      }
    }
  };
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
      width:50%;
      height:500px;
    }
    .lg-table{
      float: left;
      width:28%;
      height:450px;
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
