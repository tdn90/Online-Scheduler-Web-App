function getQueryVariable(variable) {
    var query = window.location.search.substring(1);
    var vars = query.split('&');
    for (var i = 0; i < vars.length; i++) {
        var pair = vars[i].split('=');
        if (decodeURIComponent(pair[0]) == variable) {
            return decodeURIComponent(pair[1]);
        }
    }
    console.log('Query variable %s not found', variable);
}
function updateQueryStringParameter(uri, key, value) {
    var re = new RegExp("([?&])" + key + "=.*?(&|$)", "i");
    var separator = uri.indexOf('?') !== -1 ? "&" : "?";
    if (uri.match(re)) {
      return uri.replace(re, '$1' + key + "=" + value + '$2');
    }
    else {
      return uri + separator + key + "=" + value;
    }
  }
function setURL(url) {
    if (history.pushState) {
        console.log(url)
        history.pushState({}, null, url);
    }
}
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
            submit: function(alertError=false) {
                if (this.key == "") {
                    if (!alertError) {
                        this.showAlert = true
                    }
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
                                
                                setURL(updateQueryStringParameter(window.location.href, "id", self.key))
                            } else {
                                console.log("backend http code not 200")
                                self.showAlert = true;
                                if (alertError) {
                                    self.showAlert = false;
                                    alert("Failed to load this schedule. Please check that you got the correct link from the organizer.")
                                }
                                setURL(updateQueryStringParameter(window.location.href, "id", ""))
                            }
                        },
                        error: function(resp) {
                            console.log("ERROR, ", resp)
                            self.showAlert = true;
                            if (alertError) {
                                self.showAlert = false;
                                alert("Failed to load this schedule. Please check that you got the correct link from the organizer.")
                            }
                            setURL(updateQueryStringParameter(window.location.href, "id", ""))
                        },
                    });
                }
            }
        }
    });

    var id_qparam = getQueryVariable("id")
    if (id_qparam != undefined) {
        open_modal.key = id_qparam
        open_modal.submit(true)
    }

})