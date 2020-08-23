<template>
  <div id="app" :style="backgroundDiv">
    <Header/>
    <router-view/>
  </div>
</template>

<script>
  import Header from './components/Header'
export default {
  name: 'App',
  components:{
    Header,
  },
  data() {
    return {
      backgroundDiv: {
        backgroundColor: '#FFFAFA',
        position: 'absolute',
        backgroundRepeat: 'repeat',
        backgroundSize: 'cover',
      }
    }
  }
}
</script>

<style>
#app {
  text-align: center;
  color: #2c3e50;
  height: 100%;
  width: 100%;
}


</style>
