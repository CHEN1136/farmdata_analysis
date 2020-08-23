<template>
  <div>
    <div class="titleContainer" style="position: center">
      <h3>农产品价格查询及走势查看</h3>
    </div>
    <div class="sm-formContainer lg-formContainer" style="position: center">
      <el-form :inline="true" :model="formInline" class="demo-form-inline">
        <el-form-item label="区域：" min-width="20%">
          <el-select v-model="region" placeholder="请选择区域" size="small" @change="getMarketsByRegion" clearable>
            <el-option
              v-for="item in regionOptions"
              :key="item.marketRegion"
              :label="item.marketRegion"
              :value="item.marketRegion">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="市场：" min-width="20%">
          <el-select v-model="formInline.market" placeholder="请选择市场" size="small" clearable>
            <el-option
              v-for="item in marketOptions"
              :key="item.marketName"
              :label="item.marketName"
              :value="item.marketName">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="品类：" min-width="20%">
          <el-select v-model="category" placeholder="请选择品类" size="small" @change="getVarietiesByCategory" clearable>
            <el-option
              v-for="item in categoryOptions"
              :key="item.categoryName"
              :label="item.categoryName"
              :value="item.categoryName">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="品种：" min-width="20%">
          <el-select v-model="formInline.variety" placeholder="请选择品种" size="small" clearable>
            <el-option
              v-for="item in varietyOptions"
              :key="item.varietyName"
              :label="item.varietyName"
              :value="item.varietyName">
            </el-option>
          </el-select>
        </el-form-item>
        <el-form-item min-width="20%">
          <el-button type="primary" @click="onSubmit" size="small">查询</el-button>
        </el-form-item>
      </el-form>
    </div>
    <div>
      <div class="sm-tableContainer lg-tableContainer" style="position: center">
        <el-table :data="tableData" border style="width: 100%" align="center" size="small">
          <el-table-column
            prop="date"
            label="日期"
            min-width="17%"
            align="center">
          </el-table-column>
          <el-table-column
            prop="region"
            label="区域"
            min-width="11%"
            align="center">
          </el-table-column>
          <el-table-column
            prop="marketName"
            label="市场"
            min-width="30%"
            align="center">
          </el-table-column>
          <el-table-column
            prop="varietyName"
            label="品种名"
            min-width="12%"
            align="center">
          </el-table-column>
          <el-table-column
            prop="lowestPrice"
            label="最低价"
            min-width="10%"
            align="center">
          </el-table-column>
          <el-table-column
            prop="highestPrice"
            label="最高价"
            min-width="10%"
            align="center">
          </el-table-column>
          <el-table-column
            prop="avgPrice"
            label="平均价"
            min-width="10%"
            align="center">
          </el-table-column>
        </el-table>
        <el-pagination
          background
          layout="prev, pager, next"
          :total="total"
          :current-page.sync="currentPage"
          :page-count="allPage"
          hide-on-single-page="true"
          @current-change="getPageByNumber">
          hide-on-single-page="true"
          @current-change=""
          @prev-click="getPrevPage"
          @next-click="getNextPage">
        </el-pagination>
      </div>
      <div class="sm-graphContainer lg-graphContainer" style="position: center">
        <ve-line :data="chartData.chartData" :settings="chartData.chartSettings"></ve-line>
      </div>
    </div>

  </div>
