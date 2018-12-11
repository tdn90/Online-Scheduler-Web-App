Vue.component('schedule-recents', {
    data: function() {
        return {
            duration: 1,
            results: {},
            hasResults: false
        }
    },
    methods: {
        get: function() {
            var self = this
            STARTLOAD()
            $.ajax({url: "https://97xvmjynw9.execute-api.us-east-1.amazonaws.com/Alpha/sysadmin/getschedulehour?hours=" + self.duration, 
                type: 'GET',
                success: function(result){
                    window.STOPLOAD()
                    if (result.httpCode == 200) {
                        self.results = (result.schedule_lst !== undefined)? result.schedule_lst : {}
                        if (result.schedule_lst !== undefined) {
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
            });
        },
        clearResults: function() {
            this.results = {}
            this.hasResults = false
        }
    },
    template: `
    <div class="card d-block">
        <div class="card-body">
            <h5 class="card-title d-flex align-content-between"><i class="material-icons mr-1">search</i>Recent Schedules</h5>
            <h6 class="card-subtitle mb-2 text-muted">Browse recently created meeting schedules</h6>
            <hr />
            <div>
                <div class="form-inline">
                    Show meetings no older than: &nbsp;&nbsp;
                    <input type="number" min="0" class="form-control" v-model="duration" style="width:70px">
                    &nbsp; hours. &nbsp;
                    <button class="btn btn-outline-primary d-flex" v-on:click="get"><i class="material-icons mr-1">search</i>Search</button>
                </div>
            </div><br />
            <h5 v-if="hasResults">{{Object.keys(results).length}} Results<br/><small clas="text-mutes">Click on a meeting schedule to visit its participant page</small><br/><br/></h5>
            
            <h5 v-else>No results</h5>
            <div class="list-group">
                <a v-for="result in results" class="list-group-item list-group-item-action flex-column align-items-start" v-bind:href="'https://s3.amazonaws.com/wpi.cs3733.heze.2/participant.html?id=' + result.scheduleID">
                    <div class="d-flex w-100 justify-content-between">
                        <h5 class="mb-1 d-inline-block"">{{ result.name }}</h5>
                    </div>
                </a>
            </div>
        </div>
    </div>
    `
})


Vue.component('schedules-old', {
    data: function() {
        return {
            duration: 0,
            results: {},
            hasResults: false
        }
    },
    methods: {
        convertToSlotNameString: function (slot) {
            return moment.utc(slot * 1000).format("h:mm a") + " to " + moment.utc((slot + this.duration * 60) * 1000).format("h:mm a")
        },
        review: function() {
            var self = this
            STARTLOAD()
            $.ajax({url: "https://97xvmjynw9.execute-api.us-east-1.amazonaws.com/Alpha/sysadmin/getscheduleday?days=" + self.duration, 
                type: 'GET',
                success: function(result){
                    window.STOPLOAD()
                    if (result.httpCode == 200) {
                        self.results = (result.schedule_lst !== undefined)? result.schedule_lst : {}
                        if (result.schedule_lst !== undefined) {
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
            });
        },
        deleteFunc: function() {
            var self = this
            STARTLOAD()
            $.ajax({url: "https://97xvmjynw9.execute-api.us-east-1.amazonaws.com/Alpha/sysadmin/deleteschedule", 
                type: 'DELETE',
                success: function(result){
                    window.STOPLOAD()
                    if (result.httpCode == 200) {
                        self.results = (result.schedule_lst !== undefined)? result.schedule_lst : {}
                        if (result.schedule_lst !== undefined) {
                            self.hasResults = true;
                        }
                        self.$emit("reload")
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
                    days: self.duration
                })
            });
        },
        tsString: function(tsobj) {
            return moment(tsobj.date.date.year + "-" + tsobj.date.date.month + "-" + tsobj.date.date.day + " " + tsobj.date.time.hour + ":" + tsobj.date.time.minute, "YYYY-MM-DD H:mm").format("dddd MM/DD/YYYY h:mm a")
        },
        clearResults: function() {
            this.results = {}
            this.hasResults = false
        }
    },
    template: `
    <div class="card d-block">
        <div class="card-body">
            <h5 class="card-title d-flex align-content-between"><i class="material-icons mr-1">search</i>Old Schedules</h5>
            <h6 class="card-subtitle mb-2 text-muted">Review &amp; delete old meeting schedules</h6>
            <hr />
            <div>
                <div class="form-inline">
                    Review meetings older than: &nbsp;&nbsp;
                    <input type="number" min="0" class="form-control" v-model="duration" style="width:70px">
                    &nbsp; days. &nbsp;
                    <button class="btn btn-outline-primary d-flex" v-on:click="review"><i class="material-icons mr-1">search</i>Search</button>
                </div>
            </div><br />
            <h5 v-if="hasResults">{{Object.keys(results).length}} Results<br/><small clas="text-mutes">Click on a meeting schedule to visit its participant page</small><br/><br/></h5>
            <h5 v-else>No results</h5>
            <div class="list-group">
                <a v-for="result in results" class="list-group-item list-group-item-action flex-column align-items-start" v-bind:href="'https://s3.amazonaws.com/wpi.cs3733.heze.2/participant.html?id=' + result.scheduleID">
                    <div class="d-flex w-100 justify-content-between">
                        <h5 class="mb-1 d-inline-block"">{{ result.name }}</h5>
                    </div>
                </a>
            </div><br />
            <button v-if="hasResults" class="btn btn-outline-danger btn-block justify-content-center align-content-between d-flex" v-on:click="deleteFunc">
                <i class="material-icons mr-1">delete_forever</i>
                <span>Delete These Schedules Forever</span>
            </button>
        </div>
    </div>
    `
})
$(document).ready(function() {
    var orgpage = new Vue({
        el: "#orgpage",
        methods: {
            reload: function(which) {
                if (which == "old") {
                    this.$refs.old.clearResults()
                } else if (which == "recent") {
                    this.$refs.recent.clearResults()
                } else {
                    this.$refs.old.clearResults()
                    this.$refs.recent.clearResults()
                }
            }
        }
    })
})