<template>
  <div id="app">
    <transition name="transitionName" leave-active-class="animate__animated  animate__zoomOut">
      <router-view v-if="RouterState"></router-view>
    </transition>
  </div>
</template>

<script>
export default {
  name: 'App',
  provide() {
    return {
      reload: this.reload
    }
  },
  data() {
    return {
      RouterState: true
    }
  },
  methods: {
    reload() {
      console.log('执行reload')
      this.RouterState = false
      this.$nextTick(() => {
        this.RouterState = true
      })
    }
  }
}
</script>
<style lang="scss">
@import "./styles/index.scss"; // 全局自定义的css样式
</style>
