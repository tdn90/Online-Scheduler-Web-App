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

Vue.component('meeting-search', {
    props: ['start', 'end', 'duration', 'schedid'],
    data: function() {
        return {
            year: "",
            month: -9999,
            day: -9999,
            day_of_week: -9999,
            day_of_month: -9999,
            startTime: -9999,
            results: {},
            hasResults: false
        }
    },
    methods: {
        getAllTimeslots: function () {
            var nums = Math.floor((this.end - this.start) * 60 / this.duration)
            var slots = []
            var last = this.start * 60 * 60
            for (var i = 0; i < nums; i++) {
                slots.push([i, last]);
                last += this.duration * 60
            }
            return slots
        },
        convertToSlotNameString: function (slot) {
            return moment.utc(slot * 1000).format("h:mm a") + " to " + moment.utc((slot + this.duration * 60) * 1000).format("h:mm a")
        },
        submit: function() {
            var self = this
            STARTLOAD()
            $.ajax({url: "https://97xvmjynw9.execute-api.us-east-1.amazonaws.com/Alpha/participant/searchopenslots", 
                type: 'POST',
                success: function(result){
                    window.STOPLOAD()
                    if (result.httpCode == 200) {
                        self.results = (result.timeslot_lst !== undefined)? result.timeslot_lst : {}
                        if (result.timeslot_lst !== undefined) {
                            self.hasResults = true;
                        }
                    } else {
                        alert('Error. Search failed.')
                    }
                },
                error: function(resp) {
                    window.STOPLOAD()
                    alert('Error. Could not search.')
                },
                dataType: 'json',
                data: JSON.stringify({
                    scheduleID: self.schedid,
                    month: self.month,
                    year: (self.year == "")? -9999 : parseInt(self.year),
                    day_of_week: self.day_of_week,
                    day_of_month: self.day_of_month,
                    startTime: self.startTime
                })
            });
        },
        tsString: function(tsobj) {
            return moment(tsobj.date.date.year + "-" + tsobj.date.date.month + "-" + tsobj.date.date.day + " " + tsobj.date.time.hour + ":" + tsobj.date.time.minute, "YYYY-MM-DD H:mm").format("dddd MM/DD/YYYY h:mm a")
        },
        register: function(ts) {
            this.$emit('register', ts)
        },
        cancel: function(ts) {
            this.$emit('cancel', ts)
        },
        clearResults: function() {
            this.results = {}
            this.hasResults = false
        }
    },
    template: `
    <div class="card d-block">
        <div class="card-body">
            <h5 class="card-title d-flex align-content-between"><i class="material-icons mr-1">search</i>Filter Results</h5>
            <h6 class="card-subtitle mb-2 text-muted">Search for available timeslots that work for you</h6>
            <hr />
            <div>
                <form class="form-inline" style="display:inline-block; float:left">
                    <!--<div class="input-group">
                        <div class="input-group-prepend">
                            <div class="input-group-text"><input type="checkbox"/>&nbsp;&nbsp;Year: </div>
                        </div>
                        
                    </div>--><input type="number" class="form-control" min="0" style="width:10rem" placeholder="Any Year" v-model="year"/>&nbsp;&nbsp;
                    <select class="form-control" v-model="month">
                        <option value="-9999">Any Month</option>
                        <option value="1">January</option>
                        <option value="2">February</option>
                        <option value="3">March</option>
                        <option value="4">April</option>
                        <option value="5">May</option>
                        <option value="6">June</option>
                        <option value="7">July</option>
                        <option value="8">August</option>
                        <option value="9">September</option>
                        <option value="10">October</option>
                        <option value="11">November</option>
                        <option value="12">December</option>
                    </select>&nbsp;&nbsp;
                    <select class="form-control" v-model="day_of_month">
                        <option value="-9999">Any Day</option>
                        <option v-for="i in 31" v-bind:value="i">{{i}}</option>
                    </select>&nbsp;&nbsp;
                    <select class="form-control" v-model="day_of_week">
                        <option value="-9999">Any Day of the Week</option>
                        <option value="1">Sunday</option>
                        <option value="2">Monday</option>
                        <option value="3">Tuesday</option>
                        <option value="4">Wednesday</option>
                        <option value="5">Thrusday</option>
                        <option value="6">Friday</option>
                        <option value="7">Saturday</option>
                    </select>&nbsp;&nbsp;
                    <select class="form-control" v-model="startTime">
                        <option value="-9999">Any Time Slot</option>
                        <option v-for="slot in getAllTimeslots()" v-bind:value="slot[1] * 1000">{{convertToSlotNameString(slot[1])}}</option>
                    </select>&nbsp;&nbsp;
                </form>
                <div style="display:inline-block">
                <button class="btn btn-outline-primary d-flex" v-on:click="submit"><i class="material-icons mr-1">search</i>Search</button>
                </div>
            </div><br />
            <h5 v-if="hasResults">{{Object.keys(results).length}} Results:</h5>
            <h5 v-else>No results</h5>
            <div class="list-group">
                <div v-for="result in results" class="list-group-item list-group-item-action flex-column align-items-start">
                    <div class="d-flex w-100 justify-content-between">
                        <h5 class="mb-1 d-inline-block"">{{ tsString(result) }}</h5>
                        <div style="float:right; display: inline-block">
                            <button v-if="result.organizerAvailable && result.meeting == undefined" class="btn btn-outline-success d-flex" v-on:click="register(result)"><i class="material-icons mr-1">add</i>Register</button>
                            <small v-if="!result.organizerAvailable" >Unavailable</small>
                            <div v-if="result.organizerAvailable && result.meeting != undefined" class="justify-content-center align-content-between">
                                <p style="display: inline-block; float:left; padding-top:7px;">{{ result.meeting.participant }}</p>&nbsp;&nbsp;
                                <button class="btn btn-outline-danger d-flex" style="float:right;" v-on:click="cancel(result)"><i class="material-icons mr-1">cancel</i>Cancel</button>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <!--<a href="#" class="card-link">Card link</a>
            <a href="#" class="card-link">Another link</a>-->
        </div>
    </div>
    `
})

