$(document).ready(function(){
    console.log("Start")
    $(function () {
        $('#meetingScheduleStartDate').datepicker({
            format: 'mm/dd/yyyy',
            daysOfWeekDisabled: [0,6]
        });
        $('#meetingScheduleEndDate').datepicker({
            format: 'mm/dd/yyyy',
            daysOfWeekDisabled: [0,6]
        });
    });
})