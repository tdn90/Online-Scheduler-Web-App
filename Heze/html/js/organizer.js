function wdayonly(d, reverse=false) {
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

function loadSchedule(secretKey) {
    window.meeting_grid_vue.setKey(secretKey)
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
                { text: '12:00 AM', value: '0', s_enable: true, e_enable: false},
                { text: '1:00 AM', value: '1', s_enable: true, e_enable: true},
                { text: '2:00 AM', value: '2', s_enable: true, e_enable: true},
                { text: '3:00 AM', value: '3', s_enable: true, e_enable: true},
                { text: '4:00 AM', value: '4', s_enable: true, e_enable: true},
                { text: '5:00 AM', value: '5', s_enable: true, e_enable: true},
                { text: '6:00 AM', value: '6', s_enable: true, e_enable: true},
                { text: '7:00 AM', value: '7', s_enable: true, e_enable: true},
                { text: '8:00 AM', value: '8', s_enable: true, e_enable: true},
                { text: '9:00 AM', value: '9', s_enable: true, e_enable: true},
                { text: '10:00 AM', value: '10', s_enable: true, e_enable: true},
                { text: '11:00 AM', value: '11', s_enable: true, e_enable: true},
                { text: '12:00 PM', value: '12', s_enable: true, e_enable: true},
                { text: '1:00 PM', value: '13', s_enable: true, e_enable: true},
                { text: '2:00 PM', value: '14', s_enable: true, e_enable: true},
                { text: '3:00 PM', value: '15', s_enable: true, e_enable: true},
                { text: '4:00 PM', value: '16', s_enable: true, e_enable: true},
                { text: '5:00 PM', value: '17', s_enable: true, e_enable: true},
                { text: '6:00 PM', value: '18', s_enable: true, e_enable: true},
                { text: '7:00 PM', value: '19', s_enable: true, e_enable: true},
                { text: '8:00 PM', value: '20', s_enable: true, e_enable: true},
                { text: '9:00 PM', value: '21', s_enable: true, e_enable: true},
                { text: '10:00 PM', value: '22', s_enable: true, e_enable: true},
                { text: '11:00 PM', value: '23', s_enable: false, e_enable: true},
            ]
        },
        watch: {
            startselected: function (val, oldval) {
                if (this.watchlock) return
                this.watchlock = true;
                if (parseInt(val) >= parseInt(this.endselected)) {
                    this.endselected = (parseInt(val)+1).toString()
                }
                this.watchlock = false;
            },
            endselected: function (val, oldval) {
                if (this.watchlock) return
                this.watchlock = true;
                if (parseInt(val) <= parseInt(this.startselected)) {
                    this.startselected = (parseInt(val)-1).toString()
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
            submit: function(event) {
                if (this.$refs.form.checkValidity() === false) {
                    event.preventDefault();
                    event.stopPropagation();
                    this.showValidationAlert = true;
                    his.$refs.form.classList.add('was-validated');
                } else {
                    var api_content = {}
                    api_content["name"] = this.name
                    api_content["start"] = moment(this.startDate).valueOf()
                    api_content["end"] = moment(this.endDate).valueOf()
                    api_content["meetingDuration"] = this.duration
                    api_content["startHour"] = parseInt(this.startselected)
                    api_content["endHour"] = parseInt(this.endselected)
                    this.showValidationAlert = false;
                    alert("Will do XMLHTTPRequest with body: \n\n" + JSON.stringify(api_content))
                    //Hide the modal once we have a new body
                    this.$refs.form.classList.remove('was-validated');
                    loadSchedule("AABBCCDD")
                    $('#newModal').modal('hide')
                }
            }
        }
    })

    window.meeting_grid_vue = new Vue({
        el: '#meeting-echedule-holder-vue',
        data: {
            hasKey: false,
            key: null
        },
        methods: {
            setKey: function(k) {
                //TODO: check secret key
                this.hasKey = true
            }
        }
    });

    //TODO: Delete
    loadSchedule("AABBCCDDEE")

    var d = moment()
    wdayonly(d);
    meeting_create_vue.startDate = d.format("MM/DD/YYYY");
    d.add(7, 'day')
    meeting_create_vue.endDate = d.format("MM/DD/YYYY");
    $(function () {
        $('#meetingScheduleStartDate').datepicker({
            format: 'mm/dd/yyyy',
            daysOfWeekDisabled: [0, 6]
        }).on("changeDate", function(e) {
            meeting_create_vue.startDate = e.format();
        });
        $('#meetingScheduleEndDate').datepicker({
            format: 'mm/dd/yyyy',
            daysOfWeekDisabled: [0, 6]
        }).on("changeDate", function(e) {
            meeting_create_vue.endDate = e.format();
        });
    });
})