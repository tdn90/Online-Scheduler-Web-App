Vue.component('fullpage-load', {
    props: {
        timeout: {
            default: 0, //ms
            type: Number
        },
        loading: {
            default: false,
            type: Boolean
        }
    },
    data: function() {
        return {timeout_handler: null, is_show: false}
    },
    watch: {
        loading: function(val, old) {
            var self = this
            if (val) {
                this.timeout_handler = setTimeout(function(){ 
                    if (self.loading) {
                        self.is_show = true
                        self.timeout_handler = null
                    }
                }, this.timeout);
            } else if (this.timeout_handler != null) {
                clearTimeout(this.timeout_handler)
                this.timeout_handler = null
                this.is_show = false
            } else {
                this.is_show = false
            }
        }
    },
    template: `
        <div>
            <div class="cover" v-if="is_show">
                <div style="position: absolute; top:25%; text-align:center; width:100vw">
                    <h1 class="text-light">Loading</h1>
                    <h3 class="text-light">Please Wait</h3>
                </div>
                <div class="pong-loader"></div>
            </div>
        </div>
    `
})
$(document).ready(function(){
    window.fullpageloadcontroller = new Vue({
        el: "#loadingHolder",
        data: {
            isLoad: false,
            timeout: 1000,
        }
    })
})

window.STARTLOAD = function() {
    window.fullpageloadcontroller.isLoad = true;
}

window.STOPLOAD = function() {
    window.fullpageloadcontroller.isLoad = false;
}