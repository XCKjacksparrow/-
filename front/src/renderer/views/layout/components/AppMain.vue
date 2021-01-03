<template>
  <section class="app-main">
    <transition  name="bounce" enter-active-class="animate__animated animate__rollIn">
      <router-view></router-view>
    </transition>
  </section>
</template>

<script>
export default {
  name: 'AppMain',
  computed: {},
  data() {
    return {
    }
  }
}
</script>

