import Vue from 'vue'
import Router from 'vue-router'
import PriceCompared from '@/components/PriceCompared'
import PricePrediction from '@/components/PricePrediction'
import RegionalQuote from '@/components/RegionalQuote'
import InfoCount from '@/components/InfoCount'
import FindPrice from "@/components/FindPrice";

Vue.use(Router)

export default new Router({
  routes: [
    {
      path: '/',
      name: 'InfoCount',
      component: InfoCount
    },
    {
      path: '/priceCompared',
      name: 'PriceCompared',
      component: PriceCompared
    },
    {
      path: '/pricePrediction',
      name: 'PricePrediction',
      component: PricePrediction
    },
    {
      path: '/region',
      name: 'RegionalQuote',
      component: RegionalQuote
    },
    {
      path: '/monitor',
      name: 'InfoCount',
      component: InfoCount
    },
    {
      path: '/FindPrice',
      name: 'FindPrice',
      component: FindPrice
    }


  ]
})
