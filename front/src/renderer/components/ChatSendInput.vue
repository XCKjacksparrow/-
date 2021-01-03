<template>
    <div>
        <el-input placeholder="请输入内容" v-model="message" @keydown.enter.native="sendChatToAll">
                                <span slot="prepend">
                                    发送消息：
                                </span>
            <el-button slot="append" @click="sendChatToAll" icon="el-icon-s-promotion"></el-button>
        </el-input>
    </div>
</template>

<script>
  export default {}
</script>

<style scoped>

</style>
