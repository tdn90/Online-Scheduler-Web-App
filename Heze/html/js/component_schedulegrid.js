Vue.component('meeting-schedule-grid', {
    props: ['mode'],
    data: function () {
        return {
            startTime: 9,
            endTime:   17,
            meetingDuration: 45,
            dates: [
                {
                    date: 1543276800,
                    id: "asd",
                    timeslots: [
                        {
                            id:"dsa",
                            start:1543309200,
                            organizerAvailable: true,
                            meeting: {
                                id: "asdasdasd",
                                participant: "John Doe"
                            }
                        },
                        {
                            id:"dsa",
                            start:1543395600,
                            organizerAvailable: true,
                            meeting: null
                        }
                    ]
                },
                {
                    date: 1543363200,
                    id: "asd",
                    timeslots: [
                        {
                            id:"dsa",
                            start:1543395600,
                            organizerAvailable: true,
                            meeting: {
                                id: "asdasdasd",
                                participant: "Jane Doe"
                            }
                        },
                        {
                            id:"dsa",
                            start:1543395600,
                            organizerAvailable: true,
                            meeting: null
                        }
                    ]
                },
            ]
        }
    },
    methods: {
        convertToDayString: function(date) {
            return moment.utc(date*1000).format("dddd MM/DD/YYYY")
        },
        convertToSlotNameString: function(slot) {
            return moment.utc(slot*1000).format("h:mm a") + " to " + moment.utc((slot + this.meetingDuration*60)*1000).format("h:mm a")
        },
        getAllTimeslots: function() {
            var nums = Math.floor((this.endTime-this.startTime)*60/this.meetingDuration)
            var slots = []
            var last = this.startTime * 60 * 60
            console.log()
            for (var i = 0; i < nums; i++) {
                slots.push([i, last]);
                last+=this.meetingDuration*60
            }
            return slots
        }
    },
    template: `
        <table class="table">
            <thead>
                <th>Timeslot</th>
                <th v-for="day in dates">{{convertToDayString(day.date)}}</th>
            </thead>
            <tbody>
                <tr v-for="slot in getAllTimeslots()">
                    <td>{{convertToSlotNameString(slot[1])}}</td>
                    <td v-for="date in dates">
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