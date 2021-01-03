<template>
    <el-card class="box-card">
      <div class="clearfix title">
        <h2>课堂通知</h2>
      </div>
      <!-- <el-row type="flex" justify="end">
                <el-button type="primary" @click="reset">清空</el-button>
            </el-row> -->
      <el-row
        type="flex"
        class="row-bg"
        justify="center"
        style="min-height: 500px;"
      >
        <el-table
          ref="filterTable"
          :data="release"
          :fit="true"
          style="width: 100%;border-radius: 15px"
        >
          <el-table-column prop="title" label="通知标题"> </el-table-column>
          <el-table-column
            prop="content"
            label="通知内容"
            sortable
            column-key="date"
          >
          </el-table-column>
          <el-table-column
            prop="type"
            label="通知类型"
            :filters="[
              { text: '系统通知', value: '系统通知' },
              { text: '课堂通知', value: '课堂通知' },
            ]"
            :filter-method="filterTag"
            filter-placement="bottom-end"
          >
            <template slot-scope="scope">
              <el-tag
                :type="scope.row.tag == '系统通知' ? 'primary' : 'success'"
                disable-transitions
                >{{ scope.row.type }}
              </el-tag>
            </template>
          </el-table-column>
        </el-table>
      </el-row>
    </el-card>
</template>

<script>
import { mapGetters } from 'vuex'
export default {
  data() {
    return {
      tableData: [
        {
          title: '教师上线了！,ip:127.0.0.1,identity:teacher',
          date: '2019-10-04 12:52:00',
          tag: '系统通知'
        },
        {
          title: '教师上线了！,ip:127.0.0.1,identity:teacher',
          date: '2019-10-04 12:52:00',
          tag: '系统通知'
        },
        {
          title: '教师发布了新的作业，点击查看详情！',
          date: '2019-10-04 12:52:00',
          tag: '课堂通知'
        }
      ]
    }
  },
  methods: {
    filterTag(value, row) {
      return row.tag === value
    },
    reset() {
      this.$store.dispatch('reset_release')
    }
  },
  computed: {
    ...mapGetters(['release'])
  }
}
</script>

<style rel="stylesheet/scss" lang="scss" scoped>
.clearfix > h2 {
  text-align: center;
}

.box-card {
  background-image: linear-gradient(
    to top,
    lightgrey 0%,
    lightgrey 1%,
    #e0e0e0 26%,
    #efefef 48%,
    #d9d9d9 75%,
    #bcbcbc 100%
  );

  border: 0;
  height: 1030px;
  width: 100%;
  height: 100vh;
}
.title {
  background-image: linear-gradient(to right, #74ebd5 0%, #9face6 100%);
  border: 1px solid #bcbcbc;
  width: 50%;
  margin: 0 auto;
  border-radius: 15px;
  margin-bottom: 20px;
}
</style>
