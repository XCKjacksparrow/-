<template>

            
    <div id="app">
 
        <!-- 阿里云视频播放器样式 -->
    <link
      rel="stylesheet"
      href="https://g.alicdn.com/de/prismplayer/2.8.1/skins/default/aliplayer-min.css"
    />
    <!-- 阿里云视频播放器脚本 -->
    <!-- <script
      charset="utf-8"
      type="text/javascript"
      src="https://g.alicdn.com/de/prismplayer/2.8.1/aliplayer-min.js"
    /> -->

    <!-- 定义播放器dom -->
    <div id="J_prismPlayer" class="prism-player" />
    </div>
</template>
<script>
import { getPlayAuth } from '../../api/vod'
export default {
  //   asyncData({ params, error }) {
  //     return vod.getPlayAuth(params.vid).then((response) => {
  //       // console.log(response.data.data)
  //       return {
  //         vid: params.vid,
  //         playAuth: response.data.data.playAuth,
  //       };
  //     });
  //   },
  data() {
    return {
      videoId: '',
      playAuth: ''
    }
  },

  methods: {
    createdLoca() {
      var script = document.createElement('script')
      script.type = 'text/javascript'
      script.async = true
      script.src = 'https://g.alicdn.com/de/prismplayer/2.8.1/aliplayer-min.js'
      document.head.appendChild(script)
    }
  },
  created() {
    this.createdLoca()
    if (this.$route.params && this.$route.params.id) {
      this.videoId = this.$route.params.id
      console.log(this.$route.params.id)
      getPlayAuth(this.videoId).then((response) => {
        this.playAuth = response.data.playAuth
        console.log(this.playAuth)
      })
    }
  },

  mounted() {
    this.createdLoca()

    var player = new Aliplayer(
      {
        id: 'J_prismPlayer',
        vid: this.videoId, // 视频id
        playauth: this.playAuth, // 播放凭证
        encryptType: '1', // 如果播放加密视频，则需设置encryptType=1，非加密视频无需设置此项
        width: '100%',
        height: '500px',
        autoplay: true
      },
      function(player) {
        console.log('播放器创建成功')
      }
    )
  }
}
</script>
