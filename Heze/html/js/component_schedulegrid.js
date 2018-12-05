Vue.component('meeting-schedule-grid', {
    props: ['mode', 'value'],
    data: function () {
        return {
            page: 1,
            /* Modal stuffs */
            showValidationAlert: false,
            showMeetingBookedAlert: false,
            selectedDay: null,
            selectedTime: null,
            selectedTslot: null,
            duration: null,
            enteredName: "",
            lastMeetingSecretKey: "",
            enteredSecretKeyForCancellation: "",
            cancelNotAllowed: false,
            showSecretKeyAlert: false,
        }
    },
    methods: {
        convertToDayString: function (date) {
            return moment(date.date.year + "-" + date.date.month + "-" + date.date.day, "YYYY-MM-DD").format("dddd MM/DD/YYYY")
        },
        convertToSlotNameString: function (slot) {
            return moment.utc(slot * 1000).format("h:mm a") + " to " + moment.utc((slot + this.value.meetingDuration * 60) * 1000).format("h:mm a")
        },
        getAllTimeslots: function () {
            var nums = Math.floor((this.value.endTime - this.value.startTime) * 60 / this.value.meetingDuration)
            var slots = []
            var last = this.value.startTime * 60 * 60
            console.log()
            for (var i = 0; i < nums; i++) {
                slots.push([i, last]);
                last += this.value.meetingDuration * 60
            }
            return slots
        },
        next: function () {
            console.log("next")
            if (this.value.days.length >= this.page * 5) {
                this.page++
            }
        },
        prev: function () {
            if (this.page >= 1)
                this.page--
        },
        registerOpenFunc: function (tsid, dateobj, timeslot) {
            this.selectedTslot = tsid
            var date = moment(dateobj.date.year + "-" + dateobj.date.month + "-" + dateobj.date.day, "YYYY-MM-DD")
            date = date.add(timeslot.startTime.hour, "hours")
            date = date.add(timeslot.startTime.minute, "minutes")
            this.selectedTime = date.format("h:mm a")
            this.selectedDay = date.format("dddd MM/DD/YYYY")
            this.duration = timeslot.meetingDuration
        },
        openRegister: function (tsobj) {
            this.selectedDay = "UNKNOWN"
            this.selectedTime = moment(tsobj.startTime.hour + ":" + tsobj.startTime.minute, "H:mm").format("h:mm a")
            this.duration = tsobj.meetingDuration
            this.selectedTslot = tsobj.timeSlotID
            $('#registerModal').modal('show')
        },
        registerFunc: function () {
            console.log("submit timeslot for " + this.selectedTslot + ", with name " + this.enteredName)

            var get_url = "https://97xvmjynw9.execute-api.us-east-1.amazonaws.com/Alpha/participant/registermeeting";
            var self = this
            $.ajax({url: get_url, 
                type: 'POST',
                success: function(result){
                    if (result.httpCode == 200) {
                        self.showValidationAlert = false
                        self.showMeetingBookedAlert = false
                        self.selectedTslot.meeting = result.m;
                        self.lastMeetingSecretKey = result.m.secretKey;
                        self.$emit('reload-evt')
                        
                        $('#registerModal').modal('hide')
                        $('#keyModal').modal('show')
                    } else if (result.httpCode == 403) {
                        console.log("backend http code not 200")
                        self.showMeetingBookedAlert = true
                    } else {
                        console.log("backend http code not 200")
                        self.showValidationAlert = true
                    }
                },
                error: function(resp) {
                    console.log("ERROR, ", resp)
                    self.showValidationAlert = true;
                },
                dataType: 'json',
                data: JSON.stringify({
                    id: self.selectedTslot,
                    name: self.enteredName
                })
            });
        },
        cancelFromOrg: function(a) {
            this.enteredSecretKeyForCancellation = a
            this.cancelFunc()
            self.$emit('reload-evt')
        },
        cancelFunc: function () {
            
            var get_url = "https://97xvmjynw9.execute-api.us-east-1.amazonaws.com/Alpha/participant/cancelmeeting";
            var self = this
            $.ajax({url: get_url, 
                type: 'POST',
                success: function(result){
                    if (result.httpCode == 200) {
                        self.cancelNotAllowed = false
                        self.$emit('reload-evt')
                        
                        $('#cancelModal').modal('hide')
                    } else if (result.httpCode == 403) {
                        console.log("backend http code 403 not auth")
                        self.cancelNotAllowed = true
                    } else {
                        console.log("backend http code not 200")
                        self.cancelNotAllowed = true
                    }
                },
                error: function(resp) {
                    console.log("ERROR, ", resp)
                    self.cancelNotAllowed = true;
                },
                dataType: 'json',
                data: JSON.stringify({
                    secretKey: self.enteredSecretKeyForCancellation
                })
            });
        },
        openCancel: function() {
            $('#cancelModal').modal('show')
        },
        deleteFunc: function() {
            this.$emit('delete-me')
            console.log("big delete")
        },
        toggle: function(tsid) {
            var self = this
            $.ajax({url: "https://97xvmjynw9.execute-api.us-east-1.amazonaws.com/Alpha/organizer/toggletimeslotavailability", 
                type: 'POST',
                success: function(result){
                    if (result.httpCode == 200) {
                        self.$emit('reload-evt')
                    } else {
                        alert('Error. Could not update avilability.')
                    }
                },
                error: function(resp) {
                    alert('Error. Could not update avilability.')
                },
                dataType: 'json',
                data: JSON.stringify({
                    timeSlotID: tsid
                })
            });
        }
    },
    template: `
        <div>
            <h2>Viewing Meeting Schedule: {{value.name}}</h2><br />
            <div class="btn-toolbar" role="toolbar">
                <div class="btn-group" role="group">
                    <button class="btn btn-secondary btn-sm justify-content-center align-content-between d-flex" :disabled="page <= 1" v-on:click="prev()">
                        <i class="material-icons mr-1">arrow_back</i>
                        <span>Previous</span>
                    </button>
                    <button class="btn btn-secondary btn-sm justify-content-center align-content-between d-flex" :disabled="value.days.length <= page*5" v-on:click="next()">
                        <span>Next</span>
                        <i class="material-icons mr-1">arrow_forward</i>
                    </button>
                </div>&nbsp; &nbsp;
                <button v-if="mode=='organizer'" class="btn btn-danger justify-content-center align-content-between d-flex" data-toggle="modal" data-target="#deleteScheduleModal">
                    <i class="material-icons mr-1">delete_forever</i>
                    <span>Delete</span>
                </button>
            </div>
            <br /><br />
            <table class="table">
                <thead>
                    <th>Timeslot</th>
                    <th v-for="day in value.days.slice((page-1)*5,page*5)">
                        {{convertToDayString(day.date)}}<br />
                        <div style="display:block" v-if="mode == 'organizer'">
                            <button class="btn btn-success btn-sm justify-content-center align-content-between d-flex">
                                <i class="material-icons mr-1">add</i>
                                <span>Available</span>
                            </button>
                            <button class="btn btn-danger btn-sm justify-content-center align-content-between d-flex">
                                <i class="material-icons mr-1">close</i>
                                <span>Unavailable</span>
                            </button>
                        </div>
                    </th>
                </thead>
                <tbody>
                    <tr v-for="slot in getAllTimeslots()">
                        <td>
                            {{convertToSlotNameString(slot[1])}}
                            <div style="display:block" v-if="mode == 'organizer'">
                            <button class="btn btn-success btn-sm justify-content-center align-content-between d-flex">
                                <i class="material-icons mr-1">add</i>
                                <span>Available</span>
                            </button>
                            <button class="btn btn-danger btn-sm justify-content-center align-content-between d-flex">
                                <i class="material-icons mr-1">close</i>
                                <span>Unavailable</span>
                            </button>
                        </div>
                        </td>
                        <td v-for="date in value.days.slice((page-1)*5,page*5)">
                            <div v-if="slot[0] < date.slots.length && date.slots[slot[0]].meeting == null && date.slots[slot[0]].organizerAvailable">
                                <button v-if="mode == 'participant'" class="btn btn-primary justify-content-center align-content-between d-flex"
                                data-toggle="modal" data-target="#registerModal" v-on:click="registerOpenFunc(date.slots[slot[0]].timeslotID, date.date, date.slots[slot[0]])">
                                    <i class="material-icons mr-1">add</i>
                                    <span>Register</span>
                                </button>
                                <button v-else class="btn btn-danger btn-sm justify-content-center align-content-between d-flex" v-on:click="toggle(date.slots[slot[0]].timeslotID)">
                                    <i class="material-icons mr-1">close</i>
                                    <span>Mark as Unavailable</span>
                                </button>
                            </button>
                            </div>
                            <div v-else-if="slot[0] < date.slots.length && date.slots[slot[0]].organizerAvailable">
                                {{date.slots[slot[0]].meeting.participant}} 
                                <button v-if="mode=='participant'" type="button" class="btn btn-sm btn-default btn-circle" style="float:right" data-toggle="modal" data-target="#cancelModal">
                                    <i class="material-icons" style="font-size:18px">close</i>
                                </button>
                                <button v-else type="button" class="btn btn-sm btn-default btn-circle" style="float:right" v-on:click="cancelFromOrg(date.slots[slot[0]].meeting.secretKey)">
                                    <i class="material-icons" style="font-size:18px">close</i>
                                </button>
                            </div>
                            <button v-else-if="mode=='organizer'" class="btn btn-success btn-sm justify-content-center align-content-between d-flex" v-on:click="toggle(date.slots[slot[0]].timeslotID)">
                                <i class="material-icons mr-1">add</i>
                                <span>Mark as Available</span>
                            </button>
                            <div v-else>
                                
                            </div>
                        </td>
                    </tr>
                </tbody>
            </table>

            <!-- Register for meeting modal -->
            <div class="modal fade" tabindex="-1" role="dialog" aria-labelledby="registerModal" id="registerModal" aria-hidden="true">
                <div class="modal-dialog" role="document">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title">Sign up for a meeting</h5>
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                            <span aria-hidden="true">&times;</span>
                        </button>
                    </div>
                    <div class="modal-body">
                        <strong>You've selected the following time slot:</strong>
                        <ul>
                            <li>Day: {{selectedDay}}</li>
                            <li>Starting Time: {{selectedTime}}</li>
                            <li>Duration: {{duration}} minutes</li>
                        </ul>
                        <p>How should we identify you on the schedule?</p>
                    <div class="alert alert-danger" v-if="showValidationAlert">
                        <strong>Whoops! </strong>Please enter a name (1 - 30 characters).
                    </div>
                    <div class="alert alert-danger" v-if="showMeetingBookedAlert">
                        <strong>Darn! </strong>Someone else already booked that time slot!
                    </div>
                    <form id="findMeetingSchedule">
                        <div class="form-group">
                            <label for="name">Your name (or email)</label>
                            <input type="text" class="form-control" id="name" aria-describedby="name"
                                placeholder="Jane Doe" v-model="enteredName">
                        </div>
                    </form>
                    </div>
                    <div class="modal-footer">
                        <button class="btn btn-primary justify-content-center align-content-between d-flex" v-on:click="registerFunc">
                            <i class="material-icons mr-1">send</i>
                            <span>Submit</span>
                        </button>
                        <button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>
                    </div>
                </div>
                </div>
            </div>

            <!-- successful meeting creation modal -->
            <div class="modal fade" tabindex="-1" role="dialog" aria-labelledby="keyModal" id="keyModal" aria-hidden="true">
                <div class="modal-dialog" role="document">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title">Success!</h5>
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                            <span aria-hidden="true">&times;</span>
                        </button>
                    </div>
                    <div class="modal-body">
                        <div class="alert alert-success">
                            <strong>Congrats! </strong>You have registered for a meeting.
                        </div>
                        <p><strong>Hey! Write down this key!</strong></p>
                        <p>If for some reason you change your mind later and choose to cancel this meeting, you will need this secret key</p>
                        <kbd>{{lastMeetingSecretKey}}</kbd>
                    </div>
                    <div class="modal-footer">
                        <button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>
                    </div>
                </div>
                </div>
            </div>

            <!-- Cancel meeting modal -->
            <div class="modal fade" tabindex="-1" role="dialog" aria-labelledby="cancelModal" id="cancelModal" aria-hidden="true">
                <div class="modal-dialog" role="document">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title">Cancel a meeting</h5>
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                            <span aria-hidden="true">&times;</span>
                        </button>
                    </div>
                    <div class="modal-body">
                        <p>To cancel a meeting, please use the secret key that was provided to you when you created the meeting.</p>
                    <div class="alert alert-danger" v-if="cancelNotAllowed">
                        <strong>Access Denied </strong>That isn't the right secret key.
                    </div>
                    <form id="findMeetingSchedule">
                        <div class="form-group">
                            <label for="name">Secret Key</label>
                            <input type="text" class="form-control" id="name" aria-describedby="name"
                                placeholder="AABBCCDD" v-model="enteredSecretKeyForCancellation">
                        </div>
                    </form>
                    </div>
                    <div class="modal-footer">
                        <button class="btn btn-danger justify-content-center align-content-between d-flex" v-on:click="cancelFunc">
                            <i class="material-icons mr-1">warning</i>
                            <span>Cancel Meeting</span>
                        </button>
                        <button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>
                    </div>
                </div>
                </div>
            </div>

            <!-- Delete schedule modal (organizer) -->
            <div class="modal fade" tabindex="-1" role="dialog" aria-labelledby="deleteScheduleModal" id="deleteScheduleModal" aria-hidden="true">
                <div class="modal-dialog" role="document">
                <div class="modal-content">
                    <div class="modal-header">
                        <h5 class="modal-title">Are you sure about that?</h5>
                        <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                            <span aria-hidden="true">&times;</span>
                        </button>
                    </div>
                    <div class="modal-body">
                        <p>If you would like to <strong>permanently</strong> delete this schedule, click delete.</p>
                    </div>
                    <div class="modal-footer">
                        <button class="btn btn-danger justify-content-center align-content-between d-flex" v-on:click="deleteFunc">
                            <i class="material-icons mr-1">delete_forever</i>
                            <span>Delete</span>
                        </button>
                        <button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>
                    </div>
                </div>
                </div>
            </div>

        </div>
    `
})