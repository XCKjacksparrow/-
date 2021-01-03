<template>
  <div class="menu-wrapper">
    <template v-for="item in routes" v-if="!item.hidden&&item.children">

      <template v-if="hasRole(item)">
        <router-link v-if="hasOneShowingChildren(item.children) && !item.children[0].children&&!item.alwaysShow" :to="item.path+'/'+item.children[0].path"
                     :key="item.children[0].name">
          <el-menu-item :index="item.path+'/'+item.children[0].path" :class="{'submenu-title-noDropdown':!isNest}">
            <svg-icon v-if="item.children[0].meta&&item.children[0].meta.icon" :icon-class="item.children[0].meta.icon"></svg-icon>
            <span v-if="item.children[0].meta&&item.children[0].meta.title" slot="title">{{item.children[0].meta.title}}</span>
          </el-menu-item>
        </router-link>

        <el-submenu v-else :index="item.name||item.path" :key="item.name">
          <template slot="title">
            <svg-icon v-if="item.meta&&item.meta.icon" :icon-class="item.meta.icon"></svg-icon>
            <span v-if="item.meta&&item.meta.title" slot="title">{{item.meta.title}}</span>
          </template>

          <template v-for="child in item.children" v-if="!child.hidden&&hasRole(child)" >
            <sidebar-item :is-nest="true" class="nest-menu" v-if="child.children&&child.children.length>0" :routes="[child]" :key="child.path"></sidebar-item>

            <router-link v-else :to="item.path+'/'+child.path" :key="child.name">
              <el-menu-item :index="item.path+'/'+child.path">
                <svg-icon v-if="child.meta&&child.meta.icon" :icon-class="child.meta.icon"></svg-icon>
                <span v-if="child.meta&&child.meta.title" slot="title">{{child.meta.title}}</span>
              </el-menu-item>
            </router-link>
          </template>
        </el-submenu>
      </template>

    </template>
  </div>
</template>

<script>
  import { mapState, mapGetters } from 'vuex'
  import { Message } from 'element-ui'
export default {
  name: 'SidebarItem',
  props: {
      routes: {
        type: Array
      },
      isNest: {
        type: Boolean,
        default: false
      }
  },
  methods: {
      hasOneShowingChildren(children) {
        const showingChildren = children.filter(item => {
          return !item.hidden
        })
        if (showingChildren.length === 1) {
          return true
        }
        return false
      },
      hasRole(route) {
      // console.log(route)
      // console.log(this.token)
        if (this.token.length == 0) {
          this.$router.push('/login')
        }
        const roles = [this.token.role]
        if (roles.length > 0) {
          return route.meta.role === roles[0] || route.meta.role === 'all'
        } else {
          Message.error('您尚未拥有任何角色')
          return false
        }
      }

  },
  computed: {
      ...mapGetters(['token'])
  }
}
</script>
<style>
  .svg-icon{
    vertical-align: middle!important;
    font-size: 22px !important;
  }
</style>