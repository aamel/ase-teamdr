function createForm1(notif) {
    return {
        view:"form",
        elements:[
            { view:"label", label:notif.message},
            { margin:5, cols:[
                { id: "btnAccept" + notif.notifID, view:"button", value:"Accept", type:"form" },
                { id: "btnReject" + notif.notifID, view:"button", value:"Reject" }
            ]}
        ]
    }
}

function accept(notif) {
    $.ajax({
        url: '/acceptNotification',
        type: 'POST',
        dataType: 'json',
        data: JSON.stringify(notif),
        contentType: 'application/json',
        success: function(data, textStatus, jqXHR) {
            webix.message(data.message);
        }
    });
}

function reject(notif) {
    $.ajax({
        url: '/rejectNotification',
        type: 'POST',
        dataType: 'json',
        data: JSON.stringify(notif),
        contentType: 'application/json',
        success: function(data, textStatus, jqXHR) {
            webix.message(data.message);
        }
    });
}

function getNotifs() {
    $.ajax({
        url: '/notifs',
        type: 'GET',
        contentType: 'application/json',
        success: function(data, textStatus, jqXHR) {
            for (i=0; i<data.length; i++) {
                var notif = data[i];
                if (data[i].messageType == 1) {
                    $$('notifForms').addView(createForm1(notif), -1);
                    $$('btnAccept' + notif.notifID).attachEvent("onItemClick", function(id) {
                        accept(id.substring(9));
                    });
                    $$('btnReject' + notif.notifID).attachEvent("onItemClick", function(id) {
                        reject(id.substring(9));
                    });
                }
            }
        }
    });
}

webix.ready(function () {
    webix.ui({
      rows:[
          { view:"template",
            type:"header", template:"Notifications" },
          {
            id: "notifForms", rows: []
          }
      ]
    });

    getNotifs();
});
