<template>
  <div class="header-wrap wow fadeInUp" style="background-color: rgb(49, 49, 49);">
    <div class="container">
      <div class="row">
        <div class="col-lg-3 navbar navbar-expand-lg navbar-light">
          <div class="header_logo"><a href="/"><img src="../assets/logo_2.png"></a></div>
          <button class="navbar-toggler collapsed" id="coll" type="button" data-toggle="collapse" data-target="#navbarSupportedContent" aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation"> <span class="navbar-toggler-icon"></span> </button>
        </div>
        <div class="col-lg-9">
          <nav class="navbar navbar-expand-lg navbar-light"> <a class="navbar-brand" href="/"></a>
            <div class="collapse navbar-collapse" id="navbarSupportedContent" aria-expanded="false" >
              <button class="close-toggler" type="button" data-toggle="offcanvas"> </button>
              <ul class="navbar-nav mr-auto">
                <li class="nav-item active"><a class="nav-link" href="/#/monitor" @click="toInfoCount"><font color="white" style="font-size: 17px">主页</font></a>
                </li>
                <li class="nav-item"><a class="nav-link" href="/#/region" @click="toRegionalQuote"><font color="white" style="font-size: 17px;margin-left: 15px;margin-right: 15px">区域行情</font></a>
                </li>
                <li class="nav-item"><a class="nav-link" href="/#/FindPrice" @click="toFindPrice"><font color="white" style="font-size: 17px;margin-left: 15px;margin-right: 15px">价格走势</font></a>
                </li>
                <li class="nav-item"><a class="nav-link" href="/#/priceCompared" @click="toPriceCompared"><font color="white" style="font-size: 17px;margin-left: 15px;margin-right: 15px">价格对比</font></a>
                </li>
                <li class="nav-item"><a class="nav-link" href="/#/pricePrediction" @click="toPricePrediction"><font color="white" style="font-size: 17px;margin-left: 15px;margin-right: 15px">价格预测</font></a></li>
              </ul>
            </div>
          </nav>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
    export default {
      name: "Header",
      methods: {
        toInfoCount(){
          window.location.replace('/#/monitor')
          window.location.reload()
        },
        toRegionalQuote(){
          window.location.replace('/#/region')
          window.location.reload()
        },
        toFindPrice(){
          window.location.replace('/#/FindPrice')
          window.location.reload()
        },
        toPriceCompared(){
          window.location.replace('/#/priceCompared')
          window.location.reload()
        },
        toPricePrediction(){
          window.location.replace('/#/pricePrediction')
          window.location.reload()
        }
      }
    }
</script>

