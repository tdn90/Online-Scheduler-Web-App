$(document).ready(function(){
    var dummy_grid_data = {
        startTime: 9,
        endTime:   17,
        meetingDuration: 45,
        id: "asdasdasd",
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
                    grid_holder.grid_data = dummy_grid_data
                    grid_holder.has_data = true
                    $('#openModal').modal('hide')
                }
            }
        }
    });

})