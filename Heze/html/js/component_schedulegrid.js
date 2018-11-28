Vue.component('meeting-schedule-grid', {
    props: ['mode', 'value'],
    data: function () {
        return {
        }
    },
    methods: {
        convertToDayString: function(date) {
            return moment.utc(date*1000).format("dddd MM/DD/YYYY")
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
        }
    },
    template: `
        <table class="table">
            <thead>
                <th>Timeslot</th>
                <th v-for="day in value.dates">{{convertToDayString(day.date)}}</th>
            </thead>
            <tbody>
                <tr v-for="slot in getAllTimeslots()">
                    <td>{{convertToSlotNameString(slot[1])}}</td>
                    <td v-for="date in value.dates">
                        <div v-if="slot[0] < date.timeslots.length && date.timeslots[slot[0]].meeting == null && date.timeslots[slot[0]].organizerAvailable">
                            [Free]
                        </div>
                        <div v-else-if="slot[0] < date.timeslots.length && date.timeslots[slot[0]].organizerAvailable">
                            {{date.timeslots[slot[0]].meeting.participant}}
                        </div>
                        <div v-else>
                            [Unavailable]
                        </div>
                    </td>
                </tr>
            </tbody>
        </table>
    `
})