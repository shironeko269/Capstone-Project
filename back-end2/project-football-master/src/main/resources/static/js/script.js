// export default function SaveAndUpdateScheduleService(scheduleId){
//     var quantity=[], arrServiceQty=[], arrObj2 = [];
//     quantity = document.querySelectorAll("input");
//     for (let i = 8; i < quantity.length; i++) {
//         arrServiceQty.push(quantity[i].value);
//     }
//     var arrServiceId = [];
//     arrServiceId = document.querySelectorAll(".card-body > p:nth-child(1)");
//     for (let i = 0; i < arrServiceQty.length; i++) {
//         console.log(arrServiceId[i], arrServiceQty[i])
//         var objService={servicePitchId:arrServiceId[i].innerText, quantity:arrServiceQty[i]};
//         arrObj2.push(objService);
//     }
//     arrObj2.forEach(value => {
//         console.log(value);
//     });
//     var json = JSON.stringify(arrObj2);
//     console.log("json", json);
//     var URL = "/edit-booking-service/"+scheduleId
//     console.log(URL)
//     $.ajax({
//         type: "POST",
//         url: URL,
//         data: json,
//         contentType: "application/json; charset=utf-8",
//         dataType: "json",
//         success: function (result) {
//             swal({
//                 title: "Cập nhật dịch vụ đơn đặt sân!",
//                 text: "Phí dịch vụ trả thêm: "+result.data+"đ",
//                 icon: "success",
//                 button: "Ok!",
//             }).then((value) => {
//                 location.reload();
//             });
//
//         }
//     });
// }

const SaveAndUpdateScheduleService = (scheduleId) => {
    var quantity=[], arrServiceQty=[], arrObj2 = [];
    quantity = document.querySelectorAll("input");
    for (let i = 8; i < quantity.length - 1; i++) {
        arrServiceQty.push(quantity[i].value);
    }
    var arrServiceId = [];
    arrServiceId = document.querySelectorAll(".card-body > p:nth-child(1)");
    for (let i = 0; i < arrServiceQty.length; i++) {
        console.log(arrServiceId[i], arrServiceQty[i])
        var objService={servicePitchId:arrServiceId[i].innerText, quantity:arrServiceQty[i]};
        arrObj2.push(objService);
    }
    arrObj2.forEach(value => {
        console.log(value);
    });
    var json = JSON.stringify(arrObj2);
    console.log("json", json);
    var URL = "/edit-booking-service/"+scheduleId
    console.log(URL)
    $.ajax({
        type: "POST",
        url: URL,
        data: json,
        contentType: "application/json; charset=utf-8",
        dataType: "json",
        success: function (result) {
            swal({
                title: "Cập nhật dịch vụ đơn đặt sân!",
                text: "Phí dịch vụ trả thêm: "+result.data+"đ",
                icon: "success",
                button: "Ok!",
            }).then((value) => {
                location.reload();
            });

        }
    });
}