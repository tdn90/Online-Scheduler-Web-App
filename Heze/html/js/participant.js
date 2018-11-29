$(document).ready(function(){
    var dummy_grid_data = JSON.parse(`
        {
            "scheduleID": "10b31760b7",
            "days": [
                {
                    "id": "4f156d7a34024a678ec64a69161f3a",
                    "date": {
                        "date": {
                            "year": 2018,
                            "month": 11,
                            "day": 29
                        },
                        "time": {
                            "hour": 0,
                            "minute": 0,
                            "second": 0,
                            "nano": 0
                        }
                    },
                    "slots": [
                        {
                            "timeslotID": "1206591482474bcdaba1284f799055",
                            "startTime": {
                                "hour": 10,
                                "minute": 30
                            },
                            "meetingDuration": 30,
                            "meeting": null,
                            "organizerAvailable": false
                        },
                        {
                            "timeslotID": "1256b63f0af644c9871ef4be949435",
                            "startTime": {
                                "hour": 15,
                                "minute": 0
                            },
                            "meetingDuration": 30,
                            "meeting": null,
                            "organizerAvailable": false
                        },
                        {
                            "timeslotID": "3399fb06fd3b4b029b5767c33b3bd0",
                            "startTime": {
                                "hour": 11,
                                "minute": 30
                            },
                            "meetingDuration": 30,
                            "meeting": null,
                            "organizerAvailable": false
                        },
                        {
                            "timeslotID": "48c072de49cb4ec5b3aca8ba2a96e7",
                            "startTime": {
                                "hour": 12,
                                "minute": 0
                            },
                            "meetingDuration": 30,
                            "meeting": null,
                            "organizerAvailable": false
                        },
                        {
                            "timeslotID": "55887542699247b2a80a442b2b5104",
                            "startTime": {
                                "hour": 11,
                                "minute": 0
                            },
                            "meetingDuration": 30,
                            "meeting": null,
                            "organizerAvailable": false
                        },
                        {
                            "timeslotID": "6a9687ad348c4ed8ba2166e1fb88c0",
                            "startTime": {
                                "hour": 14,
                                "minute": 0
                            },
                            "meetingDuration": 30,
                            "meeting": null,
                            "organizerAvailable": false
                        },
                        {
                            "timeslotID": "820c0f3817894aae9610672f190d83",
                            "startTime": {
                                "hour": 13,
                                "minute": 30
                            },
                            "meetingDuration": 30,
                            "meeting": null,
                            "organizerAvailable": false
                        },
                        {
                            "timeslotID": "94b3bb926a8843a0a1906351190542",
                            "startTime": {
                                "hour": 10,
                                "minute": 0
                            },
                            "meetingDuration": 30,
                            "meeting": null,
                            "organizerAvailable": true
                        },
                        {
                            "timeslotID": "baaf1545eb844d3e893e0c605d97b6",
                            "startTime": {
                                "hour": 16,
                                "minute": 30
                            },
                            "meetingDuration": 30,
                            "meeting": null,
                            "organizerAvailable": false
                        },
                        {
                            "timeslotID": "bf0157ced3f846beb38a8f31814c30",
                            "startTime": {
                                "hour": 12,
                                "minute": 30
                            },
                            "meetingDuration": 30,
                            "meeting": null,
                            "organizerAvailable": false
                        },
                        {
                            "timeslotID": "c00f0bedc75f4352b1142cf0649be0",
                            "startTime": {
                                "hour": 13,
                                "minute": 0
                            },
                            "meetingDuration": 30,
                            "meeting": null,
                            "organizerAvailable": false
                        },
                        {
                            "timeslotID": "c464a9356808465fb10e5f2fda83d5",
                            "startTime": {
                                "hour": 14,
                                "minute": 30
                            },
                            "meetingDuration": 30,
                            "meeting": null,
                            "organizerAvailable": false
                        },
                        {
                            "timeslotID": "c7f7f64c885641339d5b60084450c5",
                            "startTime": {
                                "hour": 9,
                                "minute": 0
                            },
                            "meetingDuration": 30,
                            "meeting": null,
                            "organizerAvailable": false
                        },
                        {
                            "timeslotID": "d4efebc14c7547f28fa00a3c896a2d",
                            "startTime": {
                                "hour": 9,
                                "minute": 30
                            },
                            "meetingDuration": 30,
                            "meeting": null,
                            "organizerAvailable": false
                        },
                        {
                            "timeslotID": "e67e5fac8e03494fa84b6fd349a47f",
                            "startTime": {
                                "hour": 17,
                                "minute": 0
                            },
                            "meetingDuration": 30,
                            "meeting": null,
                            "organizerAvailable": false
                        },
                        {
                            "timeslotID": "f8f34103c6bd4ae6aa4a66c410cf0e",
                            "startTime": {
                                "hour": 16,
                                "minute": 0
                            },
                            "meetingDuration": 30,
                            "meeting": null,
                            "organizerAvailable": false
                        },
                        {
                            "timeslotID": "faf964b194b041a89f9011a6394561",
                            "startTime": {
                                "hour": 15,
                                "minute": 30
                            },
                            "meetingDuration": 30,
                            "meeting": null,
                            "organizerAvailable": false
                        }
                    ]
                },
                {
                    "id": "7023392c4c2a44c2b2c74a4e97d98c",
                    "date": {
                        "date": {
                            "year": 2018,
                            "month": 12,
                            "day": 3
                        },
                        "time": {
                            "hour": 0,
                            "minute": 0,
                            "second": 0,
                            "nano": 0
                        }
                    },
                    "slots": [
                        {
                            "timeslotID": "27dd7aaa2ed740c1ad04a3d505bae6",
                            "startTime": {
                                "hour": 11,
                                "minute": 0
                            },
                            "meetingDuration": 30,
                            "meeting": null,
                            "organizerAvailable": false
                        },
                        {
                            "timeslotID": "27ecf6a7f84f4d4799edd22c5eb345",
                            "startTime": {
                                "hour": 13,
                                "minute": 30
                            },
                            "meetingDuration": 30,
                            "meeting": null,
                            "organizerAvailable": false
                        },
                        {
                            "timeslotID": "41a42913cec943ff8583463be0e440",
                            "startTime": {
                                "hour": 13,
                                "minute": 0
                            },
                            "meetingDuration": 30,
                            "meeting": null,
                            "organizerAvailable": false
                        },
                        {
                            "timeslotID": "577d15c49020489ab9c8db79fdd6b5",
                            "startTime": {
                                "hour": 10,
                                "minute": 0
                            },
                            "meetingDuration": 30,
                            "meeting": null,
                            "organizerAvailable": false
                        },
                        {
                            "timeslotID": "7ea05370a08d4d57bd2aee357cae72",
                            "startTime": {
                                "hour": 12,
                                "minute": 30
                            },
                            "meetingDuration": 30,
                            "meeting": null,
                            "organizerAvailable": false
                        },
                        {
                            "timeslotID": "94411780f7b046d086d5c70a817018",
                            "startTime": {
                                "hour": 14,
                                "minute": 30
                            },
                            "meetingDuration": 30,
                            "meeting": null,
                            "organizerAvailable": false
                        },
                        {
                            "timeslotID": "988a157021524dd0a94ad0481c1ef6",
                            "startTime": {
                                "hour": 10,
                                "minute": 30
                            },
                            "meetingDuration": 30,
                            "meeting": null,
                            "organizerAvailable": false
                        },
                        {
                            "timeslotID": "a2d1d7c56c854bc5883c6fdde36acf",
                            "startTime": {
                                "hour": 12,
                                "minute": 0
                            },
                            "meetingDuration": 30,
                            "meeting": null,
                            "organizerAvailable": false
                        },
                        {
                            "timeslotID": "a53a81b888304cce971390a7226f8d",
                            "startTime": {
                                "hour": 9,
                                "minute": 30
                            },
                            "meetingDuration": 30,
                            "meeting": null,
                            "organizerAvailable": false
                        },
                        {
                            "timeslotID": "c1b18f6b53da4b998ff4e5f0bd05c8",
                            "startTime": {
                                "hour": 17,
                                "minute": 0
                            },
                            "meetingDuration": 30,
                            "meeting": null,
                            "organizerAvailable": false
                        },
                        {
                            "timeslotID": "c4d2560d9f4b445681e69846574faa",
                            "startTime": {
                                "hour": 9,
                                "minute": 0
                            },
                            "meetingDuration": 30,
                            "meeting": null,
                            "organizerAvailable": false
                        },
                        {
                            "timeslotID": "c9f133f22b694a2785eacf419179be",
                            "startTime": {
                                "hour": 15,
                                "minute": 30
                            },
                            "meetingDuration": 30,
                            "meeting": null,
                            "organizerAvailable": false
                        },
                        {
                            "timeslotID": "d792d87c710a4ee8a0e2e2dd2825e2",
                            "startTime": {
                                "hour": 11,
                                "minute": 30
                            },
                            "meetingDuration": 30,
                            "meeting": null,
                            "organizerAvailable": false
                        },
                        {
                            "timeslotID": "dc734b2c6c0b40d68f8e4d2f90c777",
                            "startTime": {
                                "hour": 14,
                                "minute": 0
                            },
                            "meetingDuration": 30,
                            "meeting": null,
                            "organizerAvailable": false
                        },
                        {
                            "timeslotID": "e399453647bb410d84b921409e022f",
                            "startTime": {
                                "hour": 16,
                                "minute": 30
                            },
                            "meetingDuration": 30,
                            "meeting": null,
                            "organizerAvailable": false
                        },
                        {
                            "timeslotID": "e8fce7d4d3024d5e9f041a105a45ef",
                            "startTime": {
                                "hour": 15,
                                "minute": 0
                            },
                            "meetingDuration": 30,
                            "meeting": null,
                            "organizerAvailable": false
                        },
                        {
                            "timeslotID": "f2a777c0eadd4cea86432660cb6c06",
                            "startTime": {
                                "hour": 16,
                                "minute": 0
                            },
                            "meetingDuration": 30,
                            "meeting": null,
                            "organizerAvailable": false
                        }
                    ]
                },
                {
                    "id": "76fee4d7a57f48d5b14f6426c986e0",
                    "date": {
                        "date": {
                            "year": 2018,
                            "month": 12,
                            "day": 4
                        },
                        "time": {
                            "hour": 0,
                            "minute": 0,
                            "second": 0,
                            "nano": 0
                        }
                    },
                    "slots": [
                        {
                            "timeslotID": "062599447a174ee190e8498447a6df",
                            "startTime": {
                                "hour": 14,
                                "minute": 0
                            },
                            "meetingDuration": 30,
                            "meeting": null,
                            "organizerAvailable": false
                        },
                        {
                            "timeslotID": "0d0ad5b9b9654e0880737649b32f29",
                            "startTime": {
                                "hour": 9,
                                "minute": 30
                            },
                            "meetingDuration": 30,
                            "meeting": null,
                            "organizerAvailable": false
                        },
                        {
                            "timeslotID": "1f7e2f7c1076437bbfcf5552d0bbd8",
                            "startTime": {
                                "hour": 10,
                                "minute": 30
                            },
                            "meetingDuration": 30,
                            "meeting": null,
                            "organizerAvailable": false
                        },
                        {
                            "timeslotID": "220ba503e7fd4119bdad3eb6e0d3b7",
                            "startTime": {
                                "hour": 11,
                                "minute": 30
                            },
                            "meetingDuration": 30,
                            "meeting": null,
                            "organizerAvailable": false
                        },
                        {
                            "timeslotID": "2242fc7baeb84ca19e746f0e1461c1",
                            "startTime": {
                                "hour": 10,
                                "minute": 0
                            },
                            "meetingDuration": 30,
                            "meeting": null,
                            "organizerAvailable": false
                        },
                        {
                            "timeslotID": "26a136b5e0c8452d8bcc35a3a97bc9",
                            "startTime": {
                                "hour": 12,
                                "minute": 30
                            },
                            "meetingDuration": 30,
                            "meeting": null,
                            "organizerAvailable": false
                        },
                        {
                            "timeslotID": "66f9a2cfd3204b42ad5771884614d8",
                            "startTime": {
                                "hour": 13,
                                "minute": 30
                            },
                            "meetingDuration": 30,
                            "meeting": null,
                            "organizerAvailable": false
                        },
                        {
                            "timeslotID": "6bb2301552544c8881f97391fb1c87",
                            "startTime": {
                                "hour": 9,
                                "minute": 0
                            },
                            "meetingDuration": 30,
                            "meeting": null,
                            "organizerAvailable": false
                        },
                        {
                            "timeslotID": "78672c9f73e546869e6a463d53ae12",
                            "startTime": {
                                "hour": 15,
                                "minute": 30
                            },
                            "meetingDuration": 30,
                            "meeting": null,
                            "organizerAvailable": false
                        },
                        {
                            "timeslotID": "9b3b6b308ebd440eb124945738b0f1",
                            "startTime": {
                                "hour": 13,
                                "minute": 0
                            },
                            "meetingDuration": 30,
                            "meeting": null,
                            "organizerAvailable": false
                        },
                        {
                            "timeslotID": "a38fa04906944d1ab6ff91887be44a",
                            "startTime": {
                                "hour": 16,
                                "minute": 30
                            },
                            "meetingDuration": 30,
                            "meeting": null,
                            "organizerAvailable": false
                        },
                        {
                            "timeslotID": "b65a39dc0aac41cc939f691f9d5ae5",
                            "startTime": {
                                "hour": 17,
                                "minute": 0
                            },
                            "meetingDuration": 30,
                            "meeting": null,
                            "organizerAvailable": false
                        },
                        {
                            "timeslotID": "d26fc40fbc4b486a87e51d03774925",
                            "startTime": {
                                "hour": 11,
                                "minute": 0
                            },
                            "meetingDuration": 30,
                            "meeting": null,
                            "organizerAvailable": false
                        },
                        {
                            "timeslotID": "d63b3359aacc4d438094e6ca333f9e",
                            "startTime": {
                                "hour": 14,
                                "minute": 30
                            },
                            "meetingDuration": 30,
                            "meeting": null,
                            "organizerAvailable": false
                        },
                        {
                            "timeslotID": "d942db5f314143c79f464d04d6d61c",
                            "startTime": {
                                "hour": 16,
                                "minute": 0
                            },
                            "meetingDuration": 30,
                            "meeting": null,
                            "organizerAvailable": false
                        },
                        {
                            "timeslotID": "ed91552f8e454082991c57d0330616",
                            "startTime": {
                                "hour": 15,
                                "minute": 0
                            },
                            "meetingDuration": 30,
                            "meeting": null,
                            "organizerAvailable": false
                        },
                        {
                            "timeslotID": "fc03ac8d84d548968fb392f7e3f485",
                            "startTime": {
                                "hour": 12,
                                "minute": 0
                            },
                            "meetingDuration": 30,
                            "meeting": null,
                            "organizerAvailable": false
                        }
                    ]
                },
                {
                    "id": "c2c0a62586be4f769d343fbb33ec2d",
                    "date": {
                        "date": {
                            "year": 2018,
                            "month": 11,
                            "day": 30
                        },
                        "time": {
                            "hour": 0,
                            "minute": 0,
                            "second": 0,
                            "nano": 0
                        }
                    },
                    "slots": [
                        {
                            "timeslotID": "0e24c408c957408781bdcb4d5e1ae6",
                            "startTime": {
                                "hour": 16,
                                "minute": 0
                            },
                            "meetingDuration": 30,
                            "meeting": null,
                            "organizerAvailable": false
                        },
                        {
                            "timeslotID": "0f973b93b5564db39e80c787410128",
                            "startTime": {
                                "hour": 12,
                                "minute": 0
                            },
                            "meetingDuration": 30,
                            "meeting": null,
                            "organizerAvailable": false
                        },
                        {
                            "timeslotID": "3668941ddaf448aa9ca32cd52492d1",
                            "startTime": {
                                "hour": 10,
                                "minute": 0
                            },
                            "meetingDuration": 30,
                            "meeting": null,
                            "organizerAvailable": false
                        },
                        {
                            "timeslotID": "397cdfb8902c4af7a0ecf7444eedc6",
                            "startTime": {
                                "hour": 13,
                                "minute": 0
                            },
                            "meetingDuration": 30,
                            "meeting": null,
                            "organizerAvailable": false
                        },
                        {
                            "timeslotID": "3b06a6258f9f468dbd2e40bb93f948",
                            "startTime": {
                                "hour": 10,
                                "minute": 30
                            },
                            "meetingDuration": 30,
                            "meeting": null,
                            "organizerAvailable": false
                        },
                        {
                            "timeslotID": "45ab8f9e7e7741dda111f770497cd7",
                            "startTime": {
                                "hour": 9,
                                "minute": 0
                            },
                            "meetingDuration": 30,
                            "meeting": null,
                            "organizerAvailable": false
                        },
                        {
                            "timeslotID": "482ea60505d64cb38598f76b06eca8",
                            "startTime": {
                                "hour": 16,
                                "minute": 30
                            },
                            "meetingDuration": 30,
                            "meeting": null,
                            "organizerAvailable": false
                        },
                        {
                            "timeslotID": "5aa362097936447fb90b1a3dac2af2",
                            "startTime": {
                                "hour": 13,
                                "minute": 30
                            },
                            "meetingDuration": 30,
                            "meeting": null,
                            "organizerAvailable": false
                        },
                        {
                            "timeslotID": "8024bcab2df9423b9e52cc1c877c91",
                            "startTime": {
                                "hour": 11,
                                "minute": 30
                            },
                            "meetingDuration": 30,
                            "meeting": null,
                            "organizerAvailable": false
                        },
                        {
                            "timeslotID": "895248f6de81417dbee0a0d771427b",
                            "startTime": {
                                "hour": 14,
                                "minute": 0
                            },
                            "meetingDuration": 30,
                            "meeting": null,
                            "organizerAvailable": false
                        },
                        {
                            "timeslotID": "8a05dee2e3a6440aa52d1086d6b294",
                            "startTime": {
                                "hour": 14,
                                "minute": 30
                            },
                            "meetingDuration": 30,
                            "meeting": null,
                            "organizerAvailable": false
                        },
                        {
                            "timeslotID": "9b8d59e1089e47abb72b8bebf95b96",
                            "startTime": {
                                "hour": 11,
                                "minute": 0
                            },
                            "meetingDuration": 30,
                            "meeting": null,
                            "organizerAvailable": false
                        },
                        {
                            "timeslotID": "a831c78b62104f0f969bfb65b7747b",
                            "startTime": {
                                "hour": 17,
                                "minute": 0
                            },
                            "meetingDuration": 30,
                            "meeting": null,
                            "organizerAvailable": false
                        },
                        {
                            "timeslotID": "d06aef7dcc2b4e1fa8e54ffa31f1dd",
                            "startTime": {
                                "hour": 15,
                                "minute": 0
                            },
                            "meetingDuration": 30,
                            "meeting": null,
                            "organizerAvailable": false
                        },
                        {
                            "timeslotID": "e52cfbeb4b2045708c62cc7142b6f6",
                            "startTime": {
                                "hour": 15,
                                "minute": 30
                            },
                            "meetingDuration": 30,
                            "meeting": null,
                            "organizerAvailable": false
                        },
                        {
                            "timeslotID": "fdca0c4e24bb428b8bf4fe1ef401c8",
                            "startTime": {
                                "hour": 12,
                                "minute": 30
                            },
                            "meetingDuration": 30,
                            "meeting": null,
                            "organizerAvailable": false
                        },
                        {
                            "timeslotID": "ff183d8dbf714a37b45ffc8614613f",
                            "startTime": {
                                "hour": 9,
                                "minute": 30
                            },
                            "meetingDuration": 30,
                            "meeting": null,
                            "organizerAvailable": false
                        }
                    ]
                }
            ],
            "schedule_secretKey": "484c5f18047a4854b0ac7c3f06b522",
            "name": "string",
            "startTime": 9,
            "endTime": 17,
            "meetingDuration": 30
        }
    `)

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