</template>
<script>
    export default {
        name: "FindPrice",
      data() {
        return {
          currentPage:1,
          allPage:0,
          regionOptions: [],
          marketOptions: [],
          categoryOptions: [],
          varietyOptions: [],
          region: '',
          category: '',
          formInline: {
            market: '',
            variety: ''
          },
          tableData: [],
          total: 0,
          chartData: {
            chartSettings: {
              //设置中文图例
              labelMap: {
                //date对应后台返回json数据的key，具体值根据接口字段来修改
                date: "日期",
                //total对应后台返回son数据的value，具体值根据接口字段来修改
                avgPrice: "价格"
              }
            },
            chartData: {
              //这里的格式必须是对应上面后台接口的字段才能显示图例
              columns: ['date', 'avgPrice'],
              rows: []
            },
          }
        }
      },
      beforeRouteEnter(to, from, next){
        window.document.body.style.backgroundColor = '';
        next();
      },
      beforeRouteLeave(to, from, next){
        window.document.body.style.backgroundColor = '';
        next();
      },
      mounted(){
          this.getRegions();
          this.getCategories();
      },
      methods: {
        onSubmit: function () {
          console.log(this.formInline.market + this.formInline.variety);
          if(this.formInline.market=='' && this.formInline.variety=='')
          {
            this.$message({
              message: '市场和品种不能为空，请重新选择',
              center: true,
              type: 'warning'
            });
          }
          else if(this.formInline.market=='' && this.formInline.variety!='')
          {
            this.$message({
              message: '市场不能为空，请重新选择',
              center: true,
              type: 'warning'
            });
          }
          else if(this.formInline.market!='' && this.formInline.variety=='')
          {
            this.$message({
              message: '品种不能为空，请重新选择',
              center: true,
              type: 'warning'
            });
          }
          else{
            this.$axios({
              method: 'get',
              url: 'http://139.155.228.86:8083/price/getPrice',
              params: {
                market: this.formInline.market,
                pageNum: 1,
                variety: this.formInline.variety
              }
            }).then((res) => {
              console.log(res.data.data);
              this.tableData = res.data.data.records;
              this.total = res.data.data.total;
              this.allPage = res.data.data.pages;
              //this.tableData.reverse();
              if(this.tableData.length==0){
                this.$message({
                  message: this.formInline.market+'暂无'+this.formInline.variety+'的相关数据',
                  center: true,
                });
              }
            })
            this.$axios({
              method: 'get',
              url: 'http://139.155.228.86:8083/price/getPrice',
              params: {
                market: this.formInline.market,
                pageNum: -1,
                variety: this.formInline.variety
              }
            }).then((res) => {
              this.chartData.chartData.rows = res.data.data;
              //this.tableData.reverse();
            })

          }
        },
        getRegions: function () {
          this.$axios({
            method: 'get',
            url: 'http://139.155.228.86:8083/market/getRegions',
            data: {}
          }).then((res) => {
            console.log(res.data.data);
            this.regionOptions = res.data.data;
          }).catch(function (error) {
            that.$message.error("系统错误");
          })
        },

        getMarketsByRegion: function () {
          console.log(this.region);
          this.formInline.market= null
          this.$axios({
            method: 'get',
            url: ('http://139.155.228.86:8083/market/getMarkets/' + this.region),
            data: {}
          }).then((res) => {
            console.log(res.data.data);
            this.marketOptions = res.data.data;
          }).catch(function(error){
            that.$message,error("系统错误")
          })
        },
        getCategories: function () {
          this.$axios({
            method: 'get',
            url: 'http://139.155.228.86:8083/variety/getCategories',
            data: {}
          }).then((res) => {
            console.log(res.data.data);
            this.categoryOptions = res.data.data;
          }).catch(function (error) {
            that.$message.error("系统错误");
          })
        },
        getVarietiesByCategory: function () {
          console.log(this.category);
          this.formInline.variety = null
          this.$axios({
            method: 'get',
            url: 'http://139.155.228.86:8083/variety/getVarieties/' + this.category,
            data: {}
          }).then((res) => {
            console.log(res.data.data);
            this.varietyOptions = res.data.data;
          }).catch(function (error) {
            that.$message.error("系统错误");
          })
        },
        getPageByNumber: function (currentPage) {
          this.currentPage = currentPage;
          this.$axios({
            method: 'get',
            url: 'http://139.155.228.86:8083/price/getPrice',
            params: {
              market: this.formInline.market,
              variety: this.formInline.variety,
              pageNum: this.currentPage
            },
            data: {}
          }).then((res) => {
            console.log(this.currentPage);
            console.log(res.data.data);
            this.tableData = res.data.data.records;
            this.total = res.data.data.total;
            this.allPage = res.data.data.pages;
            //this.tableData.reverse();
          }).catch(function (error) {
            that.$message.error('系统错误');
          })
        },
      }
    }
</script>

<style scoped>
    /**小屏0px-768px*/
  @media only screen and (min-width:0px) and (max-width:768px){
    .sm-formContainer{
      margin-top: 10px;
      margin-left: 5%;
      width: 90%;
      height: 20%;
    }
    .sm-tableContainer{
      margin-top: 10px;
      width: 90%;
      height: 50%;
      margin-left: 5%;
    }
    .sm-graphContainer{
      margin-top: 10px;
      margin-left: 5%;
      width: 90%;
      height: 50%;;
    }
  }
  /**大屏768*/
  @media only screen and (min-width:768px){
    .lg-formContainer{
      margin-top: 10px;
      margin-left: 5%;
      width: 90%;
      height: 20%;
    }
    .lg-tableContainer{
      float: left;
      margin-top: 10px;
      width: 50%;
      height: 50%;
      margin-left: 5%;
    }
    .lg-graphContainer{
      float: right;
      margin-top: 10px;
      margin-right: 5%;
      width: 40%;
      height: 50%;;
    }
  }
    .titleContainer{
      margin-top: 10px;
      margin-left: 5%;
      width: 90%;
      height: 20%;
    }
</style>
