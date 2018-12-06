function wdayonly(d, reverse = false) {
    if (!reverse) {
        if (d.weekday() == 0)
            d.add(1, 'day')
        if (d.weekday() == 6)
            d.add(2, 'day')
    } else {
        if (d.weekday() == 0)
            d.subtract(2, 'day')
        if (d.weekday() == 6)
            d.subtract(1, 'day')
    }
    return d
}

$(document).ready(function () {
    console.log("Start")
    var meeting_create_vue = new Vue({
        el: '#meeting-create-vue',
        data: {
            name: 'Meeting Schedule',
            duration: 30,
            startselected: '8',
            endselected: '17',
            watchlock: false,
            startDate: '',
            endDate: '',
            showValidationAlert: false,
            options: [
                { text: '12:00 AM', value: '0', s_enable: true, e_enable: false },
                { text: '1:00 AM', value: '1', s_enable: true, e_enable: true },
                { text: '2:00 AM', value: '2', s_enable: true, e_enable: true },
                { text: '3:00 AM', value: '3', s_enable: true, e_enable: true },
                { text: '4:00 AM', value: '4', s_enable: true, e_enable: true },
                { text: '5:00 AM', value: '5', s_enable: true, e_enable: true },
                { text: '6:00 AM', value: '6', s_enable: true, e_enable: true },
                { text: '7:00 AM', value: '7', s_enable: true, e_enable: true },
                { text: '8:00 AM', value: '8', s_enable: true, e_enable: true },
                { text: '9:00 AM', value: '9', s_enable: true, e_enable: true },
                { text: '10:00 AM', value: '10', s_enable: true, e_enable: true },
                { text: '11:00 AM', value: '11', s_enable: true, e_enable: true },
                { text: '12:00 PM', value: '12', s_enable: true, e_enable: true },
                { text: '1:00 PM', value: '13', s_enable: true, e_enable: true },
                { text: '2:00 PM', value: '14', s_enable: true, e_enable: true },
                { text: '3:00 PM', value: '15', s_enable: true, e_enable: true },
                { text: '4:00 PM', value: '16', s_enable: true, e_enable: true },
                { text: '5:00 PM', value: '17', s_enable: true, e_enable: true },
                { text: '6:00 PM', value: '18', s_enable: true, e_enable: true },
                { text: '7:00 PM', value: '19', s_enable: true, e_enable: true },
                { text: '8:00 PM', value: '20', s_enable: true, e_enable: true },
                { text: '9:00 PM', value: '21', s_enable: true, e_enable: true },
                { text: '10:00 PM', value: '22', s_enable: true, e_enable: true },
                { text: '11:00 PM', value: '23', s_enable: false, e_enable: true },
            ]
        },
        watch: {
            startselected: function (val, oldval) {
                if (this.watchlock) return
                this.watchlock = true;
                if (parseInt(val) >= parseInt(this.endselected)) {
                    this.endselected = (parseInt(val) + 1).toString()
                }
                this.watchlock = false;
            },
            endselected: function (val, oldval) {
                if (this.watchlock) return
                this.watchlock = true;
                if (parseInt(val) <= parseInt(this.startselected)) {
                    this.startselected = (parseInt(val) - 1).toString()
                }
                this.watchlock = false;
            },
            startDate: function (val, oldval) {
                if (this.watchlock) return
                this.watchlock = true;
                var diff = moment(this.endDate).diff(moment(this.startDate), 'minutes')
                if (diff <= 0) {
                    this.endDate = wdayonly(moment(this.startDate).add(1, 'day')).format("MM/DD/YYYY")
                    $('#meetingScheduleEndDate').datepicker('update', this.endDate)
                }
                this.watchlock = false;
            },
            endDate: function (val, oldval) {
                if (this.watchlock) return
                this.watchlock = true;
                var diff = moment(this.endDate).diff(moment(this.startDate), 'minutes')
                if (diff <= 0) {
                    this.startDate = wdayonly(moment(this.endDate).subtract(1, 'day'), true).format("MM/DD/YYYY")
                    $('#meetingScheduleStartDate').datepicker('update', this.startDate)
                }
                this.watchlock = false;
            }
        },
        methods: {
            submit: function (event) {
                if (this.$refs.form.checkValidity() === false) {
                    event.preventDefault();
                    event.stopPropagation();
                    this.showValidationAlert = true;
                    his.$refs.form.classList.add('was-validated');
                } else {
                    var api_content = {}
                    api_content["name"] = this.name
                    api_content["start"] = moment(this.startDate).format("YYYY-MM-DD")
                    api_content["end"] = moment(this.endDate).format("YYYY-MM-DD")
                    api_content["meetingDuration"] = this.duration
                    api_content["startHour"] = parseInt(this.startselected)
                    api_content["endHour"] = parseInt(this.endselected)
                    this.showValidationAlert = false;
                    
                    //Hide the modal once we have a new body
                    var add_url = "https://97xvmjynw9.execute-api.us-east-1.amazonaws.com/Alpha/organizer/createschedule";
                    var self = this
                    $.ajax({url: add_url, 
                        type: 'POST',
                        dataType: 'json',
                        success: function(result){
                            self.didSubmit(result)
                        },
                        error: function(resp) {
                            alert("Error!")
                        },
                        data: JSON.stringify(api_content)
                    });
                }
            },
            didSubmit: function(resulta) {
                if (resulta.httpCode == 200) {
                    this.$refs.form.classList.remove('was-validated');
                    var get_url = "https://97xvmjynw9.execute-api.us-east-1.amazonaws.com/Alpha/organizer/getschedule?secretKey=" + resulta.secretKey;
                    var self = this
                    $.ajax({url: get_url, 
                        type: 'GET',
                        success: function(result){
                            if (result.httpcode == 200) {
                                self.showAlert = false;
                                meeting_grid_vue.grid_data = result.data;
                                meeting_grid_vue.hasKey = true;
                                open_modal.secretKey = resulta.secretKey;
                                meeting_grid_vue.key = resulta.secretKey;
                                console.log("Eventually use this data: " + JSON.stringify(result))
                                share_modal.secretKey = resulta.secretKey;
                                share_modal.id = result.data.scheduleID;
                                $('#newModal').modal('hide')
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
                } else {
                    alert("Error")
                }
            }
        }
    })

    window.meeting_grid_vue = new Vue({
        el: '#meeting-echedule-holder-vue',
        data: {
            hasKey: false,
            grid_data: {},
            key: null
        },
        methods: {
            deletefunc: function() {
                console.log("func")
                var self = this
                $.ajax({url: "https://97xvmjynw9.execute-api.us-east-1.amazonaws.com/Alpha/organizer/deleteschedule", 
                    type: 'DELETE',
                    success: function(result){
                        if (result.httpCode == 200) {
                            location.reload(); //reload page
                        } else {
                            console.log("backend http code not 200")
                            self.failedDelete = true
                            alert("Couldn't delete the schedule")
                        }
                    },
                    error: function(resp) {
                        console.log("ERROR, ", resp)
                        alert("Couldn't delete the schedule")
                    },
                    dataType: 'json',
                    data: JSON.stringify({
                        secretKey: self.key,
                        scheduleID: self.grid_data.scheduleID
                    })
                });
            },
            reload: function() {
                open_modal.reload();
            }
        }
    });

    var open_modal = new Vue({
        el: '#openModal',
        data: {
            showAlert: false,
            secretKey: ""
        },
        methods: {
            submit: function() {
                if (this.secretKey == "") {
                    this.showAlert = true
                } else {
                    this.showAlert = false
                    this.reload();
                }
            },
            reload: function() {
                var get_url = "https://97xvmjynw9.execute-api.us-east-1.amazonaws.com/Alpha/organizer/getschedule?secretKey=" + this.secretKey;
                    var self = this
                    $.ajax({url: get_url, 
                        type: 'GET',
                        success: function(result){
                            if (result.httpcode == 200) {
                                self.showAlert = false;
                                meeting_grid_vue.grid_data = result.data;
                                meeting_grid_vue.hasKey = true;
                                share_modal.secretKey = self.secretKey;
                                meeting_grid_vue.key = self.secretKey;
                                share_modal.id = result.data.scheduleID;
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
    });

    window.share_modal = new Vue({
        el: '#shareModal',
        data: {
            secretKey: "",
            id: ""
        },
        watch: {
            secretKey: function(val, old) {
                if (this.secretKey != "") {
                    $("#shareButton").show()
                } else {
                    $("#shareButton").hide()
                }
            }
        }
    })

    //TODO: Delete
    //loadSchedule("AABBCCDDEE")

    var d = moment()
    wdayonly(d);
    meeting_create_vue.startDate = d.format("MM/DD/YYYY");
    d.add(7, 'day')
    meeting_create_vue.endDate = d.format("MM/DD/YYYY");
    $(function () {
        $('#meetingScheduleStartDate').datepicker({
            format: 'mm/dd/yyyy',
            daysOfWeekDisabled: [0, 6]
        }).on("changeDate", function (e) {
            meeting_create_vue.startDate = e.format();
        });
        $('#meetingScheduleEndDate').datepicker({
            format: 'mm/dd/yyyy',
            daysOfWeekDisabled: [0, 6]
        }).on("changeDate", function (e) {
            meeting_create_vue.endDate = e.format();
        });
    });
})