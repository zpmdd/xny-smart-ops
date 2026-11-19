import hammer from "./hammer"
 
const install = function (Vue) {
    Vue.directive('hammer', hammer)
}
 
export default install