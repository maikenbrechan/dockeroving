import axios from 'axios'


export default{

    create (code) {
        return axios.post('http://localhost:8080/compiler/code', { code: code })
  }
}