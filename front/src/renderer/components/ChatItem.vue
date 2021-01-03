<template>
    <div>
        <el-avatar style="float:left;position: relative"
                   src="https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png"></el-avatar>
        <!-- 这里显示ip、身份                               -->
        <el-row style="padding:10px">
            【{{message.fromIdentity}}】{{message.username}}
            <el-tag type="danger" v-if="message.isMe">我</el-tag>
        </el-row>
        <!--这里显示内容                               -->
        <el-row style="margin-left: 40px">
            <el-tag type="info">{{message.message}}</el-tag>
        </el-row>
    </div>
</template>

<script>
  export default {
    props: {
      message: {
        type: Object,
        require: true
      }
    }
  }
</script>

<style scoped>

</style>