$(document).ready(function(){
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
                    this.reload()
                }
            },
            reload: function() {
                var get_url = "https://97xvmjynw9.execute-api.us-east-1.amazonaws.com/Alpha/participant/getschedule?id=" + this.key;
                var self = this
                STARTLOAD()
                $.ajax({url: get_url, 
                    type: 'GET',
                    success: function(result){
                        window.STOPLOAD()
                        if (result.httpcode == 200) {
                            self.showAlert = false;
                            grid_holder.grid_data = result.data;
                            grid_holder.has_data = true;
                            grid_holder.key = self.key;
                            $('#openModal').modal('hide')
                            
                            setURL(updateQueryStringParameter(window.location.href, "id", self.key))
                        } else {
                            console.log("backend http code not 200")
                            self.showAlert = true;
                            if (self.alertError) {
                                self.showAlert = false;
                                alert("Failed to load this schedule. Please check that you got the correct link from the organizer.")
                            }
                            setURL(updateQueryStringParameter(window.location.href, "id", ""))
                        }
                    },
                    error: function(resp) {
                        window.STOPLOAD()
                        console.log("ERROR, ", resp)
                        self.showAlert = true;
                        if (self.alertError) {
                            self.showAlert = false;
                            alert("Failed to load this schedule. Please check that you got the correct link from the organizer.")
                        }
                        setURL(updateQueryStringParameter(window.location.href, "id", ""))
                    },
                });
            }
        }
    });

    var grid_holder = new Vue({
        el: '#meeting-sechedule-holder-vue',
        data: {
            grid_data: {},
            key: null,
            has_data: false
        },
        methods: {
            reload: function() {
                this.$refs.search.clearResults()
                open_modal.reload()
            },
            cancel: function(tsid) {
                this.$refs.grid.openCancel()
            },
            register: function(tsobj) {
                this.$refs.grid.openRegister(tsobj)
            }
        }
    })

    var id_qparam = getQueryVariable("id")
    if (id_qparam != undefined) {
        open_modal.key = id_qparam
        open_modal.submit(true)
    }

})