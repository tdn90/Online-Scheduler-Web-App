$(document).ready(function(){
    var grid_holder = new Vue({
        el: '#meeting-sechedule-holder-vue',
        data: {
            grid_data: {},
            has_data: false
        }
    })

    var open_modal = new Vue({
        el: '#openModal',
        data: {
            showAlert: false,
            key: ""
        },
        methods: {
            submit: function() {
                if (this.key == "") {
                    this.showAlert = true
                } else {
                    this.showAlert = false

                    var get_url = "https://97xvmjynw9.execute-api.us-east-1.amazonaws.com/Alpha/participant/getschedule?id=" + this.key;
                    var self = this
                    $.ajax({url: get_url, 
                        type: 'GET',
                        success: function(result){
                            if (result.httpcode == 200) {
                                self.showAlert = false;
                                grid_holder.grid_data = result.data;
                                grid_holder.has_data = true;
                                $('#openModal').modal('hide')
                            } else {
                                console.log("backend http code not 200")
                                self.showAlert = true;
                            }
                        },
                        error: function(resp) {
                            console.log("ERROR, ", resp)
                            self.showAlert = true;
                        },
                    });
                }
            }
        }
    });

})