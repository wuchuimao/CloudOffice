<template>
  <div class="container">
    <van-nav-bar
      title="关于我们"
    />
    <div class="detail-wrap">
      <div class="item">
        吴垂茂的云上办公系统，南昌大学，信息工程学院，电子信息（控制工程专业）
        方向：车辆检测与跟踪
        工作方向：java后端开发工程师
        兴趣：篮球
        手机：15170606963
        邮箱：2438232742@qq.com
        籍贯：江西省赣州市崇义县
      </div>
    </div>

  </div>
</template>

<script>
export default {
  name: "process",

  data() {
    return {
    };
  },

  created(){
  },

  methods: {

  }
}
</script>

<style lang="scss" scoped>
.container {
  padding: 20px;
  .detail-wrap {
    .item {
      color: #838485;
    }
  }
}
</style>
