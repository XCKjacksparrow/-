<template>
  <div>
    <!-- <el-card>
      <div>
        课程图片
        <article  style="height: 357px;">
            <img :src="course.cover" :alt="course.title"  width="350px" height="350px"/>
        </article>
        <el-image
          style="width: 100px; height: 100px"
          :src="course.cover"
          fit="fit"
        >
        </el-image>

        <aside>
          <section>
            <h2>
              <span>{{ course.title }}</span>
            </h2>
          </section>
        </aside>
        <aside>
          <ol>
            <li>
              <p>&nbsp;</p>
              <aside>
                <span>课时数</span>
                <br />
                <h6>20</h6>
              </aside>
            </li>
            <li>
              <p>&nbsp;</p>
              <aside>
                <span>浏览数</span>
                <br />
                <h6>999</h6>
              </aside>
            </li>
          </ol>
        </aside>
        <div></div>
      </div>

      <div>
        <article>
          <section>
            <div>
              <div>
                <section id="c-i-tabTitle">
                  <a name="c-i" title="课程详情">课程详情</a>
                </section>
              </div>
              <article class="ml10 mr10 pt20">

                <div>
                  <h6>
                    <span>课程大纲</span>
                  </h6>
                  <section>
                    <div>
                      <menu id="lh-menu">
                        <ul>

                          <li v-for="chapter in chapterList" :key="chapter.id">
                            <a
                              :title="chapter.title"
                              href="javascript: void(0)"
                            >
                              <em />
                              {{ chapter.title }}
                            </a>
                            <ol style="display: block">
                              <li
                                v-for="video in chapter.children"
                                :key="video.id"
                              >
                                <a
                                  @click="startplayer(video.videoSourceId)"
                                  :title="video.title"
                                  target="_blank"
                                >
                                  <em>&nbsp;</em>
                                  {{ video.title }}
                                </a>
                              </li>
                            </ol>
                          </li>
                        </ul>
                      </menu>
                    </div>
                  </section>
                </div>

              </article>
            </div>
          </section>
        </article>
        <div class="clear"></div>
      </div>
    </el-card> -->

      <el-card class="box-card" shadow="never">
        <!-- title -->
        <div class="clearfix title">
          <h2>课程详情</h2>
        </div>
        <el-row type="flex" class="row-bg" justify="center" style="min-height:700px"></el-row>
      </el-card>

  </div>
</template>
<script>
import { getCourseWeb } from '../../api/course'
export default {
  data() {
    return {
      data: {},
      page: 1,
      limit: 4,
      total: 10,
      courseId: '',
      course: {},
      chapterList: [],
      isbuyCourse: false
    }
  },
  created() {
    if (this.$route.params && this.$route.params.id) {
      this.courseId = this.$route.params.id
    }
    this.initCourseInfo()
  },

  methods: {
    startplayer(id) {
      console.log(id)
      this.$router.push({ path: '/display/student_startExp' })
    },
    initCourseInfo() {
      console.log(this.courseId)
      getCourseWeb(this.courseId).then((response) => {
        console.log(response.data.course)
        this.course = response.data.course
        this.chapterList = response.data.chapterVoList
      })
    }
  }
}
</script>
<style lang="scss" scoped>
.box-card {
  background-image: linear-gradient(to top, lightgrey 0%, lightgrey 1%, #e0e0e0 26%, #efefef 48%, #d9d9d9 75%, #bcbcbc 100%);

  border: 0;
  height: 100vh;
  width: 100%;
}
.title {
  background-image: linear-gradient(to right, #74ebd5 0%, #9face6 100%);
  border: 1px solid #bcbcbc;
  width: 50%;
  margin: 0 auto;
  border-radius: 15px;
  margin-bottom: 20px;
}
.clearfix > h2 {
  text-align: center;
}
</style>