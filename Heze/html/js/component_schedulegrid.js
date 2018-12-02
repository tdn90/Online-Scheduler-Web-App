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
            enteredName: ""
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
            console.log(date)
            this.selectedTime = date.format("h:mm a")
            this.selectedDay = date.format("dddd MM/DD/YYYY")
            this.duration = timeslot.meetingDuration
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
                        self.$emit('reload-evt')
                        
                        $('#registerModal').modal('hide')
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
                    self.showAlert = true;
                },
                dataType: 'json',
                data: JSON.stringify({
                    id: self.selectedTslot,
                    name: self.enteredName
                })
            });
        }
    },
    template: `
        <div>
            <h2>Viewing Meeting Schedule: {{value.name}}</h2><br />
            <div class="btn-group" role="group">
                <button class="btn btn-secondary btn-sm justify-content-center align-content-between d-flex" :disabled="page <= 1" v-on:click="prev()">
                    <i class="material-icons mr-1">arrow_back</i>
                    <span>Previous</span>
                </button>
                <button class="btn btn-secondary btn-sm justify-content-center align-content-between d-flex" :disabled="value.days.length <= page*5" v-on:click="next()">
                    <span>Next</span>
                    <i class="material-icons mr-1">arrow_forward</i>
                </button>
            </div><br /><br />
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
                                    <span>Submit</span>
                                </button>
                                <p v-else>[Free]</p>
                            </div>
                            <div v-else-if="slot[0] < date.slots.length && date.slots[slot[0]].organizerAvailable">
                                {{date.slots[slot[0]].meeting.participant}} 
                                <button type="button" class="btn btn-sm btn-default btn-circle" style="float:right">
                                    <i class="material-icons" style="font-size:18px">close</i>
                                </button>
                            </div>
                            <div v-else>
                                [Unavailable]
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
                        <strong>Whoops! </strong>Please enter a name (1-30 characters).
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
                            <span>Register</span>
                        </button>
                        <button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>
                    </div>
                </div>
                </div>
            </div>
        </div>
    `
})