<style>
  a{-webkit-transition: all .3s ease; -moz-transition: all .3s ease; transition: all .3s ease;}
  ul, ol{list-style:none;}
  .header-wrap{}
  .navbar{padding:0;}
  .navbar-brand{display:none;}
  .navbar li{line-height:inherit; padding: 22px 0;}
  .navbar-light .navbar-nav .nav-link{font-size:14px; padding: 12px 16px; color:#727280; text-transform:uppercase; z-index:1000; font-weight:bold;}
  .caret{display: inline-block !important;
    font-size: 16px !important; color: #adadad !important;
    padding-left: 7px;}
  .navbar-light .navbar-nav .active>.nav-link{color: #727280 !important;}
  .header_logo img{width: 270px;}
  .navbar li span{display: block; text-align: center; font-size: 24px;}
  .header-wrap .dropdown a{border-radius:6px; position:relative; z-index:1000;}
  .header-wrap .dropdown-menu:before{content:""; width: 0; height: 0; border-left:10px solid transparent; border-right:10px solid transparent; border-bottom: 10px solid #fff; position: absolute; left: 50%; margin-left: -5px; z-index:1000; top:-10px;}
  .navbar li a{position:relative;}
  .navbar li:last-child a:after{border-radius:30px;}
  .navbar-light .navbar-nav .nav-item:hover .nav-link, .navbar-light .navbar-nav .nav-link:hover{color:#fff !important; background: #ffb900; }
  .tp-bannertimer{display:none;}
  .navbar{float: right;}
  .navbar-light .navbar-nav .nav-item:hover .nav-link .caret, .navbar-light .navbar-nav .nav-link:hover .caret{color: #fff !important;}
  .close-toggler{display: none;}
  .submenu li:last-child{border: none;}
  .navbar-nav > li:hover > ul {
    top: 100%;
    opacity: 1;
    visibility: visible;
  }

  .navbar-nav > li > ul > li {
    position: relative;
    float: none;
    width: 100%;
    border-bottom: 1px solid #e8e8e8;
    padding: 0;
  }

  .submenu>li:hover>a {
    background: #ffb900;
    color: #fff !important;
  }

  .navbar-nav > li > ul > li > a {text-decoration:none;
    position: relative;
    display: block;
    padding: 10px 20px;
    font-weight: 600;
    font-size: 14px;
    color: #000 !important;
    word-wrap: break-word;
    transition: all 500ms ease;
    -moz-transition: all 500ms ease;
    -webkit-transition: all 500ms ease;
    -ms-transition: all 500ms ease;
    -o-transition: all 500ms ease;
  }
  .navbar-nav li> .submenu >li a+i{display: none;
    position: absolute;
    right: 15px;
    top: 7px;
    font-size: 24px;
    cursor: pointer;
    color: #0b3c5d;
    background: #fff;
    width: 30px;
    height: 30px;
    text-align: center;
    line-height: 30px;}

  .navbar-nav>li>a+i{
    display: none;
    position: absolute;
    right: 15px;
    top: 7px;
    font-size: 24px;
    cursor: pointer;
    color: #0b3c5d;
    background: #fff;
    width: 30px;
    height: 30px;
    text-align: center;
    line-height: 30px;
    z-index: 1000;
  }
  .navbar-nav li{position: relative;}
  .navbar-nav > li > ul {list-style: none;
    position: absolute;
    left:0;
    top: 200%;
    width: 240px;
    padding: 0px;
    z-index: 100;
    background: #fff !important;
    visibility: hidden;
    opacity: 0;
    border-radius: 0px 2px 2px 2px;
    -ms-border-radius: 0px 2px 2px 2px;
    -webkit-border-radius: 0px 2px 2px 2px;
    -moz-border-radius: 0px 2px 2px 2px;
    -o-border-radius: 0px 2px 2px 2px;
    transition: all 500ms ease;
    -moz-transition: all 500ms ease;
    -webkit-transition: all 500ms ease;
    -ms-transition: all 500ms ease;
    -o-transition: all 500ms ease;
    box-shadow: 0 0 18px rgba(0, 0, 0, 0.38);
  }

  .submenu li .submenu_right{display: none;}
  .submenu_right{background:#fff; position: absolute; top: 0; left: 100%; width: 200px;}
  .submenu_right li{padding-bottom: 0; padding: 0px; border-bottom: 1px solid #e8e8e8;}
  .submenu_right li a{display: block; font-size: 14px; font-weight: 600; padding: 10px 20px; color: #000; text-decoration: none;}
  .submenu_right li a:hover{background: #ffb900; color: #fff !important;}
  .submenu li:hover .submenu_right{display: block;}
  .right_angle{position: absolute; top:8px; right: 10px; font-size: 18px !important;}
  @media screen and (max-width: 990px){
    .header-wrap{padding: 20px 0;}
    .navbar-nav {clear: both;}
    .navbar{float: none;}
    .navbar-toggler{position: absolute;
      top: 10px;
      right: 0; border: 1px solid #eee;}

    .navbar-collapse {
      position: fixed;
      top: 0px;
      bottom: 0;
      left: 100%;
      width: 60%;
      padding-right: 1rem;
      padding-left: 1rem;
      overflow-y: auto;
      visibility: hidden;
      background-color: rgba(0,0,0,0.8);
      transition: visibility .3s ease-in-out, -webkit-transform .3s ease-in-out;
      transition: transform .3s ease-in-out, visibility .3s ease-in-out;
      transition: transform .3s ease-in-out, visibility .3s ease-in-out, -webkit-transform .3s ease-in-out;
      z-index: 10000;
    }
    .navbar-nav > li > ul {width: 100%; position: static; display: none; margin-left: 0; opacity: 1; visibility: visible;}
    .navbar-collapse.show {
      visibility: visible;
      -webkit-transform: translateX(-100%);
      transform: translateX(-100%);
    }
    .close-toggler {
      background: none;
      color: #fff;
      padding: 5px;
      border: none;
      margin-top: 10px;
      font-size: 24px;
      float: right;
      display: block;
    }
    .perfect_home_wrap .readmore{margin-top: 30px; display: inline-block;}
    .navbar-nav li a+i {
      display: block;
    }
    .insta_pics img{width: 80px;}
    .navbar-light .navbar-nav .nav-link{color: #fff;}
    .navbar li{padding: 0;}
    .navbar-light .navbar-nav .active>.nav-link, .navbar-light .navbar-nav .nav-link.active, .navbar-light .navbar-nav .nav-link.show, .navbar-light .navbar-nav .show>.nav-link{color: #fff !important;}
    .navbar li span{display: inline-block; width: 30px; padding-right: 10px;}
    .navbar-nav > li > ul > li > a:hover{background: #ffb900;}
  }

  @media screen and (max-width: 767px){
    .popular_wrap .mt_md{margin-top: 20px;}
    .popular_wrap .row .col-md-4:nth-child(1) .popular_img{margin-bottom: 15px;}
    .property_details .heart_info{float: none;}
    section.our_team_wrap h1{font-size: 30px;}
    .innerTeamWrp .team_wrp{margin: 0 auto !important;}
    .four-zero-page h2{font-size: 150px;}
    .four-zero-page h3{font-size: 24px; letter-spacing: 5px;}
  }
  @media screen and (max-width: 480px){
    .four-zero-page p{letter-spacing: 0;}
    .four-zero-page h3{font-size: 18px; letter-spacing: 2px;}
    .four-zero-page h2{font-size: 70px;}
    .navbar{padding: 0 15px;}
    .navbar-toggler{right: 15px;}
    .header_logo{margin-top: 10px;}
    .nav>li>a{padding: 10px 8px; font-size: 14px;}
    .hotelLinks li{width: 50%;}
    .header_logo{width: 200px;}
    .popular_wrap h1, .buy-wrap h1, .title h1, .perfect_home_wrap h1, .sliderTxt h1{
      font-size: 28px;
    }
    .sliderTxt p{font-size: 14px; letter-spacing: 1px;}
    .property_box h3 a{font-size: 18px;}
    .property_location i{font-size: 14px;}
    .title h1 span{font-size: 14px; line-height: 24px; margin-top: 6px; font-weight: normal;}
    .start_btn a {
      display: block;
      margin-top: 20px;
      width: 220px;
      margin-left: auto;
      margin-right: auto;
    }
    section.our_team_wrap .team_member:hover .team_img img{
      right: -15px;
    }
  }


</style>
