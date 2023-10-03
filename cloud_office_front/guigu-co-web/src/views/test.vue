<template>
  <div>
    <div>账号切换</div>
    <button @click="wjl()" type="default" size="mini">王经理</button>
    <button @click="lrsjl()" type="default" size="mini">李人事经理</button>
    <button @click="zzjl()" type="default" size="mini">张总经理</button>
    <button @click="admin()" type="default" size="mini">admin</button>
    <div>当前token：{{ token }}</div>
  </div>
</template>

<script>
export default {
  name: "Test",

  data() {
    return {
      token: ''
    };
  },

  created(){
    this.token = window.localStorage.getItem('token')
  },

  methods: {
    wjl() {
      window.localStorage.setItem('token', '');
      let token = 'eyJhbGciOiJIUzUxMiIsInppcCI6IkdaSVAifQ.H4sIAAAAAAAAAKtWKi5NUrJScgwN8dANDXYNUtJRSq0oULIyNLM0tTCyMLU00FEqLU4t8kxRsjKCMPMSc1OBWsqzcpRqAUyZwuJAAAAA.MTLWqLCLN9pP75zkcvbuc9Tr-20O-heHrS55rKU2FjSa7pzrh-74rK4Eu8pvXXm-oDROGB1l_OxQ8f9YZn0WlQ'
      window.localStorage.setItem('token', token);
      this.token = window.localStorage.getItem('token')
    },

    lrsjl() {
      window.localStorage.setItem('token', '');
      let token = 'eyJhbGciOiJIUzUxMiIsInppcCI6IkdaSVAifQ.H4sIAAAAAAAAAKtWKi5NUrJScgwN8dANDXYNUtJRSq0oULIyNDM3NjMwNDY10VEqLU4t8kxRsjKGMPMSc1OBWnKKirNylGoBjn551kIAAAA.HiQAK2uUQCFlQilO3mR3p5TURrlMrjN7zrR1IcFqhU8Q-VyxUiRJrGIuSwSKOC1uyQH_4JXL99yGDq74OnKvXQ'
      window.localStorage.setItem('token', token);
      this.token = window.localStorage.getItem('token')
    },

    zzjl() {
      window.localStorage.setItem('token', '');
      let token = 'eyJhbGciOiJIUzUxMiIsInppcCI6IkdaSVAifQ.H4sIAAAAAAAAAKtWKi5NUrJScgwN8dANDXYNUtJRSq0oULIyNDM3NjMwNLYw11EqLU4t8kxRsjKFMPMSc1OBWqqqsnKUagGHebUNQQAAAA.FcCkD6QfaP8bd_3MYsRD6Tp9wr9JSRFWIUuIdG5O7XxhIeyWfzGLp40_Lesx_kWMMaaskh_ba3NrTlj8-nP6OA'
      window.localStorage.setItem('token', token);
      this.token = window.localStorage.getItem('token')
    },

    admin() {
      window.localStorage.setItem('token', '');
      let token = 'eyJhbGciOiJIUzUxMiIsInppcCI6IkdaSVAifQ.H4sIAAAAAAAAAKtWKi5NUrJScgwN8dANDXYNUtJRSq0oULIyNLM0tTAyMjI311EqLU4t8kwBikGYeYm5qUAtiSm5mXlKtQCH_xckQgAAAA.l_5H-kpSxISe6YZuxgoV-HTmQgRwiNqgMRu64FsD8GxWRFt_x8q_ZylMs6l-hZJusGCV5vOBCAIfkTh7WSFdFA'
      window.localStorage.setItem('token', token);
      this.token = window.localStorage.getItem('token')
    }
  }
}
</script>
