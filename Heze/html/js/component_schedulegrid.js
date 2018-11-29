Vue.component('meeting-schedule-grid', {
    props: ['mode', 'value'],
    data: function () {
        return {
            page: 1
        }
    },
    methods: {
        convertToDayString: function(date) {
            return moment(date.date.year + "-" + date.date.month + "-" + date.date.day, "YYYY-MM-DD").format("dddd MM/DD/YYYY")
        },
        convertToSlotNameString: function(slot) {
            return moment.utc(slot*1000).format("h:mm a") + " to " + moment.utc((slot + this.value.meetingDuration*60)*1000).format("h:mm a")
        },
        getAllTimeslots: function() {
            var nums = Math.floor((this.value.endTime-this.value.startTime)*60/this.value.meetingDuration)
            var slots = []
            var last = this.value.startTime * 60 * 60
            console.log()
            for (var i = 0; i < nums; i++) {
                slots.push([i, last]);
                last+=this.value.meetingDuration*60
            }
            return slots
        },
        next: function() {
            console.log("next")
            if (this.value.days.length >= this.page*5) {
                this.page++
            }
        },
        prev: function() {
            if (this.page >= 1)
                this.page--
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
                                <button v-if="mode == 'participant'" class="btn btn-primary justify-content-center align-content-between d-flex">
                                    <i class="material-icons mr-1">add</i>
                                    <span>Register</span>
                                </button>
                                <p v-else>[Free]</p>
                            </div>
                            <div v-else-if="slot[0] < date.slots.length && date.slots[slot[0]].organizerAvailable">
                                {{date.slots[slot[0]].meeting.participant}}
                            </div>
                            <div v-else>
                                [Unavailable]
                            </div>
                        </td>
                    </tr>
                </tbody>
            </table>
        </div>
    `
})