<template>
    <body>

    <h1>Online Compiler</h1>

    <textarea required id="sourcecode" v-model="code"
    placeholder="type in your code here">
    </textarea>

    <button type="button" id="btn" @click="sourceCode">Run</button>
    <h4>Output:</h4>

    <textarea readonly v-model="output"
    name="output"></textarea>
    </body>
</template>

<script>
import postservice from '../services/PostServices.js'

export default {
  data(){
    return{
      code: '',
      output: '',
    }
  },
  methods:{
    sourceCode() {
        //postservice.create(this.code)
        this.output = this.code
    }
}
}
</script>

<style>
    body{
        display: flex;
        flex-direction: column;
        align-items: center;
    }
    textarea{
        height: 40vh;
        width: 60%;
    }
    button{
        padding: 5px;
    }
</